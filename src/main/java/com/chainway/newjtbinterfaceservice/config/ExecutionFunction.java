package com.chainway.newjtbinterfaceservice.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class ExecutionFunction {

    private static Connection connection;


    // 执行 变更新增 函数
    public static int ExecuteStoredProcedure(String functionName, String key, String value) {
        connection = new ConnectionGet().getConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("{" + functionName + "(" + key + "," + value + ")}");
            callableStatement.setString(1, key);
            callableStatement.setString(2, value);
            callableStatement.execute();
            return callableStatement.getInt(2);
        } catch (Exception e) {
            System.out.println("执行函数出现异常");
            e.printStackTrace();
        }
        return 0;
    }

}
