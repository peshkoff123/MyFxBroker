package com.peshkoff.springkafkaprodcons.prod;

import com.peshkoff.springkafkaprodcons.dto.Quote;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableScheduling
public class SpringKafkaProducerApp {

    private static KafkaProducer producer;
    private static QuoteGenerator quoteGenerator;

    public static void main( String[] args) {
        ApplicationContext ctx = SpringApplication.run( SpringKafkaProducerApp.class, args);
        producer = ctx.getBean( KafkaProducer.class);
        quoteGenerator = ctx.getBean( QuoteGenerator.class);
    }

    @Scheduled( initialDelay = 10000, fixedDelay = 5000)
    public static void sendNextQuote() {
        quoteGenerator.getInstruments().forEach( nextInstr ->
                producer.sendQuote( nextInstr, quoteGenerator.getNextQuote(nextInstr)) );
    }
}
