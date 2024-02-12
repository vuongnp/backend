package com.vuongnp.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuongnp.payment.model.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
    List<Transaction> findByUserId(int userId);

    Optional<Transaction> findOneByIdempotentKey(String idempotentKey);
    
}
