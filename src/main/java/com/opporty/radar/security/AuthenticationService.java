package com.opporty.radar.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.opporty.radar.common.AuthenticationRequest;
import com.opporty.radar.common.AuthenticationResponse;
import com.opporty.radar.common.RefreshTokenRequest;
import com.opporty.radar.common.RegisterRequest;
import com.opporty.radar.features.auth.roles.RolesRepository;
import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.auth.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

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
}
