package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.github.luabarbosa.quarkussocial.domain.repository.FollowersRepository;
import io.github.luabarbosa.quarkussocial.domain.repository.UserRepository;
import io.github.luabarbosa.quarkussocial.rest.dto.CreatePostRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowersRepository followersRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp(){
        var user = new User();
        user.setAge(30);
        user.setName("Maria");
        userRepository.persist(user);
        userId = user.getId();


        var userNotFollower = new User();
        userNotFollower.setAge(33);
        userNotFollower.setName("Beatriz");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        var userFollower = new User();
        userFollower.setAge(21);
        userFollower.setName("Camila");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Followers follower = new Followers();
        follower.setUser(user);
        follower.setFollowers(userFollower);
        followersRepository.persist(follower);


    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest(){
        var post = new CreatePostRequest();
        post.setText("some text");

        var userId =1;

                given()
                    .contentType(ContentType.JSON)
                    .body(post)
                    .pathParams("userId", userId)
                .when()
                    .post()
                .then()
                    .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void postForAnInexistentUserTest(){
        var post = new CreatePostRequest();
        post.setText("some text");

                given()
                    .contentType(ContentType.JSON)
                    .body(post)
                    .pathParams("userId", 999)
                .when()
                    .post()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest(){
             given()
                    .pathParams("userId", 999)
                .when()
                    .get()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest(){
                given()
                    .pathParams("userId", 999)
                .when()
                    .get()
                .then()
                    .statusCode(404)
                        .body(Matchers.is("You forgot the header  followerId"));
    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    public void listPostFollowerNotFoundTest(){
                    given()
                        .pathParams("userId", 999)
                        .header("followerId", 999)
                    .when()
                        .get()
                    .then()
                        .statusCode(404)
                        .body(Matchers.is("Inexistent followerId"));
    }

    @Test
    @DisplayName("should return 403 when follower isn't follower")
    public void listPostNotAFollower(){
                given()
                    .pathParams("userId", 999)
                    .header("followerId", userNotFollowerId)
                .when()
                    .get()
                .then()
                    .statusCode(403)
                    .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest(){
        given()
          .pathParams("userId", 999)
          .header("followerId", userFollowerId)
        .when()
           .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(0));
    }
}