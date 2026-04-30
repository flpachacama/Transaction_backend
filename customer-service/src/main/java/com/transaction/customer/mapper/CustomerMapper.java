package com.transaction.customer.mapper;

import com.transaction.customer.dto.CustomerRequestDTO;
import com.transaction.customer.dto.CustomerResponseDTO;
import com.transaction.customer.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerResponseDTO toResponseDTO(Customer customer);

    Customer toEntity(CustomerRequestDTO requestDTO);

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", ignore = true)
    void updateEntity(CustomerRequestDTO requestDTO, @MappingTarget Customer customer);
}
