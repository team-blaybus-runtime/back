package com.init.infra.openai.client;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

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
                "messages", messages
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(n -> n.get("choices").get(0).get("message").get("content").asText())
                .block();
    }
}
