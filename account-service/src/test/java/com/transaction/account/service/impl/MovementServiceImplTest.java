package com.transaction.account.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaction.account.dto.MovementRequestDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.CustomerReference;
import com.transaction.account.entity.Movement;
import com.transaction.account.exception.InsufficientBalanceException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.MovementRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private MovementServiceImpl movementService;

    @Test
    void withdraw_insufficientBalance_shouldThrowException() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("478758");
        account.setCurrentBalance(BigDecimal.valueOf(100.00));
        account.setStatus(true);
        account.setCustomerReference(new CustomerReference());

        MovementRequestDTO request = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("WITHDRAW")
                .amount(BigDecimal.valueOf(150.00))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));

        // Act & Assert
        assertThatThrownBy(() -> movementService.createMovement(request))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessage("Saldo no disponible");

        verify(accountRepository).findByAccountNumber("478758");
    }

    @Test
    void withdraw_sufficientBalance_shouldSuccess() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("478758");
        account.setCurrentBalance(BigDecimal.valueOf(1000.00));
        account.setStatus(true);
        account.setCustomerReference(new CustomerReference());

        MovementRequestDTO request = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("WITHDRAW")
                .amount(BigDecimal.valueOf(575.00))
                .build();

        Movement movement = new Movement();
        movement.setId(1L);
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovementType("WITHDRAW");
        movement.setAmount(BigDecimal.valueOf(-575.00));
        movement.setBalance(BigDecimal.valueOf(425.00));
        movement.setAccount(account);

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);
        when(accountMapper.toMovementResponseDTO(movement)).thenReturn(null);

        // Act
        movementService.createMovement(request);

        // Assert
        assertThat(account.getCurrentBalance()).isEqualTo(BigDecimal.valueOf(425.00));
        verify(movementRepository).save(any(Movement.class));
        verify(accountRepository).save(account);
    }
}
