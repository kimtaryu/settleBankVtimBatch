package com.kokkan.batch.job.step.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class RetryReader {

    private final DataSource dataSource;

    @Value("${kokkan.job.chunkSize}")
    private int chunkSize;
    @Value("${kokkan.job.settlebank.providerType}")
    private String providerType; // 새틀뱅크 provider type
    @Value("${kokkan.job.settlebank.rejectCodes}")
    private List<String> rejectCodes; // 새틀뱅크 타임아웃 응답코드

    /**
     * SettleBank VTIM, VDBE 거절 건 조회
     * 10번이상 거절 시 - 매뉴얼 처리
     * 5분마다 배치가 돌기 때문에 24시간이 지난 데이터는 조회 X
     */
    @StepScope
    public JdbcCursorItemReader<RetryDto> selectPaymentBankLog() {

        log.debug("RetryReader call");

        StringBuffer sb = new StringBuffer();

        sb.append(" select  a.payment_bank_seq, a.res_code, a.request_log, ifnull(a.retry_cnt, 0) as retry_cnt, b.freight_id ");
        sb.append(" from    payment_bank_log a ");
        sb.append("     inner join freight_info b on a.freight_seq = b.freight_seq ");
        sb.append(" where 	a.provider_type = ? and a.res_code in (?, ?) and ifnull(a.retry_cnt, 0) <= 10 ");
        sb.append("     and a.reg_dt >= date_add(now(), interval -24 hour) ");
        sb.append(" order by a.payment_bank_seq ");

        return new JdbcCursorItemReaderBuilder<RetryDto>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(RetryDto.class))
                .sql(sb.toString())
                .name("selectPaymentBankLog")
//                .verifyCursorPosition(true)
                .queryArguments(providerType, rejectCodes.get(0), rejectCodes.get(1))
                .build();
    }
}
