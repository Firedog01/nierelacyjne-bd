package pl.lodz.p.edu.rest.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.edu.rest.exception.ClientException;
import pl.lodz.p.edu.rest.managers.ClientManager;
import pl.lodz.p.edu.rest.model.Address;
import pl.lodz.p.edu.rest.model.Client;
import pl.lodz.p.edu.rest.model.UniqueId;
import pl.lodz.p.edu.rest.model.idType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/clients")
public class ClientController {

    @Inject
    private ClientManager clientManager;



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(("/"))
    //FIXME BODY CLIENT OR PARTICLES?
    public Response registerClient(String clientId, idType idtype, String name,
                                 String surname, Address address
    ) throws ClientException {

        Client client = new Client(clientId, idtype, name, surname, address);
// FIXME TRY CATCH? CHANGES REPOSITORY
        try {
            clientManager.registerClient(client);
            return Response.status(Response.Status.CREATED).entity(client).build();
        } catch (RuntimeException e) { //FIXME TEMP
            System.out.println(e.getMessage()); //FIXME TEMP
        }
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(client).build();
        }

    @DELETE
    @Path("/{id}")
    public Response unregisterClient(@PathParam("id") UUID uuid) {
        if (clientManager.unregisterClient(uuid)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/{type}")
    public Response getClientByClientId(@PathParam("id") String clientId, @PathParam("type") idType idType) {
        Client client = clientManager.getByClientId(clientId, idType);
        if(client != null) {
            return Response.status(Response.Status.OK).entity(client).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}")
    public Response getClientByUuid(@PathParam("uuid") UUID uuid) {
        Client client = clientManager.getClientByUuid(new UniqueId(uuid));
        if(client != null) {
            return Response.status(Response.Status.OK).entity(client).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClients() {
        List<Client> clients = clientManager.getAllClients();
        return Response.status(Response.Status.OK).entity(clients).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/available")
    public Response getAllAvailableClients() {
        List<Client> all = clientManager.getAllClients();
        List<Client> available = new ArrayList<>();
        for (Client c : all) {
            if(!(c.isArchive())) {
                available.add(c);
            }
        }
        return Response.status(Response.Status.OK).entity(available).build();
    }
}