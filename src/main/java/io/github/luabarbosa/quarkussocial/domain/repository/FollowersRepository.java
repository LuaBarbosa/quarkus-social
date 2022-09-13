package io.github.luabarbosa.quarkussocial.domain.repository;

import io.github.luabarbosa.quarkussocial.domain.model.Followers;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowersRepository implements PanacheRepository<Followers> {
}
