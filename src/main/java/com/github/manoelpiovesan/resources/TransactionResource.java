package com.github.manoelpiovesan.resources;

import com.github.manoelpiovesan.entities.Customer;
import com.github.manoelpiovesan.entities.Transaction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

        return Response.status(Response.Status.OK)
                       .entity(transaction)
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
    private Transaction validate(Transaction transaction, Long customerId) {
        Customer customer = Customer.findById(customerId);

        if (customer == null) {
            throw new WebApplicationException("Customer not found", 404);
        }

        transaction.customer = customer;

        if (transaction.amount == null) {
            throw new WebApplicationException("Amount can't be null", 422);
        }

        if (transaction.description == null ||
            transaction.description.isEmpty()) {
            throw new WebApplicationException("Description can't be null", 422);
        }

        if (transaction.type == null) {
            throw new WebApplicationException("Type can't be null", 422);
        }

        if (abs(customer.balance - transaction.amount) > customer.limit) {
            throw new WebApplicationException("Transaction not allowed", 422);
        }

        customer.balance -= transaction.amount;
        customer.persist();

        return transaction;
    }

}
