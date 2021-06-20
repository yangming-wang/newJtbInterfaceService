package com.chainway.newjtbinterfaceservice.packer;

import lombok.Data;

@Data
public class Result {
    private String code;
    private int size;
    private String message="";
    private String id;
    private Object data="";
    private boolean flag;

    public Result(String key, String values,String id, boolean flag) {
        super();
        this.code = key;
        this.message = values;
        this.id = id;
        this.flag = flag;
    }

    public Result(String code,int size, String message, Object data) {
        super();
        this.code = code;
        this.size = size;
        this.message = message;
        this.data = data;
    }
    public Result(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    public Result(String code,String message,Object data){
        super();
        this.code = code;
        this.message = message;
        this.data=data;
    }
}
