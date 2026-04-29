package com.transaction.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementRequestDTO {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String movementType; // DEPOSIT / WITHDRAW

    @NotNull
    @Positive
    private BigDecimal amount;
}
