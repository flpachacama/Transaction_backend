package com.transaction.account.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.CustomerReference;
import com.transaction.account.exception.BusinessException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.CustomerReferenceRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private CustomerReferenceRepository customerReferenceRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_success() {
        // Arrange
        AccountRequestDTO request = AccountRequestDTO.builder()
                .accountNumber("478758")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(2000.00))
                .status(true)
                .clientId("joselema")
                .build();

        Account account = new Account();
        account.setAccountNumber("478758");
        account.setAccountType("AHORRO");
        account.setInitialBalance(BigDecimal.valueOf(2000.00));
        account.setCurrentBalance(BigDecimal.valueOf(2000.00));
        account.setStatus(true);
        account.setCustomerReference(new CustomerReference(1L, "joselema", "Jose Lema", true, null, null));

        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setAccountNumber("478758");
        savedAccount.setAccountType("AHORRO");
        savedAccount.setInitialBalance(BigDecimal.valueOf(2000.00));
        savedAccount.setCurrentBalance(BigDecimal.valueOf(2000.00));
        savedAccount.setStatus(true);
        savedAccount.setCustomerReference(new CustomerReference(1L, "joselema", "Jose Lema", true, null, null));

        AccountResponseDTO responseDTO = AccountResponseDTO.builder()
                .id(1L)
                .accountNumber("478758")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(2000.00))
                .currentBalance(BigDecimal.valueOf(2000.00))
                .status(true)
                .clientId("joselema")
                .build();

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(customerReferenceRepository.findByClientId("joselema"))
                .thenReturn(Optional.of(new CustomerReference(1L, "joselema", "Jose Lema", true, null, null)));
        when(accountMapper.toEntity(request)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        when(accountMapper.toResponseDTO(savedAccount)).thenReturn(responseDTO);

        // Act
        AccountResponseDTO result = accountService.createAccount(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAccountNumber()).isEqualTo("478758");
        assertThat(result.getClientId()).isEqualTo("joselema");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_duplicateAccountNumber_shouldThrowException() {
        // Arrange
        AccountRequestDTO request = AccountRequestDTO.builder()
                .accountNumber("478758")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(2000.00))
                .status(true)
                .clientId("joselema")
                .build();

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Ya existe una cuenta con accountNumber: 478758");
    }
}
