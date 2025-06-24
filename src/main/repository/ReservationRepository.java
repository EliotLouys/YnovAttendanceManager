package repository;

import model.Reservation;
import java.util.List;

public interface ReservationRepository {
    void save(Reservation reservation);
    List<Reservation> findAll();
    List<Reservation> findByStudentId(String studentId);
    List<Reservation> findByRoomId(String roomId);
    void delete(Reservation reservation);
}
