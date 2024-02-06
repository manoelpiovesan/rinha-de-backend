package com.github.manoelpiovesan.entities;

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
    public Integer valor;

    @Column(name = "description", nullable = false, length = 10)
    public String descricao;

    @Column(name = "type", nullable = false)
    public TransactionType tipo;

    @Column(name = "created_at", nullable = false)
    public Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("valor", valor);
        map.put("descricao", descricao);
        map.put("tipo", tipo);
        map.put("realizada_em", createdAt.getTime());

        return map;
    }

}
