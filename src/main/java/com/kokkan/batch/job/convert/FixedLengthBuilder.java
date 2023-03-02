package com.kokkan.batch.job.convert;

import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import com.kokkan.batch.job.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

/**
 * @author PC-2036
 */
@Component
@Slf4j
class FixedLengthBuilder {

    /**
     * @return
     * @ author : PC-2036
     * @ date : 2021. 6. 15.
     */
    public <T> byte[] dataBuild(FixedLength fixedLength, T object, Field field, String charSet) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String fieldName = null;

        try {
            fieldName = field.getName() + "(" + fixedLength.name() + ")";
            byte[] bData = null;

            if (fixedLength.optionField()[0] != 0x00) {
                out.write(fixedLength.optionField());
            }


            if (fixedLength.dataType() == fixedLength.DT_BINARY) {


                if (fixedLength.length() > 0) {

                    bData = AppUtil.checkNullBytes(field.get(object));
                    bData = AppUtil.bytePadding(bData, AppUtil.LEFT, fixedLength.length(), ' ');
                    out.write(bData);

                } else if (fixedLength.variableLength() > 0) {

                    int length = 0;

                    bData = AppUtil.checkNullBytes(field.get(object));

                    length = bData.length;
                    String sLength = AppUtil.leftPad(String.valueOf(length), fixedLength.variableLength(), '0');

                    out.write(sLength.getBytes());
                    out.write(bData);

                } else {
                    out.write(AppUtil.checkNullBytes(field.get(object)));
                }

            } else {

                if (fixedLength.length() > 0) {

                    String data = AppUtil.checkNull(field.get(object));
                    bData = data.getBytes(charSet);

                    if (fixedLength.dataType() == fixedLength.DT_STRING) {
                        bData = AppUtil.bytePadding(bData, AppUtil.LEFT, fixedLength.length(), ' ');
                    } else {
                        bData = AppUtil.bytePadding(bData, AppUtil.RIGHT, fixedLength.length(), '0');
                    }

                    out.write(bData);

                } else if (fixedLength.variableLength() > 0) {

                    int length = 0;

                    String data = AppUtil.checkNull(field.get(object));
                    bData = data.getBytes();

                    length = bData.length;
                    String sLength = AppUtil.leftPad(String.valueOf(length), fixedLength.variableLength(), '0');

                    out.write(sLength.getBytes());
                    out.write(bData);

                } else {
                    String data = AppUtil.checkNull(field.get(object));
                    out.write(data.getBytes());
                }
            }


        } catch (Exception e) {
            log.error(fieldName + " " + e);
            throw new UnCheckedException(ResCodeEnum.C9094, e);
        }

        return out.toByteArray();

    }

}
