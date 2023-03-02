package com.kokkan.batch.job.convert;

import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import com.kokkan.batch.job.util.AppUtil;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;


/**
 * @author PC-2036
 */
@Component
class FixedLengthParser {

    /**
     * 데이터 파싱 처리
     *
     * @param idx            : clientReqBytes의 index
     * @param clientReqBytes : 요청 데이터
     * @param fixedLength    : 어노테이션
     * @param object         : 파싱 받을 객체
     * @param field          : 파싱 받을 객체 내 필드
     * @return
     */
    public <T> int dataParse(int idx, byte[] clientReqBytes, FixedLength fixedLength, T object, Field field, String charSet) {

        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            if (fixedLength.length() > 0) {
                /*
                 * 고정 길이 정보가 있을 경우 clientReqBytes의 idx부터 fixedLength.length()길이까지 데이터를 파싱 받는다
                 */
                os.write(AppUtil.byteSubtract(clientReqBytes, idx, fixedLength.length()));
                idx += fixedLength.length();

            } else if (fixedLength.variableLength() > 0) {
                /*
                 * 가변 길이 정보가 있을 경우 가변 길이 정보로 파싱 처리
                 */


                int length;    //가변길이
                byte[] bLength = new byte[fixedLength.variableLength()]; // 가변데이터의 길이정보의 길이

                /*
                 * 길이 정보 파싱 시 길이 정보가 맨 앞이 아닐 경우를 대비하여 variableLengthPosition 만큼 이동 후 길이 정보 파싱
                 */
                System.arraycopy(clientReqBytes, idx + fixedLength.variableLengthPosition(), bLength, 0, fixedLength.variableLength());


                /*
                 * 길이 정보가 String이 아닌 bytes 일 경우
                 */
                if (fixedLength.variableLengthDataType() == fixedLength.DT_BINARY) {
                    if (fixedLength.variableLength() == 1) {
                        /*
                         * 현재는 길이 정보가 byte일 경우 1byte로 길이를 표한하기 때문에 해당 케이스만 존재
                         */
                        length = bLength[0];
                    } else {
                        throw new UnCheckedException(ResCodeEnum.C9999, "지원하지 가변길이정보의 길이 1 != " + fixedLength.variableLength());
                    }

                } else {
                    // 길이 정보가 문자일경우 (길이 정보가 데이터 맨 앞에 위치)
                    String sLength = new String(clientReqBytes, idx, fixedLength.variableLength());
                    if ("".equals(sLength.trim())) sLength = "0";
                    length = Integer.parseInt(sLength);
                }


                if (fixedLength.includeLengthData()) {
                    length += fixedLength.variableLengthPosition() + fixedLength.variableLength();
                } else {
                    idx += fixedLength.variableLength() + fixedLength.variableLengthPosition();
                }


                os.write(AppUtil.byteSubtract(clientReqBytes, idx, length));

                /*
                 * 현재까지 읽은 길이 데이터 return
                 */
                idx += length;

            } else if (fixedLength.length() == 0) {
                /*
                 * 길이가 없을 경우 이후 모든 데이터 파싱
                 */
                os.write(AppUtil.byteSubtract(clientReqBytes, idx, clientReqBytes.length - idx));

                idx = clientReqBytes.length;
            } else {
                throw new UnCheckedException(ResCodeEnum.C9999, "지원하지 않는 FixedLength.length : " + fixedLength.name());
            }

            if (field.getType() == String.class) {
                field.set(object, new String(os.toByteArray(), charSet));
            } else if (field.getType() == byte[].class) {
                field.set(object, os.toByteArray());
            } else {
                throw new UnCheckedException(ResCodeEnum.C9999, "지원하지 않는 객체 타입");
            }

        } catch (UnCheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9999, e);
        }

        return idx;
    }


}
