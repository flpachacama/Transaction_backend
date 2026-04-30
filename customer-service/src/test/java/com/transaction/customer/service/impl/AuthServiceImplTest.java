package com.transaction.customer.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.transaction.customer.dto.LoginRequestDTO;
import com.transaction.customer.entity.Customer;
import com.transaction.customer.exception.BusinessException;
import com.transaction.customer.repository.CustomerRepository;
import com.transaction.customer.security.JwtTokenService;
import com.transaction.customer.service.AuthService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_invalidCredentials_shouldThrowException() {
        // Arrange
        LoginRequestDTO request = LoginRequestDTO.builder()
                .clientId("nonexistent")
                .password("wrongpass")
                .build();

        when(customerRepository.findByClientIdAndStatusTrue("nonexistent"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Credenciales inválidas");
    }
}
