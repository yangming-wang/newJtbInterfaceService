package com.chainway.newjtbinterfaceservice.mapper;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;



@Mapper
public interface SecurityOfficerMapper {
    
    boolean Securityguard(@Param("peopleId") String peopleId);



    HashMap safetyofficerList(@Param("peopleId") String peopleId);
}
