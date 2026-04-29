package com.transaction.account.service.impl;

import com.transaction.account.dto.MovementRequestDTO;
import com.transaction.account.dto.MovementResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.Movement;
import com.transaction.account.exception.BusinessException;
import com.transaction.account.exception.InsufficientBalanceException;
import com.transaction.account.exception.ResourceNotFoundException;
import com.transaction.account.mapper.AccountMapper;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.MovementRepository;
import com.transaction.account.service.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public MovementResponseDTO createMovement(MovementRequestDTO requestDTO) {
        Account account = accountRepository.findByAccountNumber(requestDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con accountNumber: " + requestDTO.getAccountNumber()));

        BigDecimal amount = requestDTO.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto debe ser mayor que cero");
        }
        LocalDateTime now = LocalDateTime.now();
        Movement movement = new Movement();
        movement.setMovementDate(now);
        movement.setMovementType(requestDTO.getMovementType());
        movement.setAmount(amount);

        if ("DEPOSIT".equalsIgnoreCase(requestDTO.getMovementType())) {
            account.setCurrentBalance(account.getCurrentBalance().add(amount));
            log.info("Depósito aplicado: accountNumber={}, amount={}", account.getAccountNumber(), amount);
        } else if ("WITHDRAW".equalsIgnoreCase(requestDTO.getMovementType())) {
            if (account.getCurrentBalance().compareTo(amount) < 0) {
                log.warn("Saldo insuficiente para retiro: accountNumber={}, amount={}", account.getAccountNumber(), amount);
                throw new InsufficientBalanceException("Saldo no disponible");
            }
            account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
            movement.setAmount(amount.negate());
            log.info("Retiro aplicado: accountNumber={}, amount={}", account.getAccountNumber(), amount);
        } else {
            throw new BusinessException("Tipo de movimiento inválido");
        }

        movement.setBalance(account.getCurrentBalance());
        movement.setAccount(account);
        Movement saved = movementRepository.save(movement);
        account.getMovements().add(saved);
        accountRepository.save(account);
        log.info("Movimiento registrado: accountNumber={}, type={}, amount={}", account.getAccountNumber(), requestDTO.getMovementType(), amount);
        return accountMapper.toMovementResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDTO> getAllMovements() {
        return movementRepository.findAll().stream().map(accountMapper::toMovementResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovementResponseDTO getMovementById(Long id) {
        Movement movement = movementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
        return accountMapper.toMovementResponseDTO(movement);
    }

    @Override
    public void deleteMovement(Long id) {
        Movement movement = movementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
        movementRepository.delete(movement);
        log.info("Movimiento eliminado: id={}", id);
    }
}
