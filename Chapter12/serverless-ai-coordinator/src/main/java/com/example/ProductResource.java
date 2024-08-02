package com.example;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product", description = "Product management operations")
public class ProductResource {

    @Inject
    ProductRepository productRepository;

    @GET
    @Counted(name = "getAllProductsCount", description = "How many times getAllProducts has been invoked")
    @Timed(name = "getAllProductsTimer", description = "A measure of how long it takes to perform getAllProducts", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Get all products", description = "Returns a list of all products with pagination and sorting")
    public Response getAllProducts(@QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") @DefaultValue("name") String sort) {
        return null;
        // Existing implementation
    }

    @GET
    @Path("/{id}")
    @Counted(name = "getProductCount", description = "How many times getProduct has been invoked")
    @Timed(name = "getProductTimer", description = "A measure of how long it takes to perform getProduct", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Get a product by ID", description = "Returns a single product")
    public Response getProduct(@PathParam("id") String id) {
        return null;
        // Implement get single product logic
    }

    @POST
    @Counted(name = "createProductCount", description = "How many times createProduct has been invoked")
    @Timed(name = "createProductTimer", description = "A measure of how long it takes to perform createProduct", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created product")
    public Response createProduct(Product product) {
        return null;
        // Implement create product logic
    }

    // Add other CRUD methods (PUT, DELETE, GET all)
}
