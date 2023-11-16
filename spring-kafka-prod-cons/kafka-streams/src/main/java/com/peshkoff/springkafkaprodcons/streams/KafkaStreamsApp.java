package com.peshkoff.springkafkaprodcons.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KafkaStreamsApp {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run( KafkaStreamsApp.class, args);
        ctx.getBean( KafkaStreamsService.class).buildPipeline();
    }
}
