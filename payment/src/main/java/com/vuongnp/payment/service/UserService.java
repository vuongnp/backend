package com.vuongnp.payment.service;

import org.springframework.stereotype.Service;

import com.vuongnp.payment.dto.GetUserBalanceRequest;
import com.vuongnp.payment.dto.GetUserBalanceResponse;

@Service
public interface UserService {
    GetUserBalanceResponse getUserBalance(GetUserBalanceRequest request);
}
