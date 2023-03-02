package com.kokkan.batch.job.settlebank;

import com.kokkan.batch.job.convert.Parser;
import com.kokkan.batch.job.tcp.TcpConfig;
import com.kokkan.batch.job.tcp.TcpDto;
import com.kokkan.batch.job.tcp.TcpSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettleBankVanProvider {

    @Value("${kokkan.settlebank.van.ip}")
    private String ip;
    @Value("${kokkan.settlebank.van.port}")
    private int port;
    @Value("${kokkan.settlebank.van.connection-timeout}")
    private int connectionTimeout;
    @Value("${kokkan.settlebank.van.read-timeout}")
    private int readTimeout;
    private final TcpSender<RemitReqVo, RemitResVo> tcpSender;
    private final SBTcpDataWrapper sbTcpDataWrapper;
    private final Parser parser;


    /**
     * VTIM 오류 발생 시 재시도
     */
    public TcpDto<RemitReqVo, RemitResVo> remitRetry(String reqData) {


        HeaderVo headerVo = null;
        RemitReqVo reqVo = null;
        RemitResVo resVo = new RemitResVo();

        try {
            reqVo = new RemitReqVo();

            parser.parse(reqData.getBytes("EUC-KR"), reqVo);

            /*
            데이터 전송 및 파싱
             */
            TcpConfig config = TcpConfig.builder()
                    .ip(ip)
                    .port(port)
                    .connectionTimeout(connectionTimeout)
                    .readTimeout(readTimeout)
                    .build();

            TcpDto<RemitReqVo, RemitResVo> tcpDto = tcpSender.transmit(reqVo, resVo, config, sbTcpDataWrapper);
            tcpDto.setReqVo(reqVo);
            tcpDto.setResVo(resVo);

            return tcpDto;
        } catch (Exception e) {
            /*
            시스템 에러 발생 시 에러 코드 리턴
             */
            TcpDto<RemitReqVo, RemitResVo> tcpDto = new TcpDto<>();
            tcpDto.setReqVo(reqVo);
            tcpDto.setResVo(resVo);

            return tcpDto;
        }

    }

}
