package com.kokkan.batch.job.convert;

import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class Parser<T extends AbstractConvertVo> {

    private final FixedLengthParser fixedLengthParser;

    /**
     * @param reqBytes : 요청 데이터
     * @param reqVo    : 파싱 받을 객체
     */
    public void parse(byte[] reqBytes, T reqVo) {
        int idx = 0;
        parse(reqBytes, reqVo, idx, "EUC-KR");
    }


    /**
     * 파싱에 대한 재귀 처리
     *
     * @param reqBytes
     * @param reqVo
     * @param idx
     * @return
     */
    private int parse(byte[] reqBytes, T reqVo, int idx, String charSet) {
        /*
         *
         */
        Field[] bodyFields = reqVo.getClass().getDeclaredFields();
        idx = parser(reqVo, reqBytes, bodyFields, idx, charSet);
        return idx;

    }


    /**
     * 파싱 처리 (현재는 FixedLength에 대해서만 있으며 다른 파싱 타입이 생길 경우 분기처리)
     *
     * @param reqVo
     * @param reqBytes
     * @param fields
     * @param idx
     * @return
     */
    private int parser(T reqVo, byte[] reqBytes, Field[] fields, int idx, String charSet) {

        try {

            byte[] curCmdCode = null;

            for (Field field : fields) {

                FixedLength fixedLength = field.getAnnotation(FixedLength.class);
                field.setAccessible(true);
                if (fixedLength == null) continue;    //fixedLength가 없을 경우 skip
                if (reqBytes.length == idx) break;    //더이상 파싱 받을 데이터가 없으면 break

                /*
                 * 파싱 받을 필드에 optionField가 있고, 데이터상과 일치한다면 파싱 진행
                 */
                if (fixedLength.optionField()[0] != 0x00 && reqBytes.length > idx + fixedLength.optionField().length) {
                    curCmdCode = new byte[]{reqBytes[idx], reqBytes[idx + 1]};
                    if (!Arrays.equals(curCmdCode, fixedLength.optionField())) {
                        continue;
                    } else {
                        idx += 2;
                    }
                }

                /*
                 * 만일 파싱 받을 필드가 만일 AbstractParseVo 이면 재귀로 내부 변수 데이터 파싱 처리
                 */
                if (field.getType().getSuperclass() == AbstractConvertVo.class) {
                    T subClass = (T) field.getType().newInstance();
                    idx = parse(reqBytes, subClass, idx, charSet);
                    field.set(reqVo, subClass);
                    continue;
                }

                /*
                 * 데이터 파싱
                 */
                idx = fixedLengthParser.dataParse(idx, reqBytes, fixedLength, reqVo, field, charSet);


                if (field.getType() == String.class) {
                    String data = (String) field.get(reqVo);
                    log.debug(data.getBytes().length + "\t/ " + fixedLength.name() + " : " + data);
                } else if (field.getType() == byte[].class) {
                    byte[] data = (byte[]) field.get(reqVo);
                    log.debug(data.length + "\t/ " + fixedLength.name() + " : " + Hex.encodeHexString(data));
                }
            }

        } catch (UnCheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9095, e);
        }

        return idx;

    }

}
