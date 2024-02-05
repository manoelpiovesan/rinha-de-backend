package com.github.manoelpiovesan.resources;

import com.github.manoelpiovesan.entities.Customer;
import com.github.manoelpiovesan.entities.Transaction;
import com.github.manoelpiovesan.enums.TransactionType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transaction")
public class TransactionResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        return Response.ok(Transaction.listAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        Transaction localTransaction = Transaction.findById(id);

        if (localTransaction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(localTransaction).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return Response.ok(Transaction.count()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Transaction transaction) {

        validate(transaction);

        Customer customer = Customer.findById(transaction.customer.id);

        if (transaction.type == TransactionType.c) {
            customer.limit -= transaction.amount;
        } else {
            customer.balance -= transaction.amount;
        }

        customer.persist();
        transaction.persist();

        return Response.status(Response.Status.CREATED)
                       .entity(transaction)
                       .build();
    }

    private Transaction validate(Transaction transaction) {
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

        if (transaction.customer == null) {
            throw new WebApplicationException("Customer can't be null", 422);
        }

        Customer customer = Customer.findById(transaction.customer.id);

        if (customer == null) {
            throw new WebApplicationException("Customer not found", 404);
        }

        if (customer.balance < transaction.amount &&
            transaction.type == TransactionType.d) {
            throw new WebApplicationException("Insufficient funds", 422);
        }

        if (customer.limit < transaction.amount &&
            transaction.type == TransactionType.c) {
            throw new WebApplicationException("Limit exceeded", 422);
        }

        return transaction;
    }

}
