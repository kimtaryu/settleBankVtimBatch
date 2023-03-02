package com.kokkan.batch.job.tcp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TcpDto<Q, S> {

    @Setter(AccessLevel.PROTECTED)
    private String reqLog;
    @Setter(AccessLevel.PROTECTED)
    private String resLog;
    /**
     * 통신 성공 여부 (거래성공X)
     */
    @Setter(AccessLevel.PROTECTED)
    private String tcpResCode;
    @Setter(AccessLevel.PROTECTED)
    private String tcpResMsg;
    private Q reqVo;
    private S resVo;

}
