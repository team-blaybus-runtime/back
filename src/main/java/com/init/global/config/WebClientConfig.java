package com.init.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
                .build();
        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .tcpConfiguration(client ->
                                                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
                                                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
                                                                .addHandlerLast(new WriteTimeoutHandler(180))
                                                        )
                                        )
                        )
                )
                .exchangeStrategies(exchangeStrategies)
                .filter(
                        ExchangeFilterFunction.ofRequestProcessor(
                                clientRequest -> {
                                    log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                                    logHeader(clientRequest.headers());
                                    logBody(clientRequest);
                                    return Mono.just(clientRequest);
                                }
                        ))
                .filter(
                        ExchangeFilterFunction.ofResponseProcessor(
                                clientResponse -> {
                                    logHeader(clientResponse.headers().asHttpHeaders());
                                    return Mono.just(clientResponse);
                                }
                        ))
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("WebClient error: status={}, body={}", clientResponse.statusCode(), body);
                                    return Mono.error(new RuntimeException("WebClient error: " + body));
                                })
                )
                .build();
    }

    private void logBody(ClientRequest clientRequest) {
        if (clientRequest.body() != null) {
            try {
                log.debug("Request Body: {}", objectMapper.writeValueAsString(clientRequest.body()));
            } catch (JsonProcessingException e) {
                log.debug("Failed to log request body", e);
            }
        } else {
            log.debug("Request Body is empty");
        }
    }

    private static void logHeader(HttpHeaders clientRequest) {
        clientRequest.forEach(
                (name, values) -> values.forEach(value -> log.debug("{} : {}", name, value))
        );
    }
}
