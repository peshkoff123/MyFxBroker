package com.peshkoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaRestApp {
    public static void main(String[] args) {
        SpringApplication.run( KafkaRestApp.class, args);
    }
}