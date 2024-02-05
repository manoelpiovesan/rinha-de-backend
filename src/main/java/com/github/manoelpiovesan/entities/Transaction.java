package com.github.manoelpiovesan.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction extends PanacheEntity {

    @Column(name = "amount", nullable = false)
    public Integer amount;

    @Column(name = "description", nullable = false, length = 10)
    public String description;

    @Column(name = "type", nullable = false, length = 1)
    public String type;

}
