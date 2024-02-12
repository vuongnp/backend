package com.vuongnp.payment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vuongnp.payment.dto.GetUserBalanceRequest;
import com.vuongnp.payment.dto.GetUserBalanceResponse;
import com.vuongnp.payment.service.UserService;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
@Timed(histogram = true)
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    
    @GetMapping("{id}/balance")
    public ResponseEntity<GetUserBalanceResponse> getUserBalance(@PathVariable Integer id) {
        final GetUserBalanceResponse registrationResponse = userService
            .getUserBalance(GetUserBalanceRequest.builder().userId(id).build());
        return ResponseEntity.status(HttpStatus.OK).body(registrationResponse);
    }
    
}
