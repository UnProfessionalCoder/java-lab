package com.newbig.app.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.newbig.app")
@Import({
    CacheConfig.class})
public class AppConfig {

}
