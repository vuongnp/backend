package com.vuongnp.payment.service;

import org.springframework.stereotype.Service;

import com.vuongnp.payment.dto.CreateTransactionRequest;
import com.vuongnp.payment.dto.CreateTransactionResponse;
import com.vuongnp.payment.dto.GetUserTransactionRequest;
import com.vuongnp.payment.dto.GetUserTransactionResponse;

@Service
public interface TransactionService {
    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    GetUserTransactionResponse getUserTransactions(GetUserTransactionRequest request);
}
