package com.vuongnp.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.vuongnp.payment.model.User;
import com.vuongnp.payment.repository.UserRepository;
import com.vuongnp.payment.service.TransactionService;
import com.vuongnp.payment.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PaymentApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PaymentApplication.class, args);

		UserRepository userRepository = context.getBean(UserRepository.class);
		UserService userService = context.getBean(UserService.class);
		// TransactionService transactionService = context.getBean(TransactionService.class);

		User user1 = userRepository.save(User.builder().balance(100).build());
	}

}
