package com.transaction.account.mapper;

import com.transaction.account.dto.AccountRequestDTO;
import com.transaction.account.dto.AccountResponseDTO;
import com.transaction.account.dto.MovementResponseDTO;
import com.transaction.account.entity.Account;
import com.transaction.account.entity.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDTO toResponseDTO(Account account);

    Account toEntity(AccountRequestDTO requestDTO);

    MovementResponseDTO toMovementResponseDTO(Movement movement);
}
