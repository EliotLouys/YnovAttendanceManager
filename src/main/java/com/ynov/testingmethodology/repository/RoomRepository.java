package repository;

import model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    void save(Room room);
    Optional<Room> findById(String id);
    List<Room> findAll();
    void deleteById(String id);
}
