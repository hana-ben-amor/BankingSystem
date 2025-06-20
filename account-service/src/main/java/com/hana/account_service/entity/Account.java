package com.hana.account_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountType; // courant, epargne, entreprise

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private Long clientId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Account() {
    }

    public Account(Long id, String accountType, Double balance, Boolean locked, LocalDateTime createdAt,Long clientId) {
        this.id = id;
        this.accountType = accountType;
        this.balance = balance;
        this.locked = locked;
        this.createdAt = createdAt;
        this.clientId=clientId;
    }

    public Account(String accountType, Double balance, Boolean locked, LocalDateTime createdAt,Long clientId) {
        this.accountType = accountType;
        this.balance = balance;
        this.locked = locked;
        this.createdAt = createdAt;
        this.clientId=clientId;

    }
}