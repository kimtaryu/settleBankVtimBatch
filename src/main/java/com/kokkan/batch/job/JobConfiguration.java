package com.kokkan.batch.job;


import com.kokkan.batch.job.slack.SlackService;
import com.kokkan.batch.job.step.StepConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepConfiguration stepConfiguration;

    private final SlackService slackService;


    @Value("${kokkan.job.name}")
    private String jobName;


    @Bean
    public Job settlebankRetryJob() {
        return jobBuilderFactory.get("settlebankRetryJob")
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        // nothing
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                        // 로그나 슬랙 알림? 등등? 필요하면 추가
//                        StringBuffer logData = new StringBuffer();
                        StringBuffer slackData = new StringBuffer();

                        if(!"COMPLETED".equals(jobExecution.getStatus().toString())) {

                            for(StepExecution stepExecution : jobExecution.getStepExecutions()) {

                                if(!"COMPLETED".equals(stepExecution.getStatus().toString())) {

//                                    String errorMsg = (String)jobExecution.getExecutionContext().get(AppVar.ERROR_MSG);
//
//                                    if(errorMsg != null) {
//                                        logData.append("Error Msg : " + errorMsg);
//                                        slackData.append("Error Msg : " + errorMsg);
//                                    } else {
//                                        logData.append("Error Msg : " + stepExecution.getFailureExceptions());
//                                        slackData.append("Error Msg : " + stepExecution.getFailureExceptions());
//                                    }

                                    slackData.append("Error Msg : " + stepExecution.getFailureExceptions());

                                }
                            }

                        }

                        slackService.send(slackData.toString());

                    }
                })
                .start(stepConfiguration.retryStep())
                .build();
    }


}
