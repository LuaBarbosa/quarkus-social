package io.github.luabarbosa.quarkussocial.rest.dto;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse() {
    }

    public FollowerResponse(Followers follower){
        this(new FollowerResponse(follower.getId(), follower.getFollowers().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }



}
