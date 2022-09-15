package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.FollowersRepository;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;
import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.FollowerResponse;
import io.github.luabarbosa.quarkussocial.rest.dto.FollowersPerUserResponse;
import io.github.luabarbosa.quarkussocial.rest.dto.FollowersRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

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

        if(id.equals(request.getFollowersId())){
            return Response.status(Response.Status.CONFLICT).build();
        }
        var user =  userRepository.findById(id);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var follower = userRepository.findById(request.getFollowersId());

        boolean follows = repository.follows(follower, user);
        if(!follows){
            var entity = new Followers();
            entity.setUser(user);
            entity.setFollowers(follower);

            repository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();

    }
    @GET
    public Response listFollowers(@PathParam("id") Long id){

        var user =  userRepository.findById(id);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = repository.findByUser(id);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList = list.stream()
                .map(FollowerResponse :: new)
                .collect(Collectors.toList());

        responseObject.setContent(followerList);
        return Response.ok(responseObject).build();

    }
    @DELETE
    @Transactional
    public Response unfollowUser(
            @PathParam("userId") Long userId,
            @QueryParam("followerId") Long followerId){
        var user =  userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repository.deleteByFollowerAndUser(followerId, userId);

        return  Response.status(Response.Status.NO_CONTENT).build();
    }

}
