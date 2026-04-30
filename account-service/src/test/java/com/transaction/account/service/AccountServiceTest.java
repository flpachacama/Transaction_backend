package com.transaction.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.CustomerReference;
import com.transaction.account.exception.ResourceNotFoundException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.CustomerReferenceRepository;
import com.transaction.account.service.impl.AccountServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerReferenceRepository customerReferenceRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_shouldSetCurrentBalanceAndPersist() {
        AccountRequestDTO request = AccountRequestDTO.builder()
                .clientId("joselema")
                .accountNumber("478758")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(2000))
                .status(true)
                .build();

        CustomerReference customerReference = new CustomerReference();
        customerReference.setId(1L);
        customerReference.setClientId("joselema");
        customerReference.setClientName("Jose Lema");
        customerReference.setStatus(true);

        Account toSave = new Account();
        toSave.setAccountNumber("478758");
        toSave.setInitialBalance(BigDecimal.valueOf(2000));
        toSave.setStatus(true);

        Account saved = new Account();
        saved.setId(1L);
        saved.setAccountNumber("478758");
        saved.setInitialBalance(BigDecimal.valueOf(2000));
        saved.setCurrentBalance(BigDecimal.valueOf(2000));
        saved.setStatus(true);
        saved.setCustomerReference(customerReference);

        AccountResponseDTO response = AccountResponseDTO.builder()
                .id(1L)
                .clientId("joselema")
                .accountNumber("478758")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(2000))
                .currentBalance(BigDecimal.valueOf(2000))
                .status(true)
                .build();

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(customerReferenceRepository.findByClientId("joselema")).thenReturn(Optional.of(customerReference));
        when(accountMapper.toEntity(request)).thenReturn(toSave);
        when(accountRepository.save(any(Account.class))).thenReturn(saved);
        when(accountMapper.toResponseDTO(saved)).thenReturn(response);

        AccountResponseDTO result = accountService.createAccount(request);

        assertThat(result.getAccountNumber()).isEqualTo("478758");
        assertThat(result.getCurrentBalance()).isEqualByComparingTo("2000");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccountById_whenExists_shouldReturnAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("478758");

        AccountResponseDTO response = AccountResponseDTO.builder()
                .id(1L)
                .accountNumber("478758")
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toResponseDTO(account)).thenReturn(response);

        AccountResponseDTO result = accountService.getAccountById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAccountNumber()).isEqualTo("478758");
    }

    @Test
    void getAccountById_whenMissing_shouldThrowNotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cuenta no encontrada con id: 99");
    }
}
