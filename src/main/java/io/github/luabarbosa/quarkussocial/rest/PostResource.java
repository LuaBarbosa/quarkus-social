package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.Post;
import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.FollowersRepository;
import io.github.luabarbosa.quarkussocial.domain.repository.PostRepository;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;
import io.github.luabarbosa.quarkussocial.rest.dto.CreatePostRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.annotations.Pos;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final UserRepository userRepository;
    private final PostRepository repository;
    private FollowersRepository followersRepository;

    @Inject
    public PostResource (UserRepository userRepository,
                         PostRepository repository,
                         FollowersRepository followersRepository){
        this.userRepository = userRepository;
        this.repository = repository;
        this.followersRepository = followersRepository;
    }

    @POST
    @Transactional
    public Response createPosts(@PathParam("userId") Long userId, CreatePostRequest posts) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setText(posts.getText());
        post.getUser(user);

        repository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(
            @PathParam("userId") Long userId,
            @HeaderParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(followerId == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You forgot the header followerId")
                    .build();
        }

       User follower = userRepository.findById(followerId);
        if(follower == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Inexixtent followerId")
                    .build();
        }

       boolean follows = followersRepository.follows(follower, user);
       if(!follows){
           return Response.status(Response.Status.FORBIDDEN)
                   .entity("You can't see these posts")
                   .build();
       }

        PanacheQuery<Post> query = repository.find(
                "user", Sort.by("dateTime", Sort.Direction.Descending), user);

        var list = query.list();

       var postResponseList =  list.stream()
                .map(post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());
        return Response.ok(postResponseList).build();
    }
}
