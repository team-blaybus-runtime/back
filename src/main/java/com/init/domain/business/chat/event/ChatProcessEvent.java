package com.init.domain.business.chat.event;

public record ChatProcessEvent(
    Long chatRoomId,
    String question,
    String answer
) {}
