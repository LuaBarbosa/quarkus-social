package io.github.luabarbosa.quarkussocial.rest.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    String name;
    @NotNull(message = "Age is required")
    Integer age;

}
