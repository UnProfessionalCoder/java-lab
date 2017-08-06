package com.newbig.app.web;

import com.newbig.app.web.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.newbig.app.web.mapper")
@Slf4j
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppConfig.class);
        app.run(AppApplication.class, args);
        log.info("--------------------------------");

    }
}
