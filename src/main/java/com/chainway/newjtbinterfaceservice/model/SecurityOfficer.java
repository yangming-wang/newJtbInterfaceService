package com.chainway.newjtbinterfaceservice.model;


import lombok.Data;

import java.util.Date;

@Data
public class SecurityOfficer {

    private String peopleid;
    ;

    private int delflag;

    private String gjstate;

    private String countryissend;

    private Date countrysendtime;

    private int provinceissend;

    private Date provincesendtime;

    private int cwisget;

    private Date cwgettime;

    private String secunum;

    private String inscode;

    private String name;

    private int sex;

    private String idcard;

    private String mobile;

    private String address;

    private String photo_province;

    private int fingerprint;

    private String drilicence;

    private String fstdrilicdate;

    private String dripermitted;

    private String teachpermitted;

    private String employstatus;

    private String hiredate;

    private String leavedate;


}
