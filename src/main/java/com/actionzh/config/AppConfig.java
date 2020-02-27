package com.actionzh.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Wills
 * @date 2019/1/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@PropertySource("classpath:batch.properties")
public class AppConfig {


    @Value("${batch.retry.initialInterval}")
    private int batchInitialBackOffInterval;

    @Value("${batch.retry.maxInterval}")
    private int batchMaxBackOffInterval;

    @Value("${batch.retry.limit}")
    private int batchRetryLimit;

    @Value("${batch.skip.limit}")
    private int batchSkipLimit;


}
