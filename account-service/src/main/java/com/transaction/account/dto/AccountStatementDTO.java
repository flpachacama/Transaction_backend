package com.transaction.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AccountStatementDTO {
    @Schema(example = "2026-04-29T17:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @Schema(example = "Jose Lema")
    private String clientName;

    @Schema(example = "478758")
    private String accountNumber;

    @Schema(example = "AHORRO")
    private String accountType;

    @Schema(example = "2000.00")
    private BigDecimal initialBalance;

    @Schema(example = "true")
    private Boolean status;

    @Schema(example = "-575.00")
    private BigDecimal movement;

    @Schema(example = "1425.00")
    private BigDecimal balance;
}
