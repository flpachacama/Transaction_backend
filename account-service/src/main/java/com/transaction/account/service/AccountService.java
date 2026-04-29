package com.transaction.account.service;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO requestDTO);
    List<AccountResponseDTO> getAllAccounts();
    AccountResponseDTO getAccountById(Long id);
    AccountResponseDTO updateAccount(Long id, AccountRequestDTO requestDTO);
    void deleteAccount(Long id);
    Optional<AccountResponseDTO> getByAccountNumber(String accountNumber);
}
