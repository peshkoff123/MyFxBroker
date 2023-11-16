package com.peshkoff.webflux.orders;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@SpringBootApplication
@OpenAPIDefinition( info = @Info( title="OrdersService (WebFlux API)", version = "1.0"))
public class OrdersApplication {
	//spring.data.mongodb.database: Peshkoff
	//spring.data.mongodb.host: 192.168.99.100
	//spring.data.mongodb.port
	@Value("${spring.data.mongodb.host}")
	private String mongoHost;
	@Value("${spring.data.mongodb.port}")
	private String mongoPort;
	@Value("${spring.data.mongodb.database}")
	private String mongoDB;

	@Bean
	MongoClient mongoClient() {
		String mongoCon = mongoConnect != null ? mongoConnect : "mongodb://"+mongoHost+":"+mongoPort;
		System.out.format("mongoCon = " + mongoCon);
		return MongoClients.create( mongoCon);//"mongodb://localhost:27017"
	}
	@Bean
	ReactiveMongoOperations reactiveMongoTemplate(MongoClient mongoClient) {
		return new ReactiveMongoTemplate(mongoClient, mongoDB);//  "Peshkoff"
	}
    private static String mongoConnect = null;

	public static void main(String[] args) {
		//Map<String, String> env = System.getenv();
		//env.forEach( (key, val)-> System.out.format("%s=%s%n", key,	val));
		//	 System.out.format("%s=%s%n", envName,	env.get(envName));
		mongoConnect = System.getenv("mongoConnect");
		System.out.format("!!!  mongoConnect = " + mongoConnect);

		SpringApplication.run(OrdersApplication.class, args);
	}
/*
Simplified and incomplete backend for forex brocker company.
Its just a training project - will never be finished!

Projects goals:
 - practical work with infrastructure/frameworks:
    - Kafka
    - AWS
    - Reactive programming: Reactor, SpringReactive
    - MongoDB: reactive and nonReactive drivers
 - this is a training project hence business logic and dataModels are simplified considerably

The DesignDocument:
https://app.diagrams.net/?src=about#Hpeshkoff123%2FMyFxBroker%2Fmain%2FmyBroker.drawio
* */
}
