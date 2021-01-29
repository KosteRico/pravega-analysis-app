package com.kosterico.pravegaapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PravegaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PravegaApplication.class, args);
    }

}
