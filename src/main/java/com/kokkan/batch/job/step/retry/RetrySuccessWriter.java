package com.kokkan.batch.job.step.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetrySuccessWriter implements ItemStreamWriter<RetryDto> {

    private final JdbcTemplate jdbcTemplate;

    @Value("${kokkan.job.name}")
    private String jobName;

    @Override
    public void write(List<? extends RetryDto> list) throws Exception {
        // update res_code
        log.debug("RetrySuccessWriter call");

        StringBuffer sb = new StringBuffer();

        sb.append(" update 	payment_bank_log ");
        sb.append(" set		res_code = ?, ");
        sb.append(" 		mod_id = ?, ");
        sb.append(" 		mod_dt = now() ");
        sb.append(" where 	payment_bank_seq = ? ; ");

        jdbcTemplate.batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                int n = 1;
                ps.setString(n++, list.get(i).getResCode());
                ps.setString(n++, jobName);
                ps.setInt(n++, list.get(i).getPaymentBankSeq());
            }

            @Override
            public int getBatchSize() {

                return list.size();
            }
        });

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
