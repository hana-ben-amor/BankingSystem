package com.hana.account_service.controller;

import com.hana.account_service.entity.Account;
import com.hana.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping
    public ResponseEntity<List<Account>> getAll()
    {
        return ResponseEntity.ok(accountService.getAll());
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account)
    {
        return ResponseEntity.ok(accountService.create(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id)
    {
        return accountService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        /*
        * Optional<Account> optAcc=accountService.getById(id)
        * if (optAcc.isPresent()){
        *   return ResponseEntity.ok(optAcc.get());
        * }
        * else{
        *   return ResponseEntity.notFound().build();
        * }
        * */
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Account> update(@PathVariable Long id, @RequestBody Account updatedAcc)
    {
        return ResponseEntity.ok(accountService.update(id,updatedAcc));
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Account> lock(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.lock(id));
    }
    @PutMapping("/unlock/{id}")
    public ResponseEntity<Account> unlock(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.unlock(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> delete(@PathVariable Long id){
        return ResponseEntity.ok(accountService.delete(id));
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Account>> getByClientId(@PathVariable Long clientId)
    {
        return ResponseEntity.ok(accountService.getByClientId(clientId));
    }

    @PutMapping("/update-balance/{id}")
    public ResponseEntity<Account> updateBalance(@PathVariable Long id ,@RequestParam Double amount)
    {
     return ResponseEntity.ok(accountService.updateBalance(id,amount));
    }
    }
