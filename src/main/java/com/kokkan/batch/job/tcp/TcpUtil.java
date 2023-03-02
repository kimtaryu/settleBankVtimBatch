package com.kokkan.batch.job.tcp;


import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
@Component
public class TcpUtil {

    /**
     *
     */
    protected byte[] send(byte[] sendBytes, String ip, int port, int connectionTimeout, int readTimeout) {

        Socket mSocket = null;
        InputStream mIs = null;
        OutputStream mOs = null;

        byte[] reciveBytes = null;

        try {

            log.info("(" + ip + ":" + port + ")sendData : " + new String(sendBytes, "EUC-KR"));
            log.info("(" + ip + ":" + port + ")sendBytes : " + Hex.encodeHexString(sendBytes));

            try {
                mSocket = new Socket();
                mSocket.connect(new InetSocketAddress(ip, port), connectionTimeout);
                mSocket.setSoTimeout(readTimeout);
            } catch (Exception e) {
                throw new UnCheckedException(ResCodeEnum.C9090, "외부연결 실패 IP : " + ip + ", PORT : " + port, e);
            }


            try {
                mIs = mSocket.getInputStream();
                mOs = mSocket.getOutputStream();
            } catch (Exception e) {
                throw new UnCheckedException(ResCodeEnum.C9091, "getInputStream, getOutputStream 생성 실패", e);
            }

            try {
                mOs.write(sendBytes);
            } catch (Exception e) {
                throw new UnCheckedException(ResCodeEnum.C9092, "데이터 전송 실패", e);
            }

            try {
                reciveBytes = recvData(mIs);
            } catch (Exception e) {
                throw new UnCheckedException(ResCodeEnum.C9093, "데이터 응답 실패", e);
            }

            if (reciveBytes == null) {
                log.info("(" + ip + ":" + port + ")reciveBytes is null");
                throw new UnCheckedException(ResCodeEnum.C9093, "데이터 응답 실패 reciveBytes is null");
            } else {
                log.info("(" + ip + ":" + port + ")reciveData : " + new String(reciveBytes, "EUC-KR"));
                log.info("(" + ip + ":" + port + ")reciveBytes : " + Hex.encodeHexString(reciveBytes));
            }

        } catch (UnCheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9999, e);
        } finally {
            try {
                if (mIs != null) mIs.close();
                if (mOs != null) mOs.close();
                if (mSocket != null) mSocket.close();
            } catch (Exception e2) {
                log.error("Exception e2 : " + e2);
            }
        }

        return reciveBytes;
    }

    private byte[] recvData(InputStream mIs) throws Exception {

        byte[] bLen = new byte[4];
        int i = mIs.read(bLen, 0, 4);

        if (i == -1) {
            return null;
        }

        int nLen = Integer.parseInt(new String(bLen));

        int nIdx = 0;
        byte[] bData = new byte[nLen];
        byte[] bRecv = new byte[nLen];

        for (; ; ) {

            i = mIs.read(bData, 0, nLen - nIdx);
            if (i == -1) {
                break;
            }

            System.arraycopy(bData, 0, bRecv, nIdx, i);
            nIdx += i;

            if (nIdx == nLen) break;

        }

        return bRecv;

    }
}
