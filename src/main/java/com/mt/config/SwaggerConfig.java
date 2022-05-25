package com.mt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerDemoApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mt.controller"))
                .build();
    }

    private ApiInfo swaggerDemoApiInfo() {
        return new ApiInfoBuilder()
                .title("光伏系统 API")
                .build();
    }



}

