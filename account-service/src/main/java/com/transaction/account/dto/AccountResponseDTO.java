package com.transaction.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class AccountResponseDTO {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "joselema")
    private String clientId;

    @Schema(example = "478758")
    private String accountNumber;

    @Schema(example = "AHORRO")
    private String accountType;

    @Schema(example = "2000.00")
    private BigDecimal initialBalance;

    @Schema(example = "1425.00")
    private BigDecimal currentBalance;

    @Schema(example = "true")
    private Boolean status;

    @Schema(example = "2026-04-29T17:00:00")
    private LocalDateTime createdAt;
}
