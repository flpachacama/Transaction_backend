package com.transaction.customer.service.impl;

import java.util.List;
import com.transaction.customer.dto.CustomerRequestDTO;
import com.transaction.customer.dto.CustomerResponseDTO;
import com.transaction.customer.entity.Customer;
import com.transaction.customer.exception.BusinessException;
import com.transaction.customer.exception.ResourceNotFoundException;
import com.transaction.customer.mapper.CustomerMapper;
import com.transaction.customer.messaging.CustomerEventPublisher;
import com.transaction.customer.repository.CustomerRepository;
import com.transaction.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerEventPublisher customerEventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        validateUniqueClientId(requestDTO.getClientId(), null);

        Customer customer = customerMapper.toEntity(requestDTO);
        customer.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        Customer savedCustomer = customerRepository.save(customer);
        customerEventPublisher.publishCustomerCreated(savedCustomer);

        log.info("Cliente creado correctamente: id={}, clientId={}", savedCustomer.getId(), savedCustomer.getClientId());
        return customerMapper.toResponseDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = findCustomerById(id);
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = findCustomerById(id);
        validateUniqueClientId(requestDTO.getClientId(), id);

        customerMapper.updateEntity(requestDTO, customer);
        customer.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Cliente actualizado correctamente: id={}, clientId={}", updatedCustomer.getId(), updatedCustomer.getClientId());
        return customerMapper.toResponseDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = findCustomerById(id);
        customerRepository.delete(customer);
        log.info("Cliente eliminado correctamente: id={}, clientId={}", customer.getId(), customer.getClientId());
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    private void validateUniqueClientId(String clientId, Long currentCustomerId) {
        customerRepository.findByClientId(clientId)
                .filter(existing -> currentCustomerId == null || !existing.getId().equals(currentCustomerId))
                .ifPresent(existing -> {
                    throw new BusinessException("Ya existe un cliente con clientId: " + clientId);
                });
    }
}
