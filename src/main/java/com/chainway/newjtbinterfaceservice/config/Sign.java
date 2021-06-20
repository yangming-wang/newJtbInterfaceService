package com.chainway.newjtbinterfaceservice.config;

import com.chainway.newjtbinterfaceservice.config.interfaces.ISign;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;


import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.PrivateKey;


public class Sign implements ISign {

    /**
     * @param data      需要签名的数据
     * @param timestamp 时间戳
     * @param key       证书私钥
     * @return 签名加密串
     * @throws Exception
     */
    public String sign(byte[] data, long timestamp, PrivateKey key)
            throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        if (timestamp > 0) {
            md.update(EncodeUtil.toBE(timestamp));
        }

        byte[] hash = md.digest();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(hash);
        return HexBin.encode(encrypted);
    }

    @Override
    public String sign(String data, PrivateKey key) throws Exception {
        return sign(data.getBytes("utf-8"), 0, key);
    }

    @Override
    public String sign(byte[] data, PrivateKey key) throws Exception {
        return sign(data, 0, key);
    }

    public String sign(String data, long timestamp, PrivateKey key)
            throws Exception {
        return sign(data.getBytes("utf-8"), timestamp, key);
    }

    // 转换成十六进制字符串
    public static String Byte2String(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


}
