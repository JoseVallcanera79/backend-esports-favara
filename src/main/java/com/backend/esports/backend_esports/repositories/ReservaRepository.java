package com.backend.esports.backend_esports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.esports.backend_esports.models.entities.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}