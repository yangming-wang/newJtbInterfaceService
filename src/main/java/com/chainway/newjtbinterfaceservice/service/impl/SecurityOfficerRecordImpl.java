package com.chainway.newjtbinterfaceservice.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chainway.newjtbinterfaceservice.config.Config;
import com.chainway.newjtbinterfaceservice.config.HttpUtil;
import com.chainway.newjtbinterfaceservice.config.KeyCreate;
import com.chainway.newjtbinterfaceservice.mapper.SecurityOfficerMapper;
import com.chainway.newjtbinterfaceservice.packer.Result;
import com.chainway.newjtbinterfaceservice.service.SecurityOfficerRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Hashtable;


@Slf4j
@Service
public class SecurityOfficerRecordImpl implements SecurityOfficerRecord {


    @Resource
    SecurityOfficerMapper securityOfficerMapper;

    @Resource
    HttpUtil httpUtil;


    @Override
    public Result SecurityOfficerRecordMethod(String peopleId) {
        try {
            //刷新数据
            boolean ok = securityOfficerMapper.Securityguard(peopleId);
            if (ok) {
                String data = "";
                HashMap hashMap = securityOfficerMapper.safetyofficerList(peopleId);
                if (hashMap.size() > 0) {
                    String fileUrl = Config.examinerFileUrl + peopleId;
                    //上传照片到部平台
                    JSONObject imgJsonArr = httpUtil.Photo_formFileUpload(fileUrl, Config.nationwideImgUrl, "imageup",
                            "securityguardimg", KeyCreate.getbs() + ".jpg");
                    String imgErrorcode = imgJsonArr.getStr("errorcode");
                    String imgMessage = imgJsonArr.containsKey("message") ? imgJsonArr.getStr("message") : "";
                    String imgJsonData = imgJsonArr.containsKey("data") ? imgJsonArr.getStr("data") : "";
                    if (imgErrorcode.equals("0")) {
                        JSONObject imgData = JSONUtil.parseObj(imgJsonData);
                        String qgexamnum = imgData.getStr("secunum");
                        //上传照片到省平台
                        JSONObject provinceImgJsonArr = httpUtil.Photo_formFileUpload(fileUrl, Config.sjurl, "imageup", "examinerimg",
                                KeyCreate.getbs() + ".jpg");
                        String provinceImgErrorcode = provinceImgJsonArr.getStr("errorcode");
                        String provinceImgMessage = provinceImgJsonArr.containsKey("message") ? provinceImgJsonArr.getStr("message") : "";
                        String provinceImgJsonData = provinceImgJsonArr.containsKey("data") ? provinceImgJsonArr.getStr("data") : "";
                        if (provinceImgErrorcode.equals("0") && !provinceImgJsonData.isEmpty()) {
                            JSONObject provinceImgData = JSONUtil.parseObj(provinceImgJsonData);
                            String provincePhotoId = provinceImgData.getStr("id");
                            hashMap.put("photo", provincePhotoId);
                            hashMap.put("secunum", qgexamnum);
                            data = JSONUtil.toJsonStr(hashMap);
                            //上传安全到省平台
                            JSONObject provinceResult = httpUtil.sign_post(Config.sjurl, "securityguard", data);
                            String sjErrorcode = provinceResult.getStr("errorcode");
                            String sjMessage = provinceResult.containsKey("message") ? provinceResult.getStr("message") : "";
                            String sjJsonData = provinceResult.containsKey("data") ? provinceResult.getStr("data") : "";

                        } else {
                            return new Result("0", provinceImgMessage, peopleId, false);
                        }
                    } else {
                        return new Result("0", "不满足同步条件", peopleId, false);
                    }

                }
            } else {
                return new Result("0", "刷新数据失败", peopleId, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result("0","发生异常",peopleId,false);
        }
        return null;
    }


}
