package com.chainway.newjtbinterfaceservice.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LearnTimeMapper {
    boolean LearnTimefunction(@Param("studyLocusId") String studyLocusId);
}
