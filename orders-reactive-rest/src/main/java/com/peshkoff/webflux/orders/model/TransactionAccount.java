package com.peshkoff.webflux.orders.model;

import lombok.Data;

@Data
public class TransactionAccount {
    public static enum Status {
        BEGIN, CLOSE, ROLLBACK, ERROR
    }
    private String transactionId;
    private String accountId;
    private Status status;
}
