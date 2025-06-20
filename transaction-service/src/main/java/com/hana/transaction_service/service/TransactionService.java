package com.hana.transaction_service.service;

import com.hana.transaction_service.config.WebClientConfig;
import com.hana.transaction_service.dto.AccountDTO;
import com.hana.transaction_service.entity.Transaction;
import com.hana.transaction_service.kafka.KafkaProducerService;
import com.hana.transaction_service.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService
{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WebClient webClient;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Transaction findTransactionBySenderAccountId(Long id)
    {
        return transactionRepository.findBySenderAccountId(id);
    }

    public Transaction findTransactionByReceiverAccountId(Long id)
    {
        return transactionRepository.findByReceiverAccountId(id);
    }
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction createTransaction(Transaction transaction)
    {
     transaction.setTimestamp(LocalDateTime.now());
     return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }


    public Transaction transfer(Long senderId,Long receiverId,Double amount,String description){
        /*
        * first i will search the sender's account by its senderId
        * then i will check if the sender has enough balance to send
        * if yes i will create transaction with type TRANSFER
        * else i will throw an exception
        * after that i will update the balance of the sender and
        * update the balance of the receiver
        * */
        Double senderBalance=  webClient.get()
                .uri("/{senderId}",senderId)
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block()
                .getBalance();

        if(senderBalance<amount)
        {
            throw new RuntimeException("Solde insuffisant");
        }
        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/update-balance/{id}")
                        .queryParam("amount", -amount)
                        .build(senderId))
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block();

        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/update-balance/{id}")
                        .queryParam("amount", amount)
                        .build(receiverId))
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block();

        Transaction t=new Transaction(senderId,receiverId,amount,"TRANSFER",LocalDateTime.now(),description);
        kafkaProducerService.sendTransactionNotification("Nouvelle transaction : " + description);

        return transactionRepository.save(t);
    }

    public Transaction deposit(Long accountId,Double amount,String description)
    {
        /*
        * First i will search the account by accountId
        * then i will update balance by adding the amount
        * create new transaction with type DEPOSIT
        * if account not found i will throw an exception
        *  */
        webClient.get()
            .uri(uriBuilder ->
                uriBuilder
                        .path("/{id}")
                        .build(accountId))
            .retrieve()
            .bodyToMono(AccountDTO.class)
            .block();

        webClient.put()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/update-balance/{id}")
                                .queryParam("amount",amount)
                                .build(accountId)
                )
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block();
        Transaction t=new Transaction(null,accountId,amount,"DEPOSIT",LocalDateTime.now(),description);
        kafkaProducerService.sendTransactionNotification("Nouvelle transaction : " + description);
        return transactionRepository.save(t);
    }

    public Transaction withdraw(Long accountId,Double amount,String description)
    {
        /*
         * First i will search the account by accountId
         * i will check the balance if it's higher than the amount
         * then i will update balance by adding the amount
         * create new transaction with type DEPOSIT
         * if it's less i will throw an exception
         *  */

        Double balance=webClient.get()
            .uri(uriBuilder ->
                    uriBuilder
                            .path("/{id}")
                            .build(accountId))
            .retrieve()
            .bodyToMono(AccountDTO.class)
            .block().getBalance();

        if(balance<amount){
            throw new RuntimeException("Solde insuffisant");
        }

        webClient.put()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/update-balance/{id}")
                                .queryParam("amount",(-amount))
                                .build(accountId)
                )
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .block();
        Transaction t=new Transaction(null,accountId,amount,"WITHDRAW",LocalDateTime.now(),description);
        kafkaProducerService.sendTransactionNotification("Nouvelle transaction : " + description);
        return transactionRepository.save(t);

    }


    public List<Transaction> history(Long id)
    {
        return transactionRepository.findBySenderAccountIdOrReceiverAccountId(id,id);
    }
}
