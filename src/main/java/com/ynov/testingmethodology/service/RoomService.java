package com.ynov.testingmethodology.service;


import model.Room;
import com.ynov.testingmethodology.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoomService {

    private final RoomRepository roomRepo;

    @Autowired
    public RoomService(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room addRoom(Room room) {
        verify(room);

        if (roomRepo.existsByID(room.getId())) {
            throw new IllegalArgumentException("Room already exists with id "+room.getId());
        }

        return roomRepo.save(room);
    }

    public void deleteRoom(String id) {
        if (!roomRepo.existsByID(id)) {
            throw new IllegalArgumentException("Room does not exists with id :" + id);
        }

        roomRepo.deleteById(id);
    }

    public Optional<Room> getRoomById(String id) {
        if (id ==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Room id is null or empty");
        }
        return roomRepo.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    private void verify(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Room id is null or empty");
        }
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is null or empty");
        }
        if (room.getCapacity() == null || room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive integer");
        }
    }

}
