package com.transaction.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @NotNull
    @Min(0)
    private Integer age;

    @NotBlank
    private String identification;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @NotBlank
    private String clientId;

    @NotBlank
    private String password;

    @NotNull
    private Boolean status;
}
