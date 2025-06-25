package com.ynov.testingmethodology.repository;

import com.ynov.testingmethodology.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findById(String id);
    List<Room> findAll();
    void deleteById(String id);
    boolean existsByID(String id);

}
