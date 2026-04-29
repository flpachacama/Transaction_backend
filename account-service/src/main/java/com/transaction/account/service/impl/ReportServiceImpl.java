package com.transaction.account.service.impl;

import com.transaction.account.dto.AccountStatementDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.Movement;
import com.transaction.account.repository.AccountRepository;
import com.transaction.account.repository.CustomerReferenceRepository;
import com.transaction.account.repository.MovementRepository;
import com.transaction.account.service.ReportService;
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

    private final AccountRepository accountRepository;
    private final CustomerReferenceRepository customerReferenceRepository;
    private final MovementRepository movementRepository;

    @Override
    public List<AccountStatementDTO> generateStatement(
            LocalDateTime startDate, LocalDateTime endDate, String clientId) {
        
        log.info(
                "Generating statement for clientId={}, period: {} to {}",
                clientId,
                startDate,
                endDate);

        List<AccountStatementDTO> statements = new ArrayList<>();

        customerReferenceRepository
                .findByClientId(clientId)
                .ifPresentOrElse(
                        customerRef -> {
                            List<Account> accounts = customerRef.getAccounts();
                            
                            for (Account account : accounts) {
                                List<Movement> movements =
                                        movementRepository.findByMovementDateBetween(
                                                startDate, endDate);

                                if (movements.isEmpty()) {
                                    AccountStatementDTO statement =
                                            AccountStatementDTO.builder()
                                                    .date(startDate.toLocalDate())
                                                    .client(customerRef.getClientName())
                                                    .accountNumber(account.getAccountNumber())
                                                    .type(account.getAccountType())
                                                    .initialBalance(account.getInitialBalance())
                                                    .status(account.getStatus())
                                                    .movement(java.math.BigDecimal.ZERO)
                                                    .availableBalance(account.getCurrentBalance())
                                                    .build();
                                    statements.add(statement);
                                } else {
                                    for (Movement movement : movements) {
                                        AccountStatementDTO statement =
                                                AccountStatementDTO.builder()
                                                        .date(
                                                                movement
                                                                        .getMovementDate()
                                                                        .toLocalDate())
                                                        .client(customerRef.getClientName())
                                                        .accountNumber(account.getAccountNumber())
                                                        .type(account.getAccountType())
                                                        .initialBalance(account.getInitialBalance())
                                                        .status(account.getStatus())
                                                        .movement(movement.getAmount())
                                                        .availableBalance(movement.getBalance())
                                                        .build();
                                        statements.add(statement);
                                    }
                                }
                            }
                            log.info(
                                    "Statement generated successfully: clientId={}, records={}",
                                    clientId,
                                    statements.size());
                        },
                        () -> {
                            log.warn("Client not found: clientId={}", clientId);
                        });

        return statements;
    }
}
