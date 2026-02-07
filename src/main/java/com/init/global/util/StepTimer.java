package com.init.global.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepTimer {
    private long last;
    private final String traceId;

    public StepTimer(String traceId) {
        this.traceId = traceId;
        this.last = System.nanoTime();
    }

    public void log(String step) {
        long now = System.nanoTime();
        long elapsedMs = (now - last) / 1_000_000;
        log.info("[{}] {} took {} ms", traceId, step, elapsedMs);
        last = now;
    }
}
