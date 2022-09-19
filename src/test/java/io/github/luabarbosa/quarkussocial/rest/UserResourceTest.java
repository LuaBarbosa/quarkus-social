package io.github.luabarbosa.quarkussocial.rest;

import io.github.luabarbosa.quarkussocial.rest.dto.CreateUserRequest;
import io.github.luabarbosa.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("should create an user successfully")
    public void createUserTest(){
        var user = new CreateUserRequest();
        user.setName("Ana");
        user.setAge(30);

        Response response;
        response = (Response) given()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post("/users")
                .then()
                    .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("should return error when json is not valid")
    public void createUserValidationErrorTest(){
            var user = new CreateUserRequest();
            user.setName(null);
            user.setAge(null);

            Response response =
                    given()
                        .contentType(ContentType.JSON)
                        .body(user)
                    .when()
                        .post("/users")
                    .then()
                        .extract().response();

            assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
            assertEquals("Validator Error", response.jsonPath().getString("message"));

        }
}