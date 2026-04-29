package com.transaction.account.mapper;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import com.transaction.account.dto.MovementResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "customerReference.clientId", target = "clientId")
    @Mapping(source = "customerReference.clientName", target = "clientName")
    AccountResponseDTO toResponseDTO(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentBalance", ignore = true)
    @Mapping(target = "customerReference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "movements", ignore = true)
    Account toEntity(AccountRequestDTO requestDTO);

    @Mapping(source = "movementDate", target = "date")
    @Mapping(source = "account.id", target = "accountId")
    MovementResponseDTO toMovementResponseDTO(Movement movement);
}
