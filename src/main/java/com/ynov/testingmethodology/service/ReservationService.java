package com.ynov.testingmethodology.service;

import com.ynov.testingmethodology.model.Reservation;
import com.ynov.testingmethodology.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepo;

    @Autowired
    public ReservationService(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    /**
     * Crée une nouvelle réservation après validation
     */
    public Reservation createReservation(Reservation reservation) {
        validate(reservation);
        if (reservation.getStartTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Start and end times can't be before now");
        }
        return reservationRepo.save(reservation);

    }

    /**
     * Met à jour une réservation existante
     */
    public Reservation updateReservation(Reservation reservation) {
        if (reservation == null || reservation.getId() == null || reservation.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation id is required for update");
        }
        validate(reservation);
        // On peut contrôler que la réservation existe
        Optional<Reservation> existing = reservationRepo.findById(reservation.getId());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Reservation does not exist with id: " + reservation.getId());
        }
        return reservationRepo.save(reservation);
    }

    /**
     * Retourne toutes les réservations
     */
    public List<Reservation> getAllReservations() {
        return reservationRepo.findAll();
    }

    /**
     * Recherche les réservations d'un étudiant
     */
    public List<Reservation> getReservationsByStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student id is required");
        }
        return reservationRepo.findByStudentId(studentId);
    }

    /**
     * Recherche les réservations d'une salle
     */
    public List<Reservation> getReservationsByRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room id is required");
        }
        return reservationRepo.findByRoomId(roomId);
    }

    /**
     * Supprime une réservation par son id
     */
    public void deleteReservation(String reservationId) {
        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        Optional<Reservation> existing = reservationRepo.findById(reservationId);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Reservation does not exist with id: " + reservationId);
        }
        reservationRepo.delete(existing.get());
    }

    /**
     * Retourne les réservations à venir
     */
    public List<Reservation> getUpcomingReservations() {
        return reservationRepo.findAfterDate(LocalDateTime.now());
    }

    /**
     * Retourne les réservations passées 
     */
    public List<Reservation> getPastReservations() {
        return reservationRepo.findBeforeDate(LocalDateTime.now());
    }


    /**
     * Valide les données d'une réservation
     */
    private void validate(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is null");
        }
        if (reservation.getId() == null || reservation.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation id is required");
        }
        if (reservation.getRoom() == null) {
            throw new IllegalArgumentException("Room is required");
        }
        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }
        if (end.isBefore(start) || end.equals(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
}
