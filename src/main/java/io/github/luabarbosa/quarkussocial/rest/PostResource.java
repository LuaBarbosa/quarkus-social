package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final UserRepository userRepository;

    @Inject
    public PostResource (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @POST
    @Transactional
    public Response createPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return null;
    }

    @GET
    public Response listPosts(){
        return Response.ok().build();
    }
    @DELETE
    public Response deletePost(){
        return null;
    }

    @PUT
    public Response updatePosts(){
        return null;
    }
}
