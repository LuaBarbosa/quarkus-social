package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;
import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }
    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest){
        Set<ConstraintViolation<CreateUserRequest>> validate = validator.validate(userRequest);
        if(!validate.isEmpty()){
           return ResponseError
                   .createFromValidation(validate)
                   .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user); //salva entidade no banco de dados

        return Response.status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> all = repository.findAll();
        return Response.ok(all.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
        User user = repository.findById(id);
        if(user != null){
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData ){
        User user = repository.findById(id);
        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }



}
