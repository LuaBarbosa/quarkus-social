package io.github.luabarbosa.quarkussocial.domain.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "followers")
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     @ManyToOne
     @JoinColumn(name = "followers_id")
     private User followers;


    public void setUser(User user) {
    }
}
