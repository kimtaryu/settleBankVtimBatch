package com.kokkan.batch.job.util;

import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Security;

@Component
public class SEED {

    public static byte[] encryptCbcPkcs5Padding(byte[] textBytes, byte[] key, byte[] iv){
        Security.addProvider(new BouncyCastleProvider());
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "SEED");
            Cipher c = Cipher.getInstance("SEED/CBC/PKCS5Padding", "BC");
            c.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return c.doFinal(textBytes);
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9999, e);
        }
    }

    public String encryptCbcPkcs5PaddingBase64(byte[] textBytes, byte[] key, byte[] iv) {
        return Base64.encodeBase64String(encryptCbcPkcs5Padding(textBytes, key, iv));
    }


    public static byte[] decryptCbcPkcs5Padding(byte[] textBytes, byte[] key, byte[] iv){
        Security.addProvider(new BouncyCastleProvider());
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "SEED");
            Cipher c = Cipher.getInstance("SEED/CBC/PKCS5Padding", "BC");
            c.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return c.doFinal(textBytes);
        } catch (Exception e) {
            throw new UnCheckedException(ResCodeEnum.C9999, e);
        }
    }

    public String decryptCbcPkcs5PaddingBase64(byte[] textBytes, byte[] key, byte[] iv) {

        try {
            byte[] enc = Base64.decodeBase64(textBytes);
            return new String(decryptCbcPkcs5Padding(enc, key, iv), "EUC-KR");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
