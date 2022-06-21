package com.mt;

import com.mt.utils.CreateTableUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.mt.mapper")
@EnableSwagger2
@EnableScheduling
public class MybatisPlusApplication {

    public static void main(String[] args) throws Exception {
        CreateTableUtil.CreateTableUtil();
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
