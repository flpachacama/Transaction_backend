package com.transaction.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private Boolean status;
    private String clientId;
    private String clientName;
}
