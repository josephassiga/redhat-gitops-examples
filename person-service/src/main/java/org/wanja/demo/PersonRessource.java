package org.wanja.demo;

import java.util.List;

import org.wanja.entity.Person;

import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonRessource {

    @GET
    public List<Person> getAll() throws Exception {
        return Person.findAll(Sort.ascending("lastName")).list();
    }

    @POST
    @Transactional
    public Response create(Person p) {

        if (p == null || p.id != null) {
            throw new WebApplicationException("id != null");
        }
        p.persist();
        return Response.ok(p).status(200).build();

    }

    @PUT
    @Transactional
    @Path("{id}")
    public Person update(@PathParam("id") Long id, Person p) {

        final Person entity = Person.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Person with id of " + id + " does not exist.", 404);
        }

        if (p.salutation != null)
            entity.salutation = p.salutation;
        if (p.firstName != null)
            entity.firstName = p.firstName;
        if (p.lastName != null)
            entity.lastName = p.lastName;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {

        final Person entity = Person.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Person with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }
}
