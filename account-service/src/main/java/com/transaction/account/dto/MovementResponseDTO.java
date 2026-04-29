package com.transaction.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementResponseDTO {
    private Long id;
    private LocalDateTime date;
    private String movementType;
    private BigDecimal amount;
    private BigDecimal balance;
    private Long accountId;
}
