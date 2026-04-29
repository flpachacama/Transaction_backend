package com.transaction.customer.service.impl;

import com.transaction.customer.dto.LoginRequestDTO;
import com.transaction.customer.dto.LoginResponseDTO;
import com.transaction.customer.entity.Customer;
import com.transaction.customer.exception.BusinessException;
import com.transaction.customer.repository.CustomerRepository;
import com.transaction.customer.security.JwtTokenService;
import com.transaction.customer.service.AuthService;
import com.transaction.customer.util.CustomerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Customer customer = customerRepository.findByClientIdAndStatusTrue(requestDTO.getClientId())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!passwordEncoder.matches(requestDTO.getPassword(), customer.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }

        String token = jwtTokenService.generateToken(customer);
        log.info("Login exitoso para clientId={}", customer.getClientId());
        return LoginResponseDTO.builder()
                .token(token)
                .type(CustomerConstants.JWT_TOKEN_TYPE)
                .build();
    }
}
