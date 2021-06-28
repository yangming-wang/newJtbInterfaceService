package com.chainway.newjtbinterfaceservice.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface LearnTimeMapper {

    /**
     * @param studyLocusId 
     * @return
     */
    boolean LearnTimefunction(@Param("studyLocusId") String studyLocusId);

    HashMap LearnTimeget(@Param("studyLocusId") String studyLocusId);
}
