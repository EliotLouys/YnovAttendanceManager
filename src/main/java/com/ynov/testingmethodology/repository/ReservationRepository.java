package com.ynov.testingmethodology.repository;

import com.ynov.testingmethodology.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByStudentId(String studentId);
    List<Reservation> findByRoomId(String roomId);
    void delete(Reservation reservation);
    Optional<Reservation> findById(String reservationId);
    List<Reservation> findAfterDate(LocalDateTime date);

    List<Reservation> findBeforeDate(LocalDateTime date);
}
