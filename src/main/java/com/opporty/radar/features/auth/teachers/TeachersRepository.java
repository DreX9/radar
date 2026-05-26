package com.opporty.radar.features.auth.teachers;

import org.springframework.data.jpa.repository.JpaRepository;
import com.opporty.radar.features.auth.users.Users;
import java.util.Optional;

public interface TeachersRepository extends JpaRepository<Teachers, Long> {

    boolean existsByDni(String dni);
    Optional<Teachers> findByUser(Users user);
    Optional<Teachers> findByUserUsername(String username);
}


