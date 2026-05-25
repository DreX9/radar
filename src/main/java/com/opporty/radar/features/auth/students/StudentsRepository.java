package com.opporty.radar.features.auth.students;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Students, Long> {

    boolean existsByDni(String dni);
}
