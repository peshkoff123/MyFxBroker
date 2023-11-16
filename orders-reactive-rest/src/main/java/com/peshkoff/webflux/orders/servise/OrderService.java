package com.peshkoff.webflux.orders.servise;

import com.peshkoff.webflux.orders.model.Account;
import com.peshkoff.webflux.orders.model.Order;
import com.peshkoff.webflux.orders.model.TransactionAccount;
import com.peshkoff.webflux.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public Flux<Order> getList( String accountId) {
        return orderRepository.findByAccountId( accountId);
    }
    public Mono<Order> openOrder( String accountId, String instrument, double volume, double openPrice) {
        Order o = new Order();
        o.setAccountId( accountId);
        o.setInstrument( instrument);
        o.setVolume( volume);
        o.setOpenPrice( openPrice);
        o.setOpenTime( LocalDateTime.now());
        return orderRepository.insert( o);
    }
    public Mono<Order> closeOrder( String orderId, double closePrice) {
        //TransactionAccount tAcc = updAccount_TrBegin( orderId, closePrice);
        return orderRepository.findById( orderId).flatMap( o -> {
                 o.setClosePrice( closePrice);
                 o.setCloseTime( LocalDateTime.now());
                 o.setStatus( Order.Status.CLOSE);
                 double p = (o.getType() == Order.Type.BUY) ? closePrice-o.getOpenPrice() : o.getOpenPrice()-closePrice;
                 o.setProfit( p*o.getVolume());
                 update_Remote_Account_Service( o.getAccountId(), o.getProfit());
                 return orderRepository.save( o);                });
    }

    private ExecutorService executorService =  Executors.newSingleThreadExecutor();
    private WebClient webClient = WebClient.create( "http://localhost:8080");
    public void update_Remote_Account_Service(String accountId, double profit) {
        executorService.execute( () -> {
            Account acc = webClient.get()
                    .uri( "/account/profit?accId="+accountId+"&profit="+profit)
                    .retrieve()
                    .bodyToFlux( Account.class).blockLast();
            System.out.println( acc);
        });
    }

}
