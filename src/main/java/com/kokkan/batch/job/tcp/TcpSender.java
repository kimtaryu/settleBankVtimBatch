package com.kokkan.batch.job.tcp;

import com.kokkan.batch.job.convert.AbstractConvertVo;
import com.kokkan.batch.job.convert.Builder;
import com.kokkan.batch.job.convert.Parser;
import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import com.kokkan.batch.job.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TcpSender<T1 extends AbstractConvertVo, T2 extends AbstractConvertVo> {

    private final TcpUtil tcpUtil;
    private final Parser<AbstractConvertVo> parser;
    private final Builder<AbstractConvertVo> builder;

    public TcpDto transmit(T1 send, T2 receive, TcpConfig config, TcpDataWrapper tcpDataWrapper) {
        TcpDto tcpDto = new TcpDto();
        String reqLog = null;
        String resLog = null;

        try {

            byte[] buildBytes;
            byte[] sendBytes;
            sendBytes = builder.build(send);
            reqLog = new String(sendBytes, "EUC-KR");

            /*
            요청 데이터 생성
             */
            if (tcpDataWrapper == null) {
                buildBytes = AppUtil.makeLengthBytes(sendBytes, 4);
            } else {
                /*
                암호화 처리
                 */
                log.info("sendBytes(PLAIN) : " + new String(sendBytes, "EUC-KR"));
                byte[] encBytes = tcpDataWrapper.encryption(sendBytes);
                buildBytes = AppUtil.makeLengthBytes(encBytes, 4);
            }

            /*
            데이터 전송
             */
            byte[] receiveBytes = null;

            receiveBytes = tcpUtil.send(buildBytes, config.getIp(), config.getPort(),
                    config.getConnectionTimeout(), config.getReadTimeout()
            );
//            receiveBytes = "qb2nLmk2eXlVAqWeiu+tT4pIw0YoPEuRNKzvfpRXLBWrEVk/Vjdwib0I2Gy210Ur8P7C97itsoBnHfGUSQo/DVeeLCOh0H57BubNxraAT2TatRLn50uNAMdSI1Nofp+A2NEpF8FM33xa55qJDRj6iGXeNb6atv9cRy4AxxMbJ7Scv6ipQ4veyUcqcwKj1muV5XVcFHFzsQJp6tZFh4l4ETjQkuVx39zEZCz6GeffC2uaJQ3CrQ5OKGcPxtEcdd7olFiWkwFVN7MdxzKloQFm8g==".getBytes();
            /*
            응답 데이터 파싱
             */
            if (tcpDataWrapper == null) {
                resLog = new String(receiveBytes);
                parser.parse(receiveBytes, receive);
            } else {
                /*
                복호화 처리
                 */
                byte[] decBytes = tcpDataWrapper.decryption(receiveBytes);
                resLog = new String(decBytes, "EUC-KR");
                parser.parse(decBytes, receive);
            }

            tcpDto.setTcpResCode("0000");

        } catch (UnCheckedException e) {
            log.error("UnCheckedException : " + e.getMsg(), e);
            tcpDto.setTcpResCode(e.getCode());
            tcpDto.setTcpResMsg(e.getMsg());
        } catch (Exception e) {
            log.error("Exception", e);
            tcpDto.setTcpResCode(ResCodeEnum.C9999.getCode());
            tcpDto.setTcpResMsg(e.getMessage());
        }

        tcpDto.setReqLog(reqLog);
        tcpDto.setResLog(resLog);

        return tcpDto;

    }


}
