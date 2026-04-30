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
public class AccountRequestDTO {

    @Schema(example = "joselema")
    @NotBlank(message = "no puede estar vacío")
    private String clientId;

    @Schema(example = "478758")
    @NotBlank(message = "no puede estar vacío")
    private String accountNumber;

    @Schema(example = "AHORRO", allowableValues = {"AHORRO", "CORRIENTE"})
    @NotBlank(message = "no puede estar vacío")
    @Pattern(regexp = "^(AHORRO|CORRIENTE)$", message = "accountType debe ser AHORRO o CORRIENTE")
    private String accountType;

    @Schema(example = "2000.00")
    @NotNull(message = "no puede ser nulo")
    @Positive(message = "debe ser mayor que cero")
    private BigDecimal initialBalance;

    @Schema(example = "true")
    @NotNull(message = "no puede ser nulo")
    private Boolean status;
}
