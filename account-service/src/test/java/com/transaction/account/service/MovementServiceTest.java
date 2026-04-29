package com.transaction.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaction.account.dto.MovementRequestDTO;
import com.transaction.account.dto.MovementResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.CustomerReference;
import com.transaction.account.entity.Movement;
import com.transaction.account.exception.InsufficientBalanceException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.MovementRepository;
import com.transaction.account.service.impl.MovementServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private MovementServiceImpl movementService;

    @Test
    void deposit_shouldIncreaseBalance() {
        Account account = buildAccount(BigDecimal.valueOf(1000));

        MovementRequestDTO request = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("DEPOSIT")
                .amount(BigDecimal.valueOf(250))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toMovementResponseDTO(any(Movement.class))).thenReturn(MovementResponseDTO.builder().build());

        movementService.createMovement(request);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo("1250");
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_withSufficientBalance_shouldDecreaseBalance() {
        Account account = buildAccount(BigDecimal.valueOf(1000));

        MovementRequestDTO request = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("WITHDRAW")
                .amount(BigDecimal.valueOf(575))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toMovementResponseDTO(any(Movement.class))).thenReturn(MovementResponseDTO.builder().build());

        movementService.createMovement(request);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo("425");
    }

    @Test
    void withdraw_withInsufficientBalance_shouldThrowException() {
        Account account = buildAccount(BigDecimal.valueOf(100));

        MovementRequestDTO request = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("WITHDRAW")
                .amount(BigDecimal.valueOf(150))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> movementService.createMovement(request))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessage("Saldo no disponible");
    }

    @Test
    void fullFlow_createAccountDepositWithdraw_shouldKeepExpectedBalance() {
        Account account = buildAccount(BigDecimal.valueOf(2000));

        MovementRequestDTO deposit = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("DEPOSIT")
                .amount(BigDecimal.valueOf(200))
                .build();
        MovementRequestDTO withdraw = MovementRequestDTO.builder()
                .accountNumber("478758")
                .movementType("WITHDRAW")
                .amount(BigDecimal.valueOf(100))
                .build();

        when(accountRepository.findByAccountNumber("478758")).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toMovementResponseDTO(any(Movement.class))).thenReturn(MovementResponseDTO.builder().build());

        movementService.createMovement(deposit);
        movementService.createMovement(withdraw);

        assertThat(account.getCurrentBalance()).isEqualByComparingTo("2100");
    }

    private Account buildAccount(BigDecimal balance) {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("478758");
        account.setCurrentBalance(balance);
        account.setInitialBalance(balance);
        account.setStatus(true);

        CustomerReference customer = new CustomerReference();
        customer.setId(1L);
        customer.setClientId("joselema");
        customer.setClientName("Jose Lema");
        customer.setStatus(true);
        customer.setCreatedAt(LocalDateTime.now());

        account.setCustomerReference(customer);
        return account;
    }
}
