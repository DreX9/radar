package com.opporty.radar.features.auth.roles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);
}

