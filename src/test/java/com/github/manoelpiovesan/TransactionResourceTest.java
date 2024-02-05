package com.github.manoelpiovesan;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TransactionResourceTest {

    private static final Integer count = 0; // From import.sql

    private static final Long wrongId = 0L;

    private static final Map<String, Object> transactionMap = new HashMap<>();

    private static final Map<String, Object> customerMap = new HashMap<>();

    @Test
    @Order(1)
    public void testFirstCount() {
        given().when()
               .contentType("application/json")
               .get("/transaction/count")
               .then()
               .statusCode(200)
               .body(is(count.toString()));
    }

    @Test
    @Order(2)
    public void testGetWrongTransaction() {
        given().when()
               .contentType("application/json")
               .pathParam("id", wrongId)
               .get("/transaction/{id}")
               .then()
               .statusCode(404);
    }

    @Test
    @Order(3)
    public void insertTransactionWithWrongCustomer() {
        customerMap.put("id", wrongId);
        transactionMap.put("amount", 100);
        transactionMap.put("description", "Escola");
        transactionMap.put("type", "c");
        transactionMap.put("customer", customerMap);

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(404);
    }

    @Test
    @Order(4)
    public void insertTransactionWithNullAmount() {
        transactionMap.put("description", "Escola");
        transactionMap.put("type", "c");
        transactionMap.put("customer", customerMap);
        transactionMap.remove("amount");

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }

    @Test
    @Order(5)
    public void insertTransactionWithNullDescription() {
        customerMap.put("id", 1L);
        transactionMap.put("amount", 100);
        transactionMap.put("type", "c");
        transactionMap.put("customer", customerMap);
        transactionMap.remove("description");

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }

    @Test
    @Order(6)
    public void insertTransactionWithNullType() {
        transactionMap.put("amount", 100);
        transactionMap.put("description", "Escola");
        transactionMap.put("customer", customerMap);
        transactionMap.remove("type");

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }

    @Test
    @Order(7)
    public void insertTransactionWithNullCustomer() {
        transactionMap.put("amount", 100);
        transactionMap.put("description", "Escola");
        transactionMap.put("type", "c");
        transactionMap.remove("customer");

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }

    @Test
    @Order(8)
    public void insertTransactionWithInsufficientFunds() {
        transactionMap.put("amount", 50000000);
        transactionMap.put("description", "Teste");
        transactionMap.put("type", "d");
        transactionMap.put("customer", customerMap);

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }


    @Test
    @Order(9)
    public void insertTransactionWithLimitExceeded() {
        transactionMap.put("amount", 50000000);
        transactionMap.put("description", "Teste");
        transactionMap.put("type", "c");
        transactionMap.put("customer", customerMap);

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(422);
    }


    @Test
    @Order(10)
    public void insertTransaction() {
        transactionMap.put("amount", 100);
        transactionMap.put("description", "Escola");
        transactionMap.put("type", "c");
        transactionMap.put("customer", customerMap);

        given().when()
               .contentType("application/json")
               .body(transactionMap)
               .post("/transaction")
               .then()
               .statusCode(201);
    }

}
