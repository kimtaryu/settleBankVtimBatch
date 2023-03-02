package com.kokkan.batch.job.convert;

import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
@Slf4j
public class Builder<T extends AbstractConvertVo> {

    private final FixedLengthBuilder fixedLengthBuilder;


    public byte[] build(T buildVo) {
        return build(buildVo, "EUC-KR", true);
    }


    public byte[] build(T buildVo, String charSet, boolean logging) {

        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            Field[] bodyFields = buildVo.getClass().getDeclaredFields();
            byte[] bodyBytes = builder(buildVo, bodyFields, charSet, logging);

            try {
                os.write(bodyBytes);
            } catch (Exception e) {
                throw new UnCheckedException(ResCodeEnum.C9999, e);
            }

            return os.toByteArray();
        } catch (UnCheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9094, e);
        }

    }

    private byte[] builder(T buildVo, Field[] fields, String charSet, boolean logging) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            for (Field field : fields) {

                FixedLength fixedLength = field.getAnnotation(FixedLength.class);
                field.setAccessible(true);
//				if(fixedLength == null || field.get(buildVo) == null) continue;
                if (fixedLength == null) continue;


                if (field.getType().getSuperclass() == AbstractConvertVo.class) {
                    out.write(build((T) field.get(buildVo), charSet, logging));
                    continue;
                }


                byte[] bData = fixedLengthBuilder.dataBuild(fixedLength, buildVo, field, charSet);

                if (logging) {

                    if (bData != null) {

                        if (field.getType() == String.class) {
                            log.debug(bData.length + "\t/ " + fixedLength.name() + " : " + new String(bData, charSet));
                        } else if (field.getType() == byte[].class) {
                            log.debug(bData.length + "\t/ " + fixedLength.name() + "(Hex) : " + Hex.encodeHexString(bData));
                        }

                    } else {
                        log.debug(bData.length + "\t/ " + fixedLength.name() + " : null!!");
                    }
                }

                out.write(bData);
            }

        } catch (UnCheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9999, e);
        }

        return out.toByteArray();

    }
}
