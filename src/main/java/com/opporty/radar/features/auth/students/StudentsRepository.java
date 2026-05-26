package com.opporty.radar.features.auth.students;

import org.springframework.data.jpa.repository.JpaRepository;
import com.opporty.radar.features.auth.users.Users;
import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Students, Long> {

    boolean existsByDni(String dni);
    Optional<Students> findByUser(Users user);
    Optional<Students> findByUserUsername(String username);
}


