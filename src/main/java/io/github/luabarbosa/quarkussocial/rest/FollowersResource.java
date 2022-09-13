package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.FollowersRepository;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;
import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.FollowersRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowersResource {

    private FollowersRepository repository;
    private UserRepository userRepository;

    @Inject
    public FollowersResource(
            FollowersRepository repository, UserRepository userRepository){

        this.repository = repository;
        this.userRepository = userRepository;
    }
    @PUT
    @Path("{id}")
    @Transactional
    public Response followerUser(
            @PathParam("userId") Long id, FollowersRequest request ){

        var user =  userRepository.findById(id);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(request.getFollowersId());

        var entity = new Followers();
        entity.setUser(user);
        entity.setFollowers(follower);

        repository.persist(entity);

        return Response.status(Response.Status.NO_CONTENT).build();

    }


}
