package com.my.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan( {"utility", "com.my.media"})
@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
public class MyMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMediaApplication.class, args);
    }

}
