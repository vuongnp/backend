package com.vuongnp.payment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vuongnp.payment.dto.CreateTransactionRequest;
import com.vuongnp.payment.dto.CreateTransactionResponse;
import com.vuongnp.payment.dto.GetUserBalanceResponse;
import com.vuongnp.payment.dto.GetUserTransactionRequest;
import com.vuongnp.payment.dto.GetUserTransactionResponse;
import com.vuongnp.payment.service.TransactionService;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@Timed(histogram = true)
@RequestMapping("/transactions")
public class TransactionController {
    private final Counter myCounter;
    private final TransactionService transactionService;

    public TransactionController(MeterRegistry registry, TransactionService transactionService){
        this.myCounter = Counter.builder("mu_custom_counter")
                                .description("A custom counter metric")
                                .register(registry);
        this.transactionService = transactionService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserTransactionResponse> getUserTransactions(@PathVariable Integer userId) {
        myCounter.increment();
        
        final GetUserTransactionResponse response = transactionService.getUserTransactions(
                                                    GetUserTransactionRequest.builder()
                                                    .userId(userId)
                                                    .build());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    

    @PostMapping
    public ResponseEntity<CreateTransactionResponse> createTransaction(
        @Valid @RequestBody CreateTransactionRequest registrationRequest) {
        final CreateTransactionResponse response = transactionService.createTransaction(registrationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
