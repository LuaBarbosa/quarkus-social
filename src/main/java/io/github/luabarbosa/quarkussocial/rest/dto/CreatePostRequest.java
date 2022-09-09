package io.github.luabarbosa.quarkussocial.rest.dto;

import io.github.luabarbosa.quarkussocial.domain.model.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class CreatePostRequest {

    private String text;

    private LocalDateTime dateTime;

    private User user;
}
