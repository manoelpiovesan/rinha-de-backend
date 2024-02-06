package com.github.manoelpiovesan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.manoelpiovesan.enums.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "transaction")
public class Transaction extends PanacheEntity {

    @Column(name = "amount", nullable = false)
    public Integer amount;

    @Column(name = "description", nullable = false, length = 10)
    public String description;

    @Column(name = "type", nullable = false)
    public TransactionType type;

    @Column(name = "created_at", nullable = false)
    public Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("valor", amount);
        map.put("descricao", description);
        map.put("tipo", type);
        map.put("realizada_em", createdAt.getTime());

        return map;
    }

}
