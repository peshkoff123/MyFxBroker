package com.peshkoff.webflux.orders.controller;

import com.peshkoff.webflux.orders.model.Account;
import com.peshkoff.webflux.orders.model.TransactionAccount;
import com.peshkoff.webflux.orders.servise.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetAddress;

@RestController
@RequestMapping( "/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public Flux<Account> getUserAccounts( @RequestParam(required = false) String userId) {
        if( userId == null) {
            return accountService.getAllUserAccounts();
        }
        return accountService.getUserAccounts( userId);
    }
    @PostMapping
    public Mono<Account> createAccount(@RequestBody Account acc) {
        return accountService.createAccount( acc);
    }
    @PutMapping("/status")
    public Mono<Account> changeAccountStatus( @RequestParam String accId, @RequestParam String accStatus) {
        return accountService.changeAccountStatus( accId, accStatus);
    }
    @PutMapping
    public Mono<Account> changeAccount( @RequestParam String accId,
                                        @RequestParam Double equity, @RequestParam String accName) {
        return accountService.changeAccount( accId, equity, accName);
    }
    @DeleteMapping
    public Mono<Void> deleteAccount( @RequestParam String accId) {
        return accountService.deleteAccount( accId);
    }

    @GetMapping( "/profit")
    public Mono<Account> addProfit( @RequestParam String accId,
                                    @RequestParam Double profit) {
        return accountService.addAccountProfit( accId, profit);
    }
    @GetMapping("/hb")
    public String getHeartBeat() {
        String res = "";
        try { InetAddress inetAddress = InetAddress.getLocalHost();
              res = inetAddress.toString();
              System.out.format( "getHeartBeat() " + res);
        } catch (Exception ex) { ex.printStackTrace(); }
        return res;
    };

    /* Todo : implement TwoPhaseCommit
//addProfit_Transaction
localhost:8080/account/prof_begin?userId=UserID2&profit=10
//addProfit_Commit
localhost:8080/account/prof_com?transactionId=transactionId
//addProfit_Rollback
localhost:8080/account/prof_rol?transactionId=transactionId
    @GetMapping( "/prof_begin")
    public Mono<TransactionAccount> addProfit_Transaction(@RequestParam String accId,
                                                          @RequestParam Double profit) {
        return Mono.just( new TransactionAccount());
    }
    @GetMapping( "/prof_com")
    public Mono<TransactionAccount> addProfit_Commit( @RequestParam String transactionId) {
        return Mono.just( new TransactionAccount());
    }
    @GetMapping( "/prof_rol")
    public Mono<TransactionAccount> addProfit_Rollback( @RequestParam String transactionId) {
        return Mono.just( new TransactionAccount());
    }
*/
}

// docker run --name myorders -p 8080:8080 -p 27017:27017 -e "mongoConnect=mongodb://192.168.56.1:27017" orders:0.0.1-SNAPSHOT

// docker run -p <host_port>:<container_port>
// docker run -d -p 27017:27017 --name mymongo mongo:3.6
// docker run -d -p 27017:27017 -v "$(pwd)"/mongo-docker:/data/db --name mymongo mongo:3.6
// curl 192.168.99.100:27017
/*
//heartBeat
localhost:8080/account/hb
//list
localhost:8080/account
localhost:8080/account?userId=UserID2
//createAccount
fetch('/account', { method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userId: 'UserID', name: 'BlaBla', equity:100})
                  }).then( result => result.json().then( console.log))
//changeAccountStatus
fetch('/account/status?accId=65434b3f531e910753e562ba&accStatus=CLOSED',
                  { method: 'PUT'}).then( result => result.json().then( console.log))
//changeAccount
fetch('/account?accId=65434b3f531e910753e562ba&equity=100101&accName=newAccName',
                  { method: 'PUT'}).then( result => result.json().then( console.log))
//addProfit
localhost:8080/account/profit?accId=accId&profit=10
//deleteAccount
fetch('/account?accId=654350caf1ab5d69a3b02427',
                  { method: 'DELETE'}).then( console.log)
*/
