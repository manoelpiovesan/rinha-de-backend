package com.github.manoelpiovesan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getExtrato() {
        Map<String, Object> extrato = new HashMap<>();
        Map<String, Object> saldo = new HashMap<>();
        List<Map<String,Object>> transacoes = new ArrayList<>();

        saldo.put("limite", limit);
        saldo.put("total", balance);
        saldo.put("data_extrato", LocalDateTime.now());

        extrato.put("saldo", saldo);

        for (Transaction transaction : transactions) {
            transacoes.add(transaction.toMap());
        }

        extrato.put("ultimas_transacoes", transacoes);

        return extrato;
    }

}
