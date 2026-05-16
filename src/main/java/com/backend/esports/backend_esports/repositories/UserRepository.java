package com.backend.esports.backend_esports.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.backend.esports.backend_esports.models.entities.User;

public interface UserRepository 
extends CrudRepository<User, Long>{

        Optional<User> findByEmail(String email);

}
