package com.my.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( {"utility", "com.my.media"})
public class MyMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMediaApplication.class, args);
    }

}
