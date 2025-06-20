package com.hana.account_service.service;

import com.hana.account_service.entity.Account;
import com.hana.account_service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private  AccountRepository accountRepository;
    public Account create(Account account){
        Double balance = account.getBalance()!= null ? account.getBalance():0.0;
        account.setBalance(balance);
       return accountRepository.save(account);
    }


    public Optional<Account> getById(Long id)
    {
        return accountRepository.findById(id);
    }

    public Account update(Long id, Account updatedAcc)
    {
        Account existing=accountRepository.findById(id).orElseThrow();
        existing.setAccountType(updatedAcc.getAccountType());
        existing.setBalance(updatedAcc.getBalance());
        accountRepository.save(existing);
        return (existing);
    }

    public Account lock(Long id){
        Account existing=accountRepository.findById(id).orElseThrow();
        existing.setLocked(true);
        return existing;
    }
    public Account unlock(Long id){
        Account existing=accountRepository.findById(id).orElseThrow();
        existing.setLocked(false);
        return existing;
    }

    public Account delete(Long id)
    {
        Account existing=accountRepository.findById(id).orElseThrow();
        accountRepository.delete(existing);
        return existing;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }
    public List<Account> getByClientId(Long clientId)
    {
        return accountRepository.findByClientId(clientId);
    }

    public Account updateBalance(Long id, Double amount) {
        Account existing=accountRepository.findById(id).orElseThrow();
        Double newBalance = existing.getBalance() + amount;
        existing.setBalance(newBalance);
        return accountRepository.save(existing);
    }
}
