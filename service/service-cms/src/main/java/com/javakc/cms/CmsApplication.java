package com.javakc.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
@ComponentScan(basePackages = {"com.javakc"})
@EnableJpaAuditing
public class CmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class,args);
    }

}
