package com.chainway.newjtbinterfaceservice.controller;

import com.chainway.newjtbinterfaceservice.config.HttpUtil;
import com.chainway.newjtbinterfaceservice.packer.Result;
import com.chainway.newjtbinterfaceservice.service.SecurityOfficerRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("newJtbInterfaceService")
public class NewJtbInterfaceController {
    @Resource
    SecurityOfficerRecord securityOfficerRecord;

    @Resource
    HttpUtil httpUtil;

    @RequestMapping("/AqyDataTbServiceInterface")
    public String SecurityOfficerUpload(@RequestParam("peopleIds") String peopleIds) {
        List<Result> resultList = new ArrayList<Result>();
        StringBuffer resultJson = new StringBuffer();
        if (peopleIds==null || peopleIds=="") return new Result("0", "传入的安全员编号为空!",peopleIds, false).toString();
        if(peopleIds.contains(",")){
            String[] idArr = peopleIds.split(",");
            for (String id : idArr) {
                resultList.add(securityOfficerRecord.SecurityOfficerRecordMethod(id));
            }
        }else {
            resultList.add(securityOfficerRecord.SecurityOfficerRecordMethod(peopleIds));
        }

        //构造返回数据
        if(resultList.size() == 1){
            String str = resultList.get(0).toString();
            str = httpUtil.jsonToString(str);
            return str;
        }else{
            for(int i = 0;i<resultList.size();i++){
                resultJson.append(resultList.get(i).toString()).append(",");
            }
            String str = resultJson.toString();
            resultJson.setLength(0);
            str = str.substring(0, str.length()-1);
            str = httpUtil.jsonToString(str);
            return str;
        }

    }

}
