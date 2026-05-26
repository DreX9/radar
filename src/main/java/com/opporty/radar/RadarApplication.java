package com.opporty.radar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.opporty.radar.features.auth.roles.Roles;
import com.opporty.radar.features.auth.roles.RolesRepository;
import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.auth.users.UsersRepository;
import com.opporty.radar.features.auth.teachers.Teachers;
import com.opporty.radar.features.auth.teachers.TeachersRepository;

@SpringBootApplication
public class RadarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadarApplication.class, args);
	}

	/**
	 * Seed inicial: crea el rol ADMIN y el usuario administrador si no existen.
	 * Credenciales por defecto:
	 * username : admin
	 * password : Admin1234!
	 */
	@Bean
	CommandLineRunner initDatabase(
			RolesRepository rolesRepository,
			UsersRepository usersRepository,
			TeachersRepository teachersRepository,
			PasswordEncoder passwordEncoder) {

		return args -> {
			try {
				// 1. Crear rol ADMIN si no existe
				Roles adminRole = rolesRepository.findByName("ADMIN")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("ADMIN")
										.description("Administrador del sistema")
										.build()));

				// Intentar migrar/renombrar rol USER a MANAGER si existe
				rolesRepository.findByName("USER").ifPresent(userRole -> {
					userRole.setName("MANAGER");
					userRole.setDescription("Manager de Eventos");
					rolesRepository.save(userRole);
				});

				// 2. Crear rol MANAGER si no existe
				rolesRepository.findByName("MANAGER")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("MANAGER")
										.description("Manager de Eventos")
										.build()));

				// 3. Crear rol STUDENT si no existe

				rolesRepository.findByName("STUDENT")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("STUDENT")
										.description("Estudian te")
										.build()));

				// 4. Crear rol TEACHER si no existe
				rolesRepository.findByName("TEACHER")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("TEACHER")
										.description("Profesor / Docente")
										.build()));

				// 3. Crear usuario admin y asociarlo a un perfil de profesor si no existe
				if (usersRepository.findByUsername("mr01019000").isEmpty()) {
					Users adminUser = Users.builder()
							.username("mr01019000")
							.email("admin@radar.com")
							.password(passwordEncoder.encode("Admin1234!"))
							.enabled(true)
							.role(adminRole)
							.build();
					usersRepository.save(adminUser);

					Teachers adminTeacher = Teachers.builder()
							.nombres("Administrador")
							.apellidos("del Sistema")
							.titulo("Magíster / Director")
							.especialidad("Administración Educativa")
							.telefono("999999999")
							.dni("00000000")
							.fechaNacimiento(java.time.LocalDate.of(1990, 1, 1))
							.biography("Administrador principal del sistema Echo.")
							.status("ACTIVE")
							.hiringDate(java.time.LocalDate.of(2020, 1, 1))
							.user(adminUser)
							.build();
					teachersRepository.save(adminTeacher);
				}
			} catch (Exception e) {
				System.err.println("Advertencia: No se pudo inicializar la base de datos semilla: " + e.getMessage());
			}
		};
	}
}
