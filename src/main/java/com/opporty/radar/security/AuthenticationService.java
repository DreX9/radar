package com.opporty.radar.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.opporty.radar.common.AuthenticationRequest;
import com.opporty.radar.common.AuthenticationResponse;
import com.opporty.radar.common.RefreshTokenRequest;
import com.opporty.radar.common.RegisterRequest;
import com.opporty.radar.common.StudentRegisterRequest;
import com.opporty.radar.common.TeacherRegisterRequest;
import com.opporty.radar.features.auth.roles.RolesRepository;
import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.auth.users.UsersRepository;
import com.opporty.radar.features.auth.students.Students;
import com.opporty.radar.features.auth.students.StudentsRepository;
import com.opporty.radar.features.auth.students.StudentsViewDTO;
import com.opporty.radar.features.auth.students.StudentsMapper;
import com.opporty.radar.features.auth.teachers.Teachers;
import com.opporty.radar.features.auth.teachers.TeachersRepository;
import com.opporty.radar.features.auth.teachers.TeachersViewDTO;
import com.opporty.radar.features.auth.teachers.TeachersMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentsRepository studentsRepository;
    private final TeachersRepository teachersRepository;
    private final TeachersMapper teachersMapper;
    private final StudentsMapper studentsMapper;

    public AuthenticationResponse register(RegisterRequest request) {

        if (usersRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya está registrado");
        }

        if (usersRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado");
        }

        // 1. Obtener el rol
        var role = rolesRepository.findById(request.roleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + request.roleId()));

        // 2. Crear usuario
        var user = Users.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .role(role)
                .build();

        usersRepository.save(user);

        // 3. Generar UserDetails
        var userDetails = new CustomUserDetail(user);

        // 4. Generar tokens
        var token = jwtService.generateToken(userDetails);
        var refresh = jwtService.generateRefreshToken(userDetails);

        return new AuthenticationResponse(token, refresh);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // 1. Validar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Buscar usuario
        var user = usersRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var userDetails = new CustomUserDetail(user);

        // 3. Generar tokens
        var token = jwtService.generateToken(userDetails);
        var refresh = jwtService.generateRefreshToken(userDetails);

        return new AuthenticationResponse(token, refresh);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        var username = jwtService.extractUsername(request.refreshToken());

        var user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var userDetails = new CustomUserDetail(user);

        if (jwtService.isTokenValid(request.refreshToken(), userDetails)) {

            var newAccessToken = jwtService.generateToken(userDetails);

            return new AuthenticationResponse(
                    newAccessToken,
                    request.refreshToken()
            );
        }

        throw new RuntimeException("Token de refresco inválido");
    }

    @Transactional
    public StudentsViewDTO registerStudent(StudentRegisterRequest request) {
        if (usersRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado");
        }

        if (studentsRepository.existsByCodigo(request.codigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código de estudiante ya está registrado");
        }

        if (studentsRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI ya está registrado");
        }

        // Auto-generación del nombre de usuario
        String username = generateUniqueUsername("std", request.dni(), request.fechaNacimiento());

        var role = rolesRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Rol STUDENT no encontrado"));

        var user = Users.builder()
                .username(username)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .role(role)
                .build();
        usersRepository.save(user);

        var student = Students.builder()
                .codigo(request.codigo())
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .carrera(request.carrera())
                .ciclo(request.ciclo())
                .dni(request.dni())
                .fechaNacimiento(request.fechaNacimiento())
                .user(user)
                .build();
        studentsRepository.save(student);

        return studentsMapper.toDt(student);
    }

    @Transactional
    public TeachersViewDTO registerTeacher(TeacherRegisterRequest request) {
        if (usersRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado");
        }

        if (teachersRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI ya está registrado");
        }

        // Auto-generación del nombre de usuario
        String username = generateUniqueUsername("mr", request.dni(), request.fechaNacimiento());

        var role = rolesRepository.findById(request.roleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.roleId()));

        var user = Users.builder()
                .username(username)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .role(role)
                .build();
        usersRepository.save(user);

        var teacher = Teachers.builder()
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .titulo(request.titulo())
                .especialidad(request.especialidad())
                .telefono(request.telefono())
                .dni(request.dni())
                .fechaNacimiento(request.fechaNacimiento())
                .user(user)
                .build();
        teachersRepository.save(teacher);

        return teachersMapper.toDt(teacher);
    }

    private String generateUniqueUsername(String prefix, String dni, java.time.LocalDate dob) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("ddMMyy");
        String formattedDate = dob.format(formatter);
        String lastTwoDni = dni.length() >= 2 ? dni.substring(dni.length() - 2) : dni;
        String baseUsername = prefix + formattedDate + lastTwoDni;

        String username = baseUsername;
        int counter = 1;
        while (usersRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
    }
}
