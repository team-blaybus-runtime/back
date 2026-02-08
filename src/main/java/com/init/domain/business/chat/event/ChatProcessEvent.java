package com.init.domain.business.chat.event;

public record ChatProcessEvent(
    Long userHisId,
    String question,
    String answer
) {}
