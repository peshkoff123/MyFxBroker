package com.peshkoff.webflux.orders.servise;

import com.peshkoff.webflux.orders.model.Account;
import com.peshkoff.webflux.orders.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Flux<Account> getAllUserAccounts() {
        return accountRepository.findAll();
    }
    public Flux<Account> getUserAccounts( String userId) {
        return accountRepository.findByUserId( userId);
    }
    public Mono<Account> createAccount( Account acc) {
        if( acc == null) {
            acc = new Account();
            acc.setUserId("UserID");
        }
        acc.setCreate( LocalDateTime.now());
        acc.setUpdate( acc.getCreate());
        return accountRepository.insert( acc);
    }
    public Mono<Account> changeAccountStatus( String accId, String accStatus) {
        Account.Status newStatus = Account.Status.valueOf( accStatus);
        Mono<Account> accMono = accountRepository.findById( accId);
        return accMono.flatMap( acc-> { acc.setStatus( newStatus);
                                        acc.setUpdate( LocalDateTime.now());
                                        return accountRepository.save( acc);});
    }
    public Mono<Account> addAccountProfit( String accId, Double equity) {
        return accountRepository.findById( accId).
                flatMap( acc -> { acc.setEquity( acc.getEquity() + equity);
                                  acc.setUpdate( LocalDateTime.now());
                                  return accountRepository.save( acc); });
    }
    public Mono<Account> changeAccount( String accId, Double equity, String accName) {
       return accountRepository.findById( accId).
                flatMap( acc -> { acc.setEquity( equity);
                                  acc.setName( accName);
                                  acc.setUpdate( LocalDateTime.now());
                                  return accountRepository.save( acc); });
    }
    public Mono<Void> deleteAccount( String accId) {
        return accountRepository.deleteById( accId);
    }
}
