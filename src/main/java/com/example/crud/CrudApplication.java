package com.example.crud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileNotFoundException;

@SpringBootApplication
@EnableScheduling//开启定时器
@MapperScan("com.example.crud.mapper")//扫描
@EnableTransactionManagement//开启事务
public class CrudApplication {

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(CrudApplication.class, args);
    }

}
