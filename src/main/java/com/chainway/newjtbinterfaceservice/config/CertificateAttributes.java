package com.chainway.newjtbinterfaceservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Configuration
public class CertificateAttributes {

    public static PrivateKey privateKey;

    @Value("${certificate.password}")
    public static String password;

    @Value("${certificate.version}")
    public static String version;

    public static String cert_sn;

    public static X509Certificate cert;

    @Bean
    public void readPfx() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream is = null;
            is = this.getClass().getResourceAsStream("shenzhenchengwei.pfx");
            keyStore.load(is, password.toCharArray());
            Enumeration<String> aliases = keyStore.aliases();
            //没有包含元素就抛出异常
            if (!aliases.hasMoreElements()){
                throw new RuntimeException("no alias found");
            }
            String alias = aliases.nextElement();
            privateKey = (PrivateKey) keyStore.getKey(alias,
                    password.toCharArray());
            cert = (X509Certificate) keyStore.getCertificate(alias);
            cert_sn = Sign.Byte2String(cert.getSerialNumber().toByteArray());
            cert_sn = cert_sn.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
