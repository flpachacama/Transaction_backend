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
public class MovementResponseDTO {
    @Schema(example = "10")
    private Long id;

    @Schema(example = "478758")
    private String accountNumber;

    @Schema(example = "WITHDRAW")
    private String movementType;

    @Schema(example = "-575.00")
    private BigDecimal amount;

    @Schema(example = "1425.00")
    private BigDecimal balance;

    @Schema(example = "2026-04-29T17:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime movementDate;
}
