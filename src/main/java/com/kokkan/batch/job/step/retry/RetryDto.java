package com.kokkan.batch.job.step.retry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetryDto {

    private Integer paymentBankSeq;

    private String freightId;

    private String resCode;

    private String requestLog;

    private Integer retryCnt;

}
