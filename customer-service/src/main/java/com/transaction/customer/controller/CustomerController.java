package com.transaction.customer.controller;

import java.util.List;
import com.transaction.customer.dto.CustomerRequestDTO;
import com.transaction.customer.dto.CustomerResponseDTO;
import com.transaction.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clientes")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody CustomerRequestDTO requestDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
