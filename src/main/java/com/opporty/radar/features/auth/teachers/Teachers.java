package com.opporty.radar.features.auth.teachers;

import com.opporty.radar.features.auth.users.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Teachers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 150)
    private String titulo;

    @Column(length = 150)
    private String especialidad;

    @Column(length = 9)
    private String telefono;

    @Column(nullable = false, length = 8)
    private String dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "hiring_date")
    private LocalDate hiringDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;
}
