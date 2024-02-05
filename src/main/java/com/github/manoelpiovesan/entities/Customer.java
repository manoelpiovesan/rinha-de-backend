package com.github.manoelpiovesan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends PanacheEntity {

    @Column(name = "account_limit", nullable = false)
    public Integer limit;

    @Column(name = "balance", nullable = false)
    public Integer balance;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    public List<Transaction> transactions;

}
