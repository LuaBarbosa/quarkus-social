package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest userRequest){
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        user.persist(); //salva entidade no banco de dados

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        return Response.ok().build();
    }
}
