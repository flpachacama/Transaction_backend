package com.transaction.customer.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaction.customer.dto.CustomerRequestDTO;
import com.transaction.customer.dto.CustomerResponseDTO;
import com.transaction.customer.entity.Customer;
import com.transaction.customer.mapper.CustomerMapper;
import com.transaction.customer.messaging.CustomerEventPublisher;
import com.transaction.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerEventPublisher customerEventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void createCustomer_success() {
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .name("Jose Lema")
                .gender("MALE")
                .age(34)
                .identification("1723434343")
                .address("Otavalo sn y principal")
                .phone("098254785")
                .clientId("joselema")
                .password("1234")
                .status(true)
                .build();

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setAge(request.getAge());
        customer.setIdentification(request.getIdentification());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());
        customer.setClientId(request.getClientId());
        customer.setStatus(request.getStatus());

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName(request.getName());
        savedCustomer.setGender(request.getGender());
        savedCustomer.setAge(request.getAge());
        savedCustomer.setIdentification(request.getIdentification());
        savedCustomer.setAddress(request.getAddress());
        savedCustomer.setPhone(request.getPhone());
        savedCustomer.setClientId(request.getClientId());
        savedCustomer.setPassword("encoded-password");
        savedCustomer.setStatus(request.getStatus());

        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .identification(request.getIdentification())
                .address(request.getAddress())
                .phone(request.getPhone())
                .clientId(request.getClientId())
                .status(true)
                .build();

        when(customerRepository.findByClientId("joselema")).thenReturn(java.util.Optional.empty());
        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(passwordEncoder.encode("1234")).thenReturn("encoded-password");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerMapper.toResponseDTO(savedCustomer)).thenReturn(responseDTO);
        doNothing().when(customerEventPublisher).publishCustomerCreated(savedCustomer);

        CustomerResponseDTO result = customerService.createCustomer(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getClientId()).isEqualTo("joselema");
        assertThat(result.getStatus()).isTrue();
        verify(customerEventPublisher).publishCustomerCreated(savedCustomer);
    }
}
