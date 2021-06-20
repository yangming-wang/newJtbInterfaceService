package com.chainway.newjtbinterfaceservice.config;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;

/**
 * Java代码生成自颁发 cer证书、base64 cer证书文件
 * 没啥用  这里  的  生成公钥
 */
public class CreateCerFile {

    @Value("${CTFC.path}")
    private String path;
    @Value("${CTFC.DOMAIN_NAME}")
    private static String CTFC_DOMAIN_NAME;
    @Value("${CTFC.ORG.ORG_UNIT_NAME}")
    private static String CTFC_ORG_UNIT_NAME ;
    @Value("${CTFC.ORG.ORG_NAME}")
    private static String CTFC_ORG_NAME ;
    @Value("${CTFC.COUNTRY_CODE}")
    private static String CTFC_COUNTRY_CODE ;
    @Value("${CTFC.CITY}")
    private static String CTFC_CITY ;
    @Value("${CTFC.PROVINCE}")
    private static String CTFC_PROVINCE ;
    @Value("${CTFC.path}")
    private static String CTFC_VALID_START_TIME ;
    @Value("${CTFC.path}")
    private static String CTFC_VALID_END_TIME ;
    @Value("${CTFC.SERIAL_NUMBER}")
    private static String CTFC_SERIAL_NUMBER ;
    @Value("${CTFC.SIG_AlG}")
    private static String CTFC_SIG_AlG ;
    @Value("${CTFC.ENCRYPT_TYPE}")
    private static String CTFC_ENCRYPT_TYPE ;
    @Value("${CTFC.ENCRYPT_NUM}")
    private static String CTFC_ENCRYPT_NUM ;
    @Value("${CTFC.PROVIDER}")
    private static String CTFC_PROVIDER ;



    static {
        Security.addProvider(new BouncyCastleProvider());
    }




    /**
     * 生成cer证书
     * @param infoMap
     * @param keyPair_root
     * @param keyPair_user
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     * @throws SecurityException
     * @throws SignatureException
     */
    public X509Certificate generateCert(HashMap<String,Object> infoMap, KeyPair keyPair_root, KeyPair keyPair_user)
            throws InvalidKeyException, NoSuchProviderException, SecurityException, SignatureException {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X509Certificate cert = null;
        certGen.setSerialNumber((BigInteger)infoMap.get("CTFC_SERIAL_NUMBER"));

        //证书颁发者，这里自颁发，颁发者和使用者一致，可以只是用户或域名，也可以是部分或完整的用户信息（OU：组织单位名称、O：组织单位、C：单位的两字母国家代码、L：城市或区域、ST：省份或州）
//        certGen.setIssuerDN(new X509Name("CN="+ (String)infoMap.get("CTFC_DOMAIN_NAME")));
        certGen.setIssuerDN(new X509Name("CN="+ (String)infoMap.get("CTFC_DOMAIN_NAME")
//        	+ ", OU=" + (String)infoMap.get(CTFC_ORG_UNIT_NAME)
        		+ ", O=" + (String)infoMap.get("CTFC_ORG_NAME")));
//        			+ ", C=" + (String)infoMap.get(CTFC_COUNTRY_CODE)
//        				+ ", L=" + (String)infoMap.get("CTFC_CITY")));
//        					+ ", ST=" + (String)infoMap.get("CTFC_PROVINCE")));

        certGen.setNotBefore((Date)infoMap.get("CTFC_VALID_START_TIME"));
        certGen.setNotAfter((Date)infoMap.get("CTFC_VALID_END_TIME"));

        //证书使用者，这里自颁发，使用者和颁发者一致，可以只是用户或域名，也可以是完整的用户信息（组织单位名称、组织单位、国家代码、城市或区域、省份或州）
//        certGen.setSubjectDN(new X509Name("CN=" + (String)infoMap.get("CTFC_DOMAIN_NAME")));
        certGen.setSubjectDN(new X509Name("CN="+ (String)infoMap.get("CTFC_DOMAIN_NAME")
//        		+ ", OU=" + (String)infoMap.get(CTFC_ORG_UNIT_NAME)
        			+ ", O=" + (String)infoMap.get("CTFC_ORG_NAME")));
//        				+ ", C=" + (String)infoMap.get(CTFC_COUNTRY_CODE)
//        					+ ", L=" + (String)infoMap.get("CTFC_CITY")));
//        						+ ", ST=" + (String)infoMap.get(CTFC_PROVINCE)));
        certGen.setPublicKey(keyPair_user.getPublic());
        certGen.setSignatureAlgorithm((String)infoMap.get("CTFC_SIG_AlG"));


        cert = certGen.generateX509Certificate(keyPair_root.getPrivate(), (String)infoMap.get("CTFC_PROVIDER")); //BC：证书提供人
        return cert;
    }

    /**
     * 生成密钥对   生成cer证书需要的参数
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateKeyPair(HashMap<String,Object> infoMap,int seed) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance((String)infoMap.get("CTFC_ENCRYPT_TYPE"));
        kpg.initialize((int)infoMap.get("CTFC_ENCRYPT_NUM"),new SecureRandom(new byte[seed]));
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }


    /**
     * 生成原格式 cer证书 生成cer证书需要的参数
     * @return
     */
    public boolean createCerFile(HashMap<String,Object> infoMap) {
        try {
            System.out.println(infoMap);
            //两个密钥对
            KeyPair keyPair_root = generateKeyPair(infoMap,10);
            KeyPair keyPair_user = generateKeyPair(infoMap,10);

            X509Certificate cert = generateCert(infoMap, keyPair_root, keyPair_user);
//            System.out.println(cert.toString());
//            String cerString = new BASE64Encoder().encode(cert.getEncoded());
//            System.out.println(cerString);

            //生成cer证书文件
            String certPath = path + infoMap.get("CTFC_DOMAIN_NAME") + ".cer";
            FileOutputStream fos = new FileOutputStream(certPath);
            fos.write(cert.getEncoded()); //证书可以二进制形式存入库表，存储字段类型为BLOB
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("createPublicKey error：" + e.getMessage());
            return false;
        }
    }

    /**
     * 生成base64格式 cer证书
     * @param infoMap
     * @return
     */
    public boolean createBase64CerFileByDecode(HashMap<String,Object> infoMap) {
        try {
            KeyPair keyPair_root = generateKeyPair(infoMap,10);
            KeyPair keyPair_user = generateKeyPair(infoMap,10);
            X509Certificate cert = generateCert(infoMap, keyPair_root, keyPair_user);
            String certPath = "E:/" + infoMap.get("CTFC_DOMAIN_NAME") + "_base64.cer";

            String encode = new BASE64Encoder().encode(cert.getEncoded());
            String base64EncodeCer = "-----BEGIN CERTIFICATE-----\r\n" + encode + "\r\n-----END CERTIFICATE-----\r\n";
//            System.out.println(base64EncodeCer);

            //生成base64 cer证书文件
            FileWriter wr = new java.io.FileWriter(new File(certPath));
            wr.write(base64EncodeCer);
            wr.flush();
            wr.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("createPublicKeyByDecode error：" + e.getMessage());
            return false;
        }
    }


}
