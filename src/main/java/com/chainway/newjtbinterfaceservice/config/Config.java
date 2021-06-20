package com.chainway.newjtbinterfaceservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${photo.examinerFileUrl}")
    public static String examinerFileUrl;

    @Value("${ministryStandard.nationwideImgUrl}")
    public static String nationwideImgUrl;

    @Value("${provincialBureau.sjurl}")
    public static String sjurl;

}
