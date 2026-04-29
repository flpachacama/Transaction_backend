package com.transaction.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String accountType;

    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;

    @NotNull
    private Boolean status;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientName;
}
