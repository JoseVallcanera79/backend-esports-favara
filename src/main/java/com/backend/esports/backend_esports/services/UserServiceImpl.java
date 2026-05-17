package com.backend.esports.backend_esports.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.esports.backend_esports.models.entities.User;
import com.backend.esports.backend_esports.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    // 🔥 REGISTRO CON VERIFICACIÓN
    @Override
    @Transactional
    public User save(User user) {

        Optional<User> existingUser =
            repository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("El email ya existe");
        }

        // 🔐 password encriptada
        user.setPassword(
            passwordEncoder.encode(user.getPassword())
        );

        // 🔒 usuario NO verificado aún
        user.setEnabled(false);

        User savedUser = repository.save(user);

        // 📩 ENVIAR EMAIL (NO ROMPE REGISTRO SI FALLA)
        try {
            emailService.enviarCorreo(savedUser.getEmail());
        } catch (Exception e) {
            System.out.println("Error enviando email: " + e.getMessage());
        }

        return savedUser;
    }

    // 🔥 EDITAR PERFIL
    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {

        Optional<User> o = repository.findById(id);

        if (o.isPresent()) {

            User userDB = o.get();

            userDB.setNombre(user.getNombre());
            userDB.setApellido(user.getApellido());
            userDB.setEmail(user.getEmail());
            userDB.setFoto(user.getFoto());

            if (
                user.getPassword() != null &&
                !user.getPassword().isBlank()
            ) {
                userDB.setPassword(
                    passwordEncoder.encode(user.getPassword())
                );
            }

            return Optional.of(
                repository.save(userDB)
            );
        }

        return Optional.empty();
    }

    // 🔥 BORRAR USUARIO
    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    // 🔥 ACTIVAR USUARIO (VERIFICACIÓN EMAIL)
    @Transactional
    public void enableUser(String email) {

        User user = repository.findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("Usuario no encontrado")
            );

        user.setEnabled(true);

        repository.save(user);
    }
}