package com.chainway.newjtbinterfaceservice.config;

import com.chainway.newjtbinterfaceservice.config.interfaces.IVerify;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Verify implements IVerify {

    @Override
    public boolean verify(String data, long timestamp, String encodedEncryptedStr, X509Certificate userCert) throws Exception {
        return verify(data.getBytes("utf-8"), timestamp, encodedEncryptedStr,
                userCert);
    }

    @Override
    public boolean verify(String data, String encodedEncryptedStr, X509Certificate userCert) throws Exception {
        return verify(data.getBytes("utf-8"), 0, encodedEncryptedStr, userCert);
    }

    @Override
    public boolean verify(byte[] data, String encodedEncryptedStr, X509Certificate userCert) throws Exception {
        return verify(data, encodedEncryptedStr, userCert);
    }

    public boolean verify(byte[] data, long timestamp, String encodedEncryptedStr, X509Certificate userCert) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        if (timestamp > 0) {
            md.update(EncodeUtil.toBE(timestamp));
        }
        byte[] hash = md.digest();
        byte[] encryptedStr = HexBin.decode(encodedEncryptedStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, userCert);
        byte[] plain = cipher.doFinal(encryptedStr);
        boolean ok = Arrays.equals(hash, plain);
        return ok;
    }
}
