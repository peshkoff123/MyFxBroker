package com.peshkoff.webflux.orders.controller;

import com.peshkoff.webflux.orders.model.Account;
import com.peshkoff.webflux.orders.model.Order;
import com.peshkoff.webflux.orders.servise.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping( "/order")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public Flux<Order> getList(@RequestParam String accountId) {
        return orderService.getList( accountId);
    }
    @GetMapping( "/open")
    public Mono<Order> openOrder(@RequestParam String accountId,
                                 @RequestParam String instrument,
                                 @RequestParam double volume,
                                 @RequestParam double openPrice) {
        return orderService.openOrder( accountId, instrument, volume, openPrice);
    }
    @GetMapping( "/close")
    public Mono<Order> closeOrder(@RequestParam  String orderId,
                                  @RequestParam double closePrice) {
        return orderService.closeOrder( orderId, closePrice);
    }
}
/*
//getList
localhost:8080/order?accountId=accountId_1
//openOrder
localhost:8080/order/open?accountId=accountId_1&instrument=EURUSD&volume=100&openPrice=1.05564
localhost:8080/order/open?accountId=accountId_1&instrument=USDJPY&volume=1&openPrice=150.105
//closeOrder
localhost:8080/order/close?orderId=6544aa38a1c3f0178335466b&closePrice=1.3556
localhost:8080/order/close?orderId=6543b08eed372b66256c7036&closePrice=145.105

// ToDo
//getEquity
localhost:8080/order/equity?accountId=accountId_1

    private static final String[] INSTRUMENTS = { "EURUSD", "GBPUSD", "USDJPY", "USDCAD", "USDCHF"};
    private static final double[] START_PRICE = {  1.05564,  1.21306,  150.105,  1.38025,  0.94640};
* */
