package com.transaction.account.controller;

import com.transaction.account.dto.AccountStatementDTO;
import com.transaction.account.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Operaciones REST para reportes de estado de cuenta")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Generar estado de cuenta por cliente y rango de fechas")
    @ApiResponse(responseCode = "200", description = "Reporte generado")
    @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    public ResponseEntity<List<AccountStatementDTO>> generateStatement(
            @Parameter(description = "Fecha/hora inicio (ISO-8601)", example = "2026-04-01T00:00:00", required = true)
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha/hora fin (ISO-8601)", example = "2026-04-30T23:59:59", required = true)
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Identificador de cliente", example = "joselema", required = true)
            @RequestParam("clientId") @NotBlank String clientId) {
        return ResponseEntity.ok(reportService.generateStatement(startDate, endDate, clientId));
    }
}
