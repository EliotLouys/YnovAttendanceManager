package com.ynov.testingmethodology.service;


import com.ynov.testingmethodology.repository.RoomRepository;
import com.ynov.testingmethodology.model.Room;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;
import com.ynov.testingmethodology.model.Student;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.google.common.base.Verify;

import javax.swing.text.html.Option;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Room Service Unit Tests")
public class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private RoomService roomService;

    @Nested
    @DisplayName("add room tests")
    class AddRoomTests {
        @Test
        @DisplayName("Nominal case - should return a room")
        void addRoomTest(){
            //Given
            Room room = new Room("1","Salle 101",20);
            when(roomRepository.existsByID(room.getId())).thenReturn(false);
            when(roomRepository.save(room)).thenReturn(room);

            //When
            Room res = roomService.addRoom(room);

            //Then
            assertEquals(room,res);
        }

        @Test
        @DisplayName("Room already exists - should throw illegal arg exception")
        void addRoomRoomAlreadyExistsTest(){
            //Given
            Room room = new Room("1","Salle 101",20);
            when(roomRepository.existsByID(room.getId())).thenReturn(true);

            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room is null - should throw illegal arg exception")
        void addRoomRoomNullTest(){
            //Given
            Room room = null;
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room null id - should throw illegal arg exception")
        void addRoomRoomNullIdTest(){
            //Given
            Room room = new Room(null,"Salle 101",20);
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room empty id - should throw illegal arg exception")
        void addRoomRoomEmptyIdTest(){
            //Given
            Room room = new Room("  ","Salle 101",20);
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room empty name - should throw illegal arg exception")
        void addRoomRoomEmptyNameTest(){
            //Given
            Room room = new Room("1","  ",20);

            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room null name - should throw illegal arg exception")
        void addRoomRoomNullNameTest(){
            //Given
            Room room = new Room("1",null,20);
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room null capacity - should throw illegal arg exception")
        void addRoomRoomNullCapacityTest(){
            //Given
            Room room = new Room("1","Salle 101",null);
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }

        @Test
        @DisplayName("Room negative capacity - should throw illegal arg exception")
        void addRoomRoomNegativeCapacityTest(){
            //Given
            Room room = new Room("1","Salle 101",-1);
            //Then
            assertThrows(IllegalArgumentException.class,()->roomService.addRoom(room));
        }
    }

    @Nested
    @DisplayName("Delete room tests")
    class DeleteRoomTests {
        @Test
        @DisplayName("Nominal case")
        public void deleteRoomTestNominalCase(){
            //Given
            String id = "1";
            when(roomRepository.existsByID(id)).thenReturn(true);

            //When
            assertDoesNotThrow(()->{roomService.deleteRoom(id);});

            //Then
            verify(roomRepository).existsByID(id);
            verify(roomRepository).deleteById(id);
        }

        @Test
        @DisplayName("Non existant Room - should throww illegal argument exception")
        public void deleteStudentTestNonExistantRoom(){
            //Given
            String id = "1";
            when(roomRepository.existsByID(id)).thenReturn(false);

            //When
            assertThrows(IllegalArgumentException.class,()->{roomService.deleteRoom(id);});

        }
    }

    @Nested
    @DisplayName("Get room by ID tests")
    class GetRoomByIdTests {
        @Test
        @DisplayName("Nominal case - should return a room")
        public void getRoomTestNominalCase(){
            //Given0
            String id = "1";
            Room room = new Room("1","Salle 101",20);
            when(roomRepository.findById(id)).thenReturn(Optional.of(room));
            //When
            Optional<Room> res = roomService.getRoomById(id);
            //Then
            assertEquals(Optional.of(room),res);
        }

        @Test
        @DisplayName("null id - should throw illegal aragument exception")
        public void getRoomNullIdTest() {
            //Given
            String id = null;
            //then
            assertThrows(IllegalArgumentException.class,()->{roomService.getRoomById(id);});
        }

        @Test
        @DisplayName("empty id - should throw illegal aragument exception")
        public void getRoomEmptyIdTest() {
            //Given
            String id = "  ";
            //then
            assertThrows(IllegalArgumentException.class,()->{roomService.getRoomById(id);});
        }
    }

    @Nested
    @DisplayName("Get all rooms test")
    class GetAllRoomsTests {
        @Test
        @DisplayName("Nominal case - should return list of rooms")
        public void getAllRoomsTestNominalCase(){
            //Given
            Room room1 = new Room("1","Salle 101",20);
            Room room2 = new Room("2","Salle 102",30);
            List<Room> rooms = Arrays.asList(room1,room2);
            when(roomRepository.findAll()).thenReturn(rooms);
            //When
            List<Room> res = roomService.getAllRooms();
            //Then
            assertEquals(rooms,res);

        }
    }



}
