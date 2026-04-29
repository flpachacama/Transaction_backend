package com.transaction.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Schema(example = "478758")
    @NotBlank(message = "no puede estar vacío")
    private String accountNumber;

    @Schema(example = "DEPOSIT", allowableValues = {"DEPOSIT", "WITHDRAW"})
    @NotBlank(message = "no puede estar vacío")
    @Pattern(regexp = "^(DEPOSIT|WITHDRAW)$", message = "movementType debe ser DEPOSIT o WITHDRAW")
    private String movementType;

    @Schema(example = "150.00")
    @NotNull(message = "no puede ser nulo")
    @Positive(message = "debe ser mayor que cero")
    private BigDecimal amount;
}
