package com.peshkoff.webflux.orders.repository;

import com.peshkoff.webflux.orders.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String>, CustomOrderRepository {
    Flux<Order> findByAccountId( String accountId);
}
