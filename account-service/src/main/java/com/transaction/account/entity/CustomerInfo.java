package com.transaction.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_info")
public class CustomerInfo {

    @Id
    @Column(name = "client_id", length = 50)
    private String clientId;

    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "status")
    private Boolean status;
}
