package com.transaction.account.service;

import com.transaction.account.dto.AccountStatementDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    List<AccountStatementDTO> generateStatement(LocalDateTime startDate, LocalDateTime endDate, String clientId);
}
