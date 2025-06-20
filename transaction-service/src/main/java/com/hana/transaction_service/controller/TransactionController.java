package com.hana.transaction_service.controller;

import com.hana.transaction_service.entity.Transaction;
import com.hana.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @GetMapping("/sender/{id}")
    public ResponseEntity<Transaction> findTransactionBySenderAccountId(@PathVariable Long id)
    {
        return ResponseEntity.ok(transactionService.findTransactionBySenderAccountId(id));
    }
    @GetMapping("/receiver/{id}")
    public ResponseEntity<Transaction> findTransactionByReceiverAccountId(@PathVariable Long id)
    {
        return ResponseEntity.ok(transactionService.findTransactionByReceiverAccountId(id));
    }
    @GetMapping
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }
    @GetMapping("/{id}")
    public Optional<Transaction> getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction)
    {
        return transactionService.createTransaction(transaction);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }

    @PostMapping("/transfer/{senderId}/{receiverId}")
    public ResponseEntity<Transaction> transfer(@PathVariable Long senderId,@PathVariable Long receiverId,@RequestParam Double amount)
    {
        return ResponseEntity.ok(transactionService.transfer(senderId,receiverId,amount,"Transfer from account :"+senderId+" to account : "+receiverId));
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<Transaction> deposit(
            @PathVariable Long accountId,
            @RequestParam Double amount,
            @RequestParam(required = false, defaultValue = "Deposit made by client") String description
    ) {
        Transaction t = transactionService.deposit(accountId, amount,"DEPOSIT on account: "+ accountId+" amount: "+amount+ "on: "+LocalDateTime.now());
        return ResponseEntity.ok(t);
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<Transaction> withdraw(
            @PathVariable Long accountId,
            @RequestParam Double amount,
            @RequestParam(required = false, defaultValue = "Withdrawal by client") String description
    ) {
        Transaction t = transactionService.withdraw(accountId, amount, "WITHDRAW on account: "+ accountId+" amount: "+amount+ "on: "+LocalDateTime.now());
        return ResponseEntity.ok(t);
    }


    @GetMapping("/history/{id}")
    public ResponseEntity<List<Transaction>> history(@PathVariable Long id)
    {
        return ResponseEntity.ok(transactionService.history(id));
    }

}
