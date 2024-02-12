package com.vuongnp.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserTransactionRequest {
    @Positive
    private int userId;
}
