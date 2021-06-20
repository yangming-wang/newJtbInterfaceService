package com.chainway.newjtbinterfaceservice.cer;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chainway.newjtbinterfaceservice.config.CreateCerFile;
import org.junit.Test;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 */
public class CreateCerFileTest {

    @Test
    public  void  Certificategeneration() throws NoSuchAlgorithmException, InvalidKeyException,
            NoSuchProviderException, SecurityException, SignatureException, CertificateEncodingException, IOException {
        try{
            CreateCerFile dataCertCreate = new CreateCerFile();
            //证书有效开始时间
            Date validStartTime = new Date();
            Calendar cal=Calendar.getInstance();//获取一个Calendar对象
            cal.setTime(validStartTime);
//            cal.add(Calendar.DATE,n);//增加n天
//            cal.add(Calendar.MONTH,n);//增加n个月
            cal.add(Calendar.YEAR,10);//增加n年
            System.out.println("当前时间:"+validStartTime);
            System.out.println("输出今天的十年后:"+cal.getTime());
            //证书有效结束时间
            Date validEndTime=cal.getTime();
            //生成序列号域
            BigInteger serialNumber = new BigInteger(String.valueOf(validStartTime.getTime() / 1000L));
            //正式开始生成证书
            HashMap<String,Object> infoMap = new HashMap<String,Object>();
            infoMap.put("CTFC_DOMAIN_NAME","chainway 驾培成为");		//CN：用户姓名或域名
            infoMap.put("CTFC_ORG_UNIT_NAME", "驾培计时");         //OU：组织单位名称
            infoMap.put("CTFC_ORG_NAME", "成为信息技术有限公司");          		//O：组织名称
            infoMap.put("CTFC_COUNTRY_CODE", "CW");           		//C：单位的两字母国家代码
            infoMap.put("CTFC_CITY", "深圳市");              			//L：城市或区域
            infoMap.put("CTFC_PROVINCE", "广东省");          			//ST：省份或州
            infoMap.put("CTFC_VALID_START_TIME", validStartTime); 	//证书有效起始时间
            infoMap.put("CTFC_VALID_END_TIME", validEndTime);   		//证书有效截止时间
            infoMap.put("CTFC_SERIAL_NUMBER", serialNumber);  		//序列号域
            infoMap.put("CTFC_SIG_AlG", "SHA256withRSA");  			//签名算法
            infoMap.put("CTFC_ENCRYPT_TYPE", "RSA");  				//加密类型
            infoMap.put("CTFC_ENCRYPT_NUM", 2048);  					//加密位数
            infoMap.put("CTFC_PROVIDER", "BC");  						//提供人
            // 生成公钥
            boolean createCerFileRs = dataCertCreate.createCerFile(infoMap);
            System.out.println("createCerFile, result==" + createCerFileRs);

            boolean createBase64CerFileByDecodeRs = dataCertCreate.createBase64CerFileByDecode(infoMap);
            System.out.println("createBase64CerFileByDecode, result==" + createBase64CerFileByDecodeRs);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        CreateCerFile dataCertCreate = new CreateCerFile();
        Date validStartTime = new Date();

    }

    @Test
    public void a(){
        JSONObject provinceImgJsonArr = JSONUtil.parseObj("error学员照片不存在");
        System.out.println(provinceImgJsonArr);
    }


}
