package com.github.manoelpiovesan;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CustomerResourceTest {

    private static final Integer count = 5; // From import.sql
    private static final Long wrongId = 6L;// From competition readme.md
    private static final Map<String, Object> customerMap = new HashMap<>();

    @Test
    @Order(1)
    public void testFirstCount() {
        given().when()
               .contentType("application/json")
               .get("/customer/count")
               .then()
               .statusCode(200)
               .body(is(count.toString()));
    }

    @Test
    @Order(2)
    public void testGetWrongCustomer() {
        given().when()
               .contentType("application/json")
               .pathParam("id", wrongId)
               .get("/customer/{id}")
               .then()
               .statusCode(404);
    }

    @Test
    @Order(3)
    public void getCustomerById() {
        customerMap.put("id", 1); // From import.sql
        customerMap.put("limit", 100000); // From import.sql
        customerMap.put("balance", 0); // From import.sql

        given().when()
               .contentType("application/json")
               .pathParam("id", 1)
               .get("/customer/{id}")
               .then()
               .statusCode(200)
               .body("id", is(customerMap.get("id")),
                     "limit", is(customerMap.get("limit")),
                     "balance", is(customerMap.get("balance"))
               );
    }

}
