package com.vuongnp.payment.dto;

import java.util.List;

import com.vuongnp.payment.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserTransactionResponse {
    private List<Transaction> transactions;
}
