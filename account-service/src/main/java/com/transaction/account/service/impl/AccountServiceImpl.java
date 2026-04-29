package com.transaction.account.service.impl;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.exception.BusinessException;
import com.transaction.account.exception.ResourceNotFoundException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
        if (accountRepository.existsByAccountNumber(requestDTO.getAccountNumber())) {
            throw new BusinessException("Ya existe una cuenta con accountNumber: " + requestDTO.getAccountNumber());
        }

        Account account = accountMapper.toEntity(requestDTO);
        account.setCurrentBalance(requestDTO.getInitialBalance());

        Account saved = accountRepository.save(account);
        log.info("Cuenta creada: accountNumber={}, clientId={}", saved.getAccountNumber(), saved.getClientId());
        return accountMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO requestDTO) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
        account.setAccountType(requestDTO.getAccountType());
        account.setStatus(requestDTO.getStatus());
        account.setClientId(requestDTO.getClientId());
        account.setClientName(requestDTO.getClientName());
        account.setInitialBalance(requestDTO.getInitialBalance());
        if (account.getCurrentBalance() == null) account.setCurrentBalance(requestDTO.getInitialBalance());
        Account updated = accountRepository.save(account);
        log.info("Cuenta actualizada: id={}, accountNumber={}", updated.getId(), updated.getAccountNumber());
        return accountMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
        accountRepository.delete(account);
        log.info("Cuenta eliminada: id={}, accountNumber={}", account.getId(), account.getAccountNumber());
    }
}
