package com.transaction.customer.service;

import com.transaction.customer.dto.LoginRequestDTO;
import com.transaction.customer.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO requestDTO);
}
