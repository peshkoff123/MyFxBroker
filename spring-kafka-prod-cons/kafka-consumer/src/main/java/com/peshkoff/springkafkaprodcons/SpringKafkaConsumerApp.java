package com.peshkoff.springkafkaprodcons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
/*@ComponentScan(basePackages = {
        "ru.xpendence.kafkaserver.config",
        "ru.xpendence.kafkaserver.controller",
        "ru.xpendence.kafkaserver.service",
})
@PropertySource({
        "classpath:kafka.properties"
})*/
@EnableKafka
public class SpringKafkaConsumerApp {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run( SpringKafkaConsumerApp.class, args);
        Object o =ctx.getBean( "kafkaListenerContainerFactory");
        System.out.println( "kafkaListenerContainerFactory:"+o);
    }
}
