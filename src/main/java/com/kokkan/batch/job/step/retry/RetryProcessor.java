package com.kokkan.batch.job.step.retry;

import com.kokkan.batch.job.settlebank.RemitReqVo;
import com.kokkan.batch.job.settlebank.RemitResVo;
import com.kokkan.batch.job.settlebank.SettleBankVanProvider;
import com.kokkan.batch.job.tcp.TcpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RetryProcessor implements ItemProcessor<RetryDto, RetryDto> {

    private final SettleBankVanProvider settleBankVanProvider;

    @Override
    public RetryDto process(RetryDto retryDto) throws Exception {

        log.debug("재판송금 조회 payment_bank_seq : {}", retryDto.getPaymentBankSeq());

        // 통신 ㄱㄱ
        TcpDto<RemitReqVo, RemitResVo> tcpDto = settleBankVanProvider.remitRetry(retryDto.getRequestLog());

        log.debug("재판송금 결과 : {}", tcpDto.getTcpResCode());

        retryDto.setResCode(tcpDto.getTcpResCode());
        retryDto.setRetryCnt(retryDto.getRetryCnt()+1);

        return retryDto;
    }

}
