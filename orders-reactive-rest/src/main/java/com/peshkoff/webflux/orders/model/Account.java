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
@Document( collection="Accounts")
public class Account {
    public enum Status {
        ACTIVE, SUSPENDED, CLOSED
    }
    public enum Currency {
        USD, EUR, POUND
    }
    @Id
    private String id;
    private String userId;
    private Status status = Status.ACTIVE;
    private Currency currency = Currency.USD;
    private double equity;
    private String name;
    private LocalDateTime update;
    private LocalDateTime create;
}
