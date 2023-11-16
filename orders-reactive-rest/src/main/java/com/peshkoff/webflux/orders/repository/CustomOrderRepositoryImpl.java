package com.peshkoff.webflux.orders.repository;

import com.peshkoff.webflux.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    //private ReactiveMongoTransactionManager
    //TwoPhaseCommit example
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Order> getListOpened(String orderId) {
        Query query = new Query( Criteria.where("id").is( orderId))
                .addCriteria( Criteria.where("status").is( Order.Status.OPEN));
        return reactiveMongoTemplate.find( query, Order.class);
    }
    public Flux<Order> getListClosed(String orderId) {
        Query query = new Query( Criteria.where("id").is( orderId))
                .addCriteria( Criteria.where("status").is( Order.Status.CLOSE));
        return reactiveMongoTemplate.find( query, Order.class);
    }
    @Override
    public Mono<Order> closeOrder( String orderId, double closePrice) {
        // anyway need to read from DB first
        // double p = (o.getType() == Order.Type.BUY) ? closePrice-o.getOpenPrice() : o.getOpenPrice()-closePrice;
        Query query = new Query( Criteria.where("id").is( orderId))
                   .addCriteria( Criteria.where("status").is( Order.Status.OPEN));
        Update update = new Update().set( "closePrice", closePrice)
                                    .set( "closeTime", LocalDateTime.now())
                                    .set( "status", Order.Status.CLOSE);
                                  //.set( "profit", p*o.getVolume());
        return reactiveMongoTemplate.findAndModify( query, update, Order.class);
    }
}
