package com.chainway.newjtbinterfaceservice.service.impl;

import com.chainway.newjtbinterfaceservice.mapper.LearnTimeMapper;
import com.chainway.newjtbinterfaceservice.packer.Result;
import com.chainway.newjtbinterfaceservice.service.LearnTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@Service
public class LearnTimeImpl implements LearnTime {

    @Resource
    LearnTimeMapper learnTimeMapper;

    @Override
    public Result LearnTimeRecord(String studyLocusId) {
        try {
            boolean ok=learnTimeMapper.LearnTimefunction(studyLocusId);
            if (ok){
                String data="";
                HashMap hashMap=learnTimeMapper.LearnTimeget(studyLocusId);
                if ( hashMap.size()>0){
                    Integer subject=Integer.parseInt(hashMap.get("subject").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
