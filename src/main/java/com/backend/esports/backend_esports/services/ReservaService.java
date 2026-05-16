package com.backend.esports.backend_esports.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.esports.backend_esports.models.entities.Reserva;
import com.backend.esports.backend_esports.repositories.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // Obtener todas
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    // Guardar reserva
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Eliminar reserva
    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }

    // 🔥 ELIMINAR RESERVAS PASADAS 90 MIN
    @Scheduled(fixedRate = 60000)
    public void eliminarReservasExpiradas() {

        List<Reserva> reservas = reservaRepository.findAll();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Reserva reserva : reservas) {

            try {

                LocalDate fecha =
                        LocalDate.parse(
                                reserva.getFecha(),
                                formatter
                        );

                LocalTime hora =
                        LocalTime.parse(reserva.getHora());

                LocalDateTime fechaHoraReserva =
                        LocalDateTime.of(fecha, hora);

                // +90 minutos
                LocalDateTime expiracion =
                        fechaHoraReserva.plusMinutes(90);

                if (LocalDateTime.now().isAfter(expiracion)) {

                    reservaRepository.deleteById(
                            reserva.getId()
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error procesando reserva: "
                                + reserva.getId()
                );
            }
        }
    }

}