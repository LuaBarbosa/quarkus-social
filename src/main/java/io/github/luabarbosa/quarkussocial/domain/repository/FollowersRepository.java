package io.github.luabarbosa.quarkussocial.domain.repository;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import io.github.luabarbosa.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FollowersRepository implements PanacheRepository<Followers> {

    public boolean follows(User follower, User user){
        var params = Parameters.with("follower", follower)
                .and("user", user).map();

        PanacheQuery<Followers> query = find("follower =:follower and user =:user", params);
        Optional<Followers> result = query.firstResultOptional();

        return result.isPresent();

    }

    public List<Followers> findByUser(Long id){
        PanacheQuery<Followers> query = find("user.id", id);
        return query.list();
    }
}
