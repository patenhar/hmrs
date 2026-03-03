package com.hrms.backend.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

@Slf4j
public class LoggingTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            long start = System.currentTimeMillis();
            try {
                runnable.run();
            } finally {
                long end = System.currentTimeMillis();
                log.info("Task completed in {}ms", end - start);
            }
        };
    }
}
