package com.github.manoelpiovesan.resources;

import com.github.manoelpiovesan.entities.Customer;
import com.github.manoelpiovesan.entities.Transaction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

@Path("/clientes")
public class TransactionResource {

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return Response.ok(Transaction.count()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/transacoes")
    @Transactional
    public Response create(@PathParam("id") Long customerId,
                           Transaction transaction) {

        Transaction localTransaction = validate(transaction, customerId);

        localTransaction.persist();
        Map<String, Object> response = new HashMap<>();

        Customer customer = Customer.findById(customerId);

        response.put("limite", customer.limit);
        response.put("saldo", customer.balance);

        return Response.status(Response.Status.OK)
                       .entity(response)
                       .build();
    }

    @GET
    @Path("/{id}/extrato")
    @Produces(MediaType.APPLICATION_JSON)
    public Response extrato(@PathParam("id") Long customerId) {
        Customer customer = Customer.findById(customerId);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(customer.getExtrato()).build();
    }

    @Transactional
    public Transaction validate(Transaction transaction, Long customerId) {
        Customer customer = Customer.findById(customerId);

        if (customer == null) {
            throw new WebApplicationException("Customer not found", 404);
        }

        transaction.customer = customer;

        if (transaction.valor == null) {
            throw new WebApplicationException("Amount can't be null", 422);
        }

        if (transaction.descricao == null ||
            transaction.descricao.isEmpty() ||
            transaction.descricao.length() > 10) {
            throw new WebApplicationException(
                    "Description can't be null or bigger than 10 characters",
                    422);
        }

        if (transaction.tipo == null) {
            throw new WebApplicationException("Type can't be null", 422);
        }

        if (abs(customer.balance - transaction.valor) > customer.limit) {
            throw new WebApplicationException("Transaction not allowed", 422);
        }

        customer.balance -= transaction.valor;
        customer.persist();

        return transaction;
    }

}
