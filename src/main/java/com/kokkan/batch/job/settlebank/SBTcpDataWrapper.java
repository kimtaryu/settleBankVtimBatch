package com.kokkan.batch.job.settlebank;

import com.kokkan.batch.job.tcp.TcpDataWrapper;
import com.kokkan.batch.job.util.SEED;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 새틀 뱅크 VAN 통신 시 아래와 같이 처리
 * 데이터 생성 -> SEED 암호화 -> BASE64 인코딩 -> 전송 -> (이후 역순)
 */
@Component
@RequiredArgsConstructor
public class SBTcpDataWrapper implements TcpDataWrapper {
    @Value("${kokkan.settlebank.van.seedkey}")
    private String key;
    private final SEED seed;
    private final byte[] IV = "0123456789012345".getBytes();    //IV 값은 고정

    @Override
    public byte[] encryption(byte[] data) {
        try {
            return seed.encryptCbcPkcs5PaddingBase64(data, getKey(), IV).getBytes("EUC-KR");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decryption(byte[] encData) {
        try {
            return seed.decryptCbcPkcs5PaddingBase64(encData, getKey(), IV).getBytes("EUC-KR");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    SB VAN에선 안내 한 key 값을 아래와 같은 비트 연산을 거쳐 key를 재 생성 하여 사용
     */
    private byte[] getKey() {
        byte[] bKey = key.getBytes();
        int keyLength = bKey.length;
        for (int i = 0; i < keyLength; ++i) {
            bKey[i] ^= bKey[keyLength - i - 1];
        }
        return bKey;
    }
}
