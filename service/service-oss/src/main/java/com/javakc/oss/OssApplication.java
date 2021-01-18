package com.javakc.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.javakc"})
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
    }

}
