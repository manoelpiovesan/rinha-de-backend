package com.github.manoelpiovesan;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest public class TransactionResourceTest {

    private static final Map<String, Object> transactionMap = new HashMap<>();

    private static final Map<String, Object> customerMap = new HashMap<>();

    private static final List<Map<String, Object>> emptyList =
            new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        customerMap.put("id", 1); // From import.sql
        customerMap.put("limit", 100000); // From import.sql
        customerMap.put("balance", 0); // From import.sql
    }

//    @Test
//    @Order(1)
//    public void testFirstGet() {
//        given()
//                .when()
//                .pathParam("id", 1)
//                .contentType("application/json")
//                .get("/clientes/{id}/extrato")
//                .then()
//                .statusCode(200)
//                .body("saldo.limite", is(customerMap.get("limit")),
//                      "saldo.total", is(customerMap.get("balance")),
//                      "saldo.data_extrato", notNullValue(),
//                      "ultimas_transacoes", is(emptyList)
//                );
//
//    }

    @Test
    @Order(2)
    public void testInsertTransactionWithWrongAmount() {
        // Invalid
        transactionMap.put("valor", 100000000);

        // Valid
        transactionMap.put("descricao", "Teste");
        transactionMap.put("tipo", "d");

        given()
                .when()
                .pathParam("id", 1)
                .contentType("application/json")
                .body(transactionMap)
                .post("/clientes/{id}/transacoes")
                .then()
                .statusCode(422);

        transactionMap.put("valor", 1000);
    }

    @Test
    @Order(3)
    public void testInsertTransactionWithWrongDescription() {
        // Invalid
        transactionMap.put("descricao",
                           "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        given()
                .when()
                .pathParam("id", customerMap.get("id"))
                .contentType("application/json")
                .body(transactionMap)
                .post("/clientes/{id}/transacoes")
                .then()
                .statusCode(422);

        transactionMap.put("descricao", "Teste");
    }

    @Test
    @Order(4)
    public void testFirstInsert() {
        transactionMap.put("tipo", "d");
        transactionMap.put("valor", 1000);
        transactionMap.put("descricao", "Teste");

        Integer newBalance = (Integer) customerMap.get("balance") - 1000;

        given()
                .when()
                .pathParam("id", customerMap.get("id"))
                .contentType("application/json")
                .body(transactionMap)
                .post("/clientes/{id}/transacoes")
                .then()
                .statusCode(200).body(
                        "limite", is(customerMap.get("limit")),
                        "saldo", is(newBalance),
                        "limite",
                        is(Integer.parseInt(customerMap.get("limit").toString()))
                );

    }

}
