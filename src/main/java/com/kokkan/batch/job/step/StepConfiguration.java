package com.kokkan.batch.job.step;

import com.kokkan.batch.job.step.retry.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    private final RetryReader retryReader;
    private final RetryProcessor retryProcessor;
    private final RetryClassifierWriter retryClassifierWriter;
    private final RetrySuccessWriter retrySuccessWriter;
    private final RetryFailureWriter retryFailureWriter;

    @Value("${kokkan.job.chunkSize}")
    private int chunkSize;


    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    @JobScope
    public Step retryStep() {
        return stepBuilderFactory.get("retryStep")
                .<RetryDto, RetryDto>chunk(chunkSize)
                .reader(retryReader.selectPaymentBankLog())
                .processor(retryProcessor)
//                .writer(dbToDbWriter)
                .writer(retryClassifierWriter.classifierCompositeItemWriter())
                .stream(retrySuccessWriter)
                .stream(retryFailureWriter)
                .build();
    }
}
