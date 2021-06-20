package com.chainway.newjtbinterfaceservice.config.interfaces;

import java.security.cert.X509Certificate;

/**
 * 解密
 */
public interface IVerify {
    public boolean verify(String data, long timestamp, String encodedEncryptedStr,
                          X509Certificate userCert) throws Exception ;
    public boolean verify(String data, String encodedEncryptedStr,
                          X509Certificate userCert) throws Exception;
    public boolean verify(byte [] data, String encodedEncryptedStr,
                          X509Certificate userCert) throws Exception;
    public boolean verify(byte [] data, long timestamp, String encodedEncryptedStr,
                          X509Certificate userCert) throws Exception;
}
