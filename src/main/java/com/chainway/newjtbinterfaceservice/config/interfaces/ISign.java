package com.chainway.newjtbinterfaceservice.config.interfaces;

import java.security.PrivateKey;

/**
 * 加密
 */
public interface ISign {
    public String sign(String data, long timestamp, PrivateKey key) throws Exception;
    public String sign(byte [] data, long timestamp, PrivateKey key) throws Exception;
    public String sign(String data, PrivateKey key) throws Exception;
    public String sign(byte [] data, PrivateKey key) throws Exception;
}
