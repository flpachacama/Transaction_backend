package com.transaction.account.controller;

import com.transaction.account.dto.MovementRequestDTO;
import com.transaction.account.dto.MovementResponseDTO;
import com.transaction.account.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/movimientos")
@Tag(name = "Movimientos", description = "Operaciones REST para gestión de movimientos")
public class MovementController {

    private final MovementService movementService;

    @PostMapping
    @Operation(summary = "Crear movimiento")
    @ApiResponse(responseCode = "201", description = "Movimiento creado")
    @ApiResponse(responseCode = "400", description = "Error de validación o negocio")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<MovementResponseDTO> createMovement(@Valid @RequestBody MovementRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.createMovement(dto));
    }

    @GetMapping
    @Operation(summary = "Listar movimientos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<MovementResponseDTO>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por id")
    @ApiResponse(responseCode = "200", description = "Movimiento encontrado")
    @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    public ResponseEntity<MovementResponseDTO> getMovementById(
            @Parameter(description = "Id de movimiento", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(movementService.getMovementById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento")
    @ApiResponse(responseCode = "204", description = "Movimiento eliminado")
    @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    public ResponseEntity<Void> deleteMovement(
            @Parameter(description = "Id de movimiento", required = true) @PathVariable Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
