package com.vuongnp.payment.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.vuongnp.payment.dto.CreateTransactionRequest;
import com.vuongnp.payment.dto.CreateTransactionResponse;
import com.vuongnp.payment.dto.GetUserTransactionRequest;
import com.vuongnp.payment.dto.GetUserTransactionResponse;
import com.vuongnp.payment.exception.DuplicateIdempotentKeyException;
import com.vuongnp.payment.exception.NotEnoughBalanceException;
import com.vuongnp.payment.exception.TooManyRequestException;
import com.vuongnp.payment.exception.UserNotFoundException;
import com.vuongnp.payment.model.Transaction;
import com.vuongnp.payment.model.User;
import com.vuongnp.payment.repository.TransactionRepository;
import com.vuongnp.payment.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceIplm implements TransactionService{
    private final DistributedLockService distributedLockService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request){
        CreateTransactionResponse createTransactionResponse;
        if (distributedLockService.accquireLock(request.getIdempotentKey())){
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.info("accquire lock successfully");
            try{
                Optional<Transaction> optionalExistedTransaction = transactionRepository
                    .findOneByIdempotentKey(request.getIdempotentKey());
                if(optionalExistedTransaction.isPresent()){
                    Transaction existedTransaction = optionalExistedTransaction.get();
                    if(existedTransaction.getUserId() == request.getUserId()
                        && existedTransaction.getAmount() == request.getAmount()){
                            createTransactionResponse = CreateTransactionResponse.builder()
                                                        .transactionId(existedTransaction.getId())
                                                        .remainBalance(existedTransaction.getBalanceAfterTransaction())
                                                        .build();
                    }else{
                        throw new DuplicateIdempotentKeyException();
                    }
                }else{
                    final User user = userRepository.findOneWithLockingById(request.getUserId())
                                                    .orElseThrow(UserNotFoundException::new);
                    if(user.getBalance() < request.getAmount()){
                        throw new NotEnoughBalanceException();
                    }
                    int balanceBeforeTransaction = user.getBalance();
                    int balanceAfterTransaction = balanceBeforeTransaction - request.getAmount();
                    user.setBalance(balanceAfterTransaction);
                    userRepository.save(user);
    
                    Transaction transaction = transactionRepository.save(Transaction.builder()
                                                                .idempotentKey(request.getIdempotentKey())
                                                                .amount(request.getAmount())
                                                                .balanceBeforeTransaction(balanceBeforeTransaction)
                                                                .balanceAfterTransaction(balanceAfterTransaction)
                                                                .userId(request.getUserId())
                                                                .build());
                    createTransactionResponse = CreateTransactionResponse.builder()
                                                                         .transactionId(transaction.getId())
                                                                         .remainBalance(balanceAfterTransaction)
                                                                         .build();
    
                }
            }finally{
                distributedLockService.releaseLock(request.getIdempotentKey());
                log.info("release lock successfully");
            }
        }else{
            log.info("cannot accquired lock");
            throw new TooManyRequestException();
        }
        return createTransactionResponse;
    }

    @Override
    public GetUserTransactionResponse getUserTransactions(GetUserTransactionRequest request){
        return GetUserTransactionResponse.builder()
                                                                .transactions(transactionRepository.findByUserId(request.getUserId()))
                                                                .build();
    }
}
