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

@SpringBootApplication
public class RadarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadarApplication.class, args);
	}

	/**
	 * Seed inicial: crea el rol ADMIN y el usuario administrador si no existen.
	 * Credenciales por defecto:
	 *   username : admin
	 *   password : Admin1234!
	 */
	@Bean
	CommandLineRunner initDatabase(
			RolesRepository rolesRepository,
			UsersRepository usersRepository,
			PasswordEncoder passwordEncoder) {

		return args -> {
			try {
				// 1. Crear rol ADMIN si no existe
				Roles adminRole = rolesRepository.findByName("ADMIN")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("ADMIN")
										.description("Administrador del sistema")
										.build()
						));

				// 2. Crear rol USER si no existe
				rolesRepository.findByName("USER")
						.orElseGet(() -> rolesRepository.save(
								Roles.builder()
										.name("USER")
										.description("Usuario estándar")
										.build()
						));

				// 3. Crear usuario admin si no existe
				if (usersRepository.findByUsername("admin").isEmpty()) {
					Users admin = Users.builder()
							.username("admin")
							.email("admin@radar.com")
							.password(passwordEncoder.encode("Admin1234!"))
							.enabled(true)
							.role(adminRole)
							.build();
					usersRepository.save(admin);
				}
			} catch (Exception e) {
				System.err.println("Advertencia: No se pudo inicializar la base de datos semilla: " + e.getMessage());
			}
		};
	}
}

