package com.ynov.testingmethodology.repository;

import com.ynov.testingmethodology.model.Reservation;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByStudentId(String studentId);
    List<Reservation> findByRoomId(String roomId);
    Reservation delete(Reservation reservation);
}
