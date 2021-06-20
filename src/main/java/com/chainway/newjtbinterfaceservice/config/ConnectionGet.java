package com.chainway.newjtbinterfaceservice.config;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * TODO:获取执行存储过程的Connection连接
 */
public class ConnectionGet {

    private Connection connection;

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String passWord;

    public Connection getConnection() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, userName, passWord);
            if (connection != null) System.out.println("获取connection连接成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}
