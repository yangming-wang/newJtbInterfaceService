package com.chainway.newjtbinterfaceservice.config;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chainway.newjtbinterfaceservice.config.interfaces.ISign;
import com.chainway.newjtbinterfaceservice.config.interfaces.IVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Slf4j
public class HttpUtil extends CertificateAttributes {

    @Resource
    ISign iSign;

    @Resource
    IVerify iVerify;


    public String jsonToString(String str) {
        StringBuffer resultJson = new StringBuffer();
        resultJson.append("[").append(str).append("]");
        str = resultJson.toString();
        return str;
    }

    public JSONObject sign_post(String url, String service, String data) {
        JSONObject json = new JSONObject();
        try {
            String result = "";
            long timestamp = new Date().getTime();
            String sign_hex = iSign.sign(data, timestamp, privateKey);
            boolean ok = iVerify.verify(data, timestamp, sign_hex, cert);
            if (ok) {
                String postUrl = url + service + "?v=" + version + "&ts=" +
                        timestamp + "&sign=" + sign_hex + "&user=" + cert_sn;
                HttpRequestUtil http = new HttpRequestUtil();
                log.error("postUrl:" + postUrl);
                result = http.post(postUrl, data.trim());
                log.error("result:" + result);
                json = JSONUtil.parseObj(result);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 照片上传
     * 1.下载照片文件判断是否存在
     * 2.根据 要签名的接口name（data），证书私钥（key） ，时间戳 生成（签名）加密字符串，
     * 3.对签名的接口name（data），如果有时间戳的话，就进行加密拼接在一起。对  签名加密字符串进行hex解码
     * 通过cert证书序列号进行DECRYPT_MODE模式cipher解密 ，对解码解密签名跟前面的加密hash进行比较，以此来验证数据的有效性
     *
     * @param inUrl
     * @param outUrl
     * @param service
     * @param type
     * @param fileName
     * @return
     */
    public JSONObject Photo_formFileUpload(String inUrl, String outUrl, String service, String type, String fileName) {
        JSONObject json = new JSONObject();
        try {
            log.error("照片地址" + inUrl);
            long timestamp = new Date().getTime();
            byte[] data = getBytes(inUrl);
            if ("stuimg".equals(type)) {
                if (data.length == 0) {
                    json.put("errorcode", 1);
                    json.put("message", "error学员照片不存在");
                    return json;
                }
                if (data.length > 1048576) {
                    json.put("errorcode", 1);
                    json.put("message", "error学员照片大于1M");
                    return json;
                }
            }
            if (((("simulation".equals(type)) || ("classroom".equals(type)) || ("onlineimg".equals(type)))) &&
                    (data.length < 1024)) {
                throw new RuntimeException("获取文件失败！");
            }
            //签名
            String sign_hex = iSign.sign(data, timestamp, privateKey);
            boolean ok = iVerify.verify(data, timestamp, sign_hex, cert);
            if (ok) {
                String url = outUrl + service + "/" + type + "?v=" + version +
                        "&ts=" + timestamp + "&sign=" + sign_hex + "&user=" +
                        cert_sn;
                log.error("url:" + url);
                HttpRequestUtil http = new HttpRequestUtil();
                String result = http.formFileUpload(data, url, fileName);
                log.error("result:" + result);
                json = JSONUtil.parseObj(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 访问照片静态url地址取得byte64
     *
     * @param strUrl
     * @return
     */
    public byte[] getBytes(String strUrl) {
        ByteArrayOutputStream baos = null;
        try {
            URL u = new URL(strUrl);
            BufferedImage image = ImageIO.read(u);
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException localIOException1) {
                }
        }
        return (baos == null) ? new byte[0] : baos.toByteArray();

    }

}
