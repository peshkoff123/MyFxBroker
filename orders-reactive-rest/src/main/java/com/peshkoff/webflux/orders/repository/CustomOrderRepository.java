package com.peshkoff.webflux.orders.repository;

import com.peshkoff.webflux.orders.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CustomOrderRepository {

    Flux<Order> getListOpened(String orderId);
    Flux<Order> getListClosed(String orderId);
    Mono<Order> closeOrder(String orderId, double closePrice/*, LocalDateTime closeTime, */);
}
