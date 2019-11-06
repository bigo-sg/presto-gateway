package com.lyft.data.gateway.ha.resource;

import com.google.inject.Inject;
import com.lyft.data.gateway.ha.router.GatewayBackendManager;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import io.dropwizard.auth.Auth;
import lombok.extern.slf4j.Slf4j;

import com.lyft.data.baseapp.auth.User;

@Slf4j
@Path("/gateway")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ "ADMIN" })
public class GatewayResource {

  @Inject private GatewayBackendManager gatewayBackendManager;

  @GET
  public Response ok(@Context Request request) {
    return Response.ok("ok").build();
  }

  @GET
  @Path("/backend/all")
  public Response getAllBackends(@Auth User user) {
    return Response.ok(this.gatewayBackendManager.getAllBackends()).build();
  }

  @GET
  @Path("/backend/active")
  public Response getActiveBackends() {
    return Response.ok(gatewayBackendManager.getAllActiveBackends()).build();
  }

  @POST
  @Path("/backend/deactivate/{name}")
  public Response deactivateBackend(@PathParam("name") String name) {
    try {
      this.gatewayBackendManager.deactivateBackend(name);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return throwError(e);
    }
    return Response.ok("ok").build();
  }

  @POST
  @Path("/backend/activate/{name}")
  public Response activateBackend(@PathParam("name") String name) {
    try {
      this.gatewayBackendManager.activateBackend(name);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return throwError(e);
    }
    return Response.ok("ok").build();
  }

  private Response throwError(Exception e) {
    return Response.status(Response.Status.NOT_FOUND)
        .entity(e.getMessage())
        .type("text/plain")
        .build();
  }
}
