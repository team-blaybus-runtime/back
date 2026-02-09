package com.init.infra.openai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    @Value("${openai.chat-model}")
    private String chatModel;

    private WebClient webClient;

    @PostConstruct
    void init(){
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public float[] embed(String text) {
        Map<String, Object> body = Map.of(
                "model", embeddingModel,
                "input", text
        );

        return webClient.post()
                .uri("/embeddings")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(node -> {
                    // 토큰 사용량
                    JsonNode usage = node.get("usage");
                    int promptTokens = usage.get("prompt_tokens").asInt();
                    int totalTokens = usage.get("total_tokens").asInt();

                    log.info(
                            "[OpenAI][EMBEDDING] tokens | prompt={}, total={}",
                            promptTokens, totalTokens
                    );

                    // embedding 벡터
                    JsonNode arr = node.get("data").get(0).get("embedding");
                    float[] result = new float[arr.size()];
                    for (int i = 0; i < arr.size(); i++) {
                        result[i] = arr.get(i).floatValue();
                    }
                    return result;
                })
                .block();
    }

    public String chat(List<Map<String, String>> messages) {
        Map<String, Object> body = Map.of(
                "model", chatModel,
                "messages", messages,
                "stream", false
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(node -> {
                    return node
                            .get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText();
                })
                .block();
    }

    public Flux<String> chatStream(String userPrompt) {
        List<Map<String, String>> messages = List.of(Map.of("role", "user", "content", userPrompt));
        Map<String, Object> body = Map.of(
                "model", chatModel,
                "messages", messages,
                "stream", true
        );

        ParameterizedTypeReference<ServerSentEvent<String>> type =
                new ParameterizedTypeReference<>() {};

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(type)
                .mapNotNull(ServerSentEvent::data)
                .filter(data -> !"[DONE]".equals(data))
                .map(json -> {
                    try {
                        JsonNode node = objectMapper.readTree(json);
                        JsonNode choices = node.get("choices");
                        if (choices != null && choices.isArray() && !choices.isEmpty()) {
                            JsonNode delta = choices.get(0).get("delta");
                            if (delta != null && delta.has("content")) {
                                return delta.get("content").asText();
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error parsing SSE JSON: {}", json, e);
                    }
                    return "";
                })
                .filter(content -> !content.isEmpty());
    }

    public String chat(String userPrompt) {
        return chat(List.of(Map.of("role", "user", "content", userPrompt)));
    }
}
