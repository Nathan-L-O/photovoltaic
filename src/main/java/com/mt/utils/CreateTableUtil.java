package com.mt.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateTableUtil {
    public static void CreateTableUtil() throws Exception
    {
        Class.forName("com.mysql.cj.jdbc.Driver");//加载注册

        //一开始必须填一个已经存在的数据库
        String url = "jdbc:mysql://192.168.3.8:3306/mysql?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
        Connection conn = DriverManager.getConnection(url, "root", "123456");//建立连接
//        Connection conn = DriverManager.getConnection(url, "root", "1qaz2wsx");//建立连接
        Statement stat = conn.createStatement();

        //创建数据库hello
        //  stat.executeUpdate("create database hello");

        String checkdatabase="show databases like \"photovoltaic\"";//判断数据库是否存在
        String  createdatabase="create  database  photovoltaic";//创建数据库

        stat = (Statement) conn.createStatement();

        ResultSet resultSet=stat.executeQuery(checkdatabase);
        if (resultSet.next()) {//若数据库存在
            System.out.println("hello exist!");


        }else{
            if(stat.executeUpdate(createdatabase)==0){
                //若数据库不存在
            }
            // 打开创建的数据库
            stat.close();
            conn.close();
            url = "jdbc:mysql://localhost:3306/photovoltaic?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
            conn = DriverManager.getConnection(url, "root", "123456");
//            conn = DriverManager.getConnection(url, "root", "1qaz2wsx");
            stat = conn.createStatement();

            ClassPathResource rc = new ClassPathResource("sql/photovoltaic.sql");
            EncodedResource er = new EncodedResource(rc, "utf-8");
            ScriptUtils.executeSqlScript(conn, er);

            stat.close();
            conn.close();
            System.out.println("create table success!");
        }

    }

}
