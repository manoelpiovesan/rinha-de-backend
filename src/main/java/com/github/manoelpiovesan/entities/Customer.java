package com.github.manoelpiovesan.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer extends PanacheEntity {

    @Column(name = "account_limit", nullable = false)
    public Integer limit;

    @Column(name = "balance", nullable = false)
    public Integer balance;

}
