package com.transaction.account.service.impl;

import com.transaction.account.dto.AccountStatementDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.Movement;
import com.transaction.account.exception.BusinessException;
import com.transaction.account.exception.ResourceNotFoundException;
import com.transaction.account.repository.CustomerReferenceRepository;
import com.transaction.account.repository.MovementRepository;
import com.transaction.account.service.ReportService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final CustomerReferenceRepository customerReferenceRepository;
    private final MovementRepository movementRepository;

    @Override
    public List<AccountStatementDTO> generateStatement(
            LocalDateTime startDate, LocalDateTime endDate, String clientId) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("El rango de fechas es inválido");
        }

        log.info("Generando estado de cuenta clientId={}, startDate={}, endDate={}", clientId, startDate, endDate);

        List<AccountStatementDTO> statements = new ArrayList<>();
        var customerReference = customerReferenceRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con clientId: " + clientId));

        List<Account> accounts = customerReference.getAccounts();
        for (Account account : accounts) {
            List<Movement> movements = movementRepository.findByAccountIdAndMovementDateBetween(
                    account.getId(), startDate, endDate);

            if (movements.isEmpty()) {
                statements.add(AccountStatementDTO.builder()
                        .date(startDate)
                        .clientName(customerReference.getClientName())
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .initialBalance(account.getInitialBalance())
                        .status(account.getStatus())
                        .movement(BigDecimal.ZERO)
                        .balance(account.getCurrentBalance())
                        .build());
                continue;
            }

            for (Movement movement : movements) {
                statements.add(AccountStatementDTO.builder()
                        .date(movement.getMovementDate())
                        .clientName(customerReference.getClientName())
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .initialBalance(account.getInitialBalance())
                        .status(account.getStatus())
                        .movement(movement.getAmount())
                        .balance(movement.getBalance())
                        .build());
            }
        }

        log.info("Reporte generado clientId={}, registros={}", clientId, statements.size());

        return statements;
    }
}
