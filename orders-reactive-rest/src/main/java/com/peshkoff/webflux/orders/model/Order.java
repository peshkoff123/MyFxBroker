package com.peshkoff.webflux.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Order {
    public static enum Status {
        OPEN, CLOSE
    }
    public static enum Type {
        BUY, SELL
    }
    @Id
    private String id;
    private String accountId;
    private String instrument;
    private Type type = Type.BUY;
    private Order.Status status = Status.OPEN;
    private double volume;
    private double openPrice;
    private LocalDateTime openTime;
    private double closePrice;
    private LocalDateTime closeTime;
    private double profit;
}
