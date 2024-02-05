package com.github.manoelpiovesan.entities;

import com.github.manoelpiovesan.enums.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction extends PanacheEntity {

    @Column(name = "amount", nullable = false)
    public Integer amount;

    @Column(name = "description", nullable = false, length = 10)
    public String description;

    @Column(name = "type", nullable = false)
    public TransactionType type;

    @Column(name = "createdAt", nullable = false)
    public Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

}
