package com.backend.esports.backend_esports.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.esports.backend_esports.models.entities.Reserva;
import com.backend.esports.backend_esports.services.ReservaService;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Obtener reservas
    @GetMapping
    public List<Reserva> listar() {
        return reservaService.findAll();
    }

    // Crear reserva
    @PostMapping
    public Reserva crear(@RequestBody Reserva reserva) {
    return reservaService.save(reserva);
}

    // Eliminar reserva
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reservaService.delete(id);
    }
}