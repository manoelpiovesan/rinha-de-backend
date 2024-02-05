package com.github.manoelpiovesan.resources;

import com.github.manoelpiovesan.entities.Customer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customer")
public class CustomerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        return Response.ok(Customer.listAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        Customer localCustomer = Customer.findById(id);

        if (localCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(localCustomer).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return Response.ok(Customer.count()).build();
    }

}
