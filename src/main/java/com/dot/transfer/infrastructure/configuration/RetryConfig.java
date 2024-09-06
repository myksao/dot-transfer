package com.dot.transfer.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@Slf4j
public class RetryConfig {

    public RetryConfig() {
        super();
        log.info("RetryConfig initialized");
    }
}
