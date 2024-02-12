package com.vuongnp.payment.service;

import org.springframework.stereotype.Service;

import com.vuongnp.payment.dto.GetUserBalanceRequest;
import com.vuongnp.payment.dto.GetUserBalanceResponse;
import com.vuongnp.payment.exception.UserNotFoundException;
import com.vuongnp.payment.model.User;
import com.vuongnp.payment.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceIplm implements UserService{
    private final UserRepository userRepository;

    @Override
    public GetUserBalanceResponse getUserBalance(GetUserBalanceRequest request){
        final User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        return GetUserBalanceResponse.builder().balance(user.getBalance()).build();
    }
}
