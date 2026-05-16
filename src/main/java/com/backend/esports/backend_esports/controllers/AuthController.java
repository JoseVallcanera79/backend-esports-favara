package com.backend.esports.backend_esports.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.esports.backend_esports.models.entities.User;
import com.backend.esports.backend_esports.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginData) {

        User user = userRepository
                .findByEmail(loginData.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        // 🔥 correcto
        boolean passwordCorrecta = passwordEncoder.matches(
                loginData.getPassword(),
                user.getPassword()
        );

        if (!passwordCorrecta) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }
}