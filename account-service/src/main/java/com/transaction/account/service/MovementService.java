package com.transaction.account.service;

import com.transaction.account.dto.MovementRequestDTO;
import com.transaction.account.dto.MovementResponseDTO;
import java.util.List;

public interface MovementService {
    MovementResponseDTO createMovement(MovementRequestDTO requestDTO);
    List<MovementResponseDTO> getAllMovements();
    MovementResponseDTO getMovementById(Long id);
    void deleteMovement(Long id);
}
