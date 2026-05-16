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

    // 🔥 REGISTRO
    @Override
    @Transactional
    public User save(User user) {


        Optional<User> existingUser =
            repository.findByEmail(user.getEmail());

    if (existingUser.isPresent()) {
        throw new RuntimeException("El email ya existe");
    }

        user.setPassword(
            passwordEncoder.encode(user.getPassword())
        );

        return repository.save(user);
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

            // 🔥 SOLO si escribe nueva password
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

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
}