package com.hana.transaction_service.repository;

import com.hana.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findBySenderAccountId(Long id);
    Transaction findByReceiverAccountId(Long id);

    List<Transaction> findBySenderAccountIdOrReceiverAccountId(Long senderId, Long receiverId);

}
