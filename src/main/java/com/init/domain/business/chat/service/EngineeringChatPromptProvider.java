package com.init.domain.business.chat.service;

import com.init.domain.persistence.engineering.entity.EngineeringKnowledge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EngineeringChatPromptProvider {

    public String createPrompt(String question, List<EngineeringKnowledge> knowledges) {
        String context = formatContext(knowledges);
        return getTemplate().formatted(context, question);
    }

    private String formatContext(List<EngineeringKnowledge> knowledges) {
        return knowledges.stream()
                .map(this::formatSingleKnowledge)
                .collect(Collectors.joining("\n\n"));
    }

    private String formatSingleKnowledge(EngineeringKnowledge p) {
        return """
[Product]
- Product Type: %s
- Category: %s
- Equipment: %s

[Component]
- System: %s
- Part Name (KO/EN): %s

[Material]
- %s

[Description]
%s
""".formatted(
                p.getProductType(),
                p.getCategory(),
                p.getEquipment(),
                p.getSystemClassification(),
                p.getPartName(),
                p.getMaterial(),
                p.getContent()
        );
    }

    private String getTemplate() {
        return """
You are a senior mechanical and systems engineer.

Your task is to answer the user's question using ONLY the provided engineering context.
You must follow all rules strictly.

[Answering Rules]
1. Base your answer strictly on the given context. Do NOT assume or invent information.
2. Use precise engineering terminology.
3. When referring to components, ALWAYS show the name in both Korean and English.
   - Format: Korean Name (English Name)
   - Example: 코일 스프링 (Coil Spring)
4. Preserve original technical names exactly as provided in the context.
5. If multiple components are involved, explain their relationship clearly.
6. Structure your answer logically:
   - Overview
   - Operating principle
   - Engineering implications or considerations
7. If the context is insufficient, explicitly state that the information is not available.

[Engineering Context]
%s

[User Question]
%s

[Answer Format]
- Use Korean as the primary language.
- Include English technical terms in parentheses on first mention.
- Write in a professional engineering tone.
""";
    }
}
