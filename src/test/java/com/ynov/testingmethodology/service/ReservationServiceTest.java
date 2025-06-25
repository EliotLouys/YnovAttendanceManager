package com.ynov.testingmethodology.service;

import com.ynov.testingmethodology.model.Reservation;
import com.ynov.testingmethodology.model.Room;
import com.ynov.testingmethodology.model.Student;
import com.ynov.testingmethodology.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Reservation Service Unit Tests")
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepo;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation sampleReservation() {
        Student s1 = new Student("s1", "John", "Doe");
        Room room = new Room("r1", "Room A", 30);
        return new Reservation("res1", Arrays.asList(s1), room,
                LocalDateTime.of(2025, 6, 25, 10, 0),
                LocalDateTime.of(2025, 6, 25, 12, 0));
    }

    @Nested
    @DisplayName("createReservation Tests")
    class CreateReservationTests {
        @Test
        @DisplayName("Nominal case - should save and return reservation")
        void createNominal() {
            Reservation res = sampleReservation();
            doNothing().when(reservationRepo).save(res);

            Reservation result = reservationService.createReservation(res);

            assertEquals(res, result);
            verify(reservationRepo).save(res);
        }

        @Test
        @DisplayName("Null reservation - should throw IllegalArgumentException")
        void createNull() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(null));
        }

        @Test
        @DisplayName("Invalid id - should throw IllegalArgumentException")
        void createInvalidId() {
            Reservation res = sampleReservation();
            res.setId("  ");
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null room - should throw IllegalArgumentException")
        void createNullRoom() {
            Reservation res = sampleReservation();
            res.setRoom(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null start or end time - should throw IllegalArgumentException")
        void createNullTimes() {
            Reservation res = sampleReservation();
            res.setStartTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
            res.setStartTime(LocalDateTime.now());
            res.setEndTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("End time before or equal to start time - should throw IllegalArgumentException")
        void createInvalidTimes() {
            Reservation res = sampleReservation();
            LocalDateTime start = LocalDateTime.of(2025, 6, 25, 10, 0);
            res.setStartTime(start);
            res.setEndTime(start);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
            res.setEndTime(start.minusHours(1));
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }
    }

    @Nested
    @DisplayName("updateReservation Tests")
    class UpdateReservationTests {
        @Test
        @DisplayName("Nominal case - should update and return reservation")
        void updateNominal() {
            Reservation res = sampleReservation();
            when(reservationRepo.findAll()).thenReturn(Arrays.asList(res));
            doNothing().when(reservationRepo).save(res);

            Reservation result = reservationService.updateReservation(res);

            assertEquals(res, result);
            verify(reservationRepo).save(res);
        }

        @Test
        @DisplayName("Null or empty id - should throw IllegalArgumentException")
        void updateInvalidId() {
            Reservation res = sampleReservation();
            res.setId(" ");
            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }

        @Test
        @DisplayName("Non-existing reservation - should throw IllegalArgumentException")
        void updateNonExisting() {
            Reservation res = sampleReservation();
            when(reservationRepo.findAll()).thenReturn(Collections.emptyList());

            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }
    }

    @Nested
    @DisplayName("getAllReservations Tests")
    class GetAllTests {
        @Test
        @DisplayName("Should return all reservations")
        void getAll() {
            Reservation res = sampleReservation();
            List<Reservation> list = Arrays.asList(res);
            when(reservationRepo.findAll()).thenReturn(list);

            List<Reservation> result = reservationService.getAllReservations();
            assertEquals(list, result);
        }
    }

    @Nested
    @DisplayName("getReservationsByStudent Tests")
    class GetByStudentTests {
        @Test
        @DisplayName("Nominal case - should return list")
        void getByStudentNominal() {
            String studentId = "s1";
            Reservation res = sampleReservation();
            when(reservationRepo.findByStudentId(studentId)).thenReturn(Arrays.asList(res));

            List<Reservation> result = reservationService.getReservationsByStudent(studentId);
            assertEquals(Arrays.asList(res), result);
        }

        @Test
        @DisplayName("Null or empty studentId - should throw IllegalArgumentException")
        void getByStudentInvalidId() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByStudent(null));
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByStudent("  "));
        }
    }

    @Nested
    @DisplayName("getReservationsByRoom Tests")
    class GetByRoomTests {
        @Test
        @DisplayName("Nominal case - should return list")
        void getByRoomNominal() {
            String roomId = "r1";
            Reservation res = sampleReservation();
            when(reservationRepo.findByRoomId(roomId)).thenReturn(Arrays.asList(res));

            List<Reservation> result = reservationService.getReservationsByRoom(roomId);
            assertEquals(Arrays.asList(res), result);
        }

        @Test
        @DisplayName("Null or empty roomId - should throw IllegalArgumentException")
        void getByRoomInvalidId() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByRoom(null));
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByRoom("  "));
        }
    }

    @Nested
    @DisplayName("deleteReservation Tests")
    class DeleteTests {
        @Test
        @DisplayName("Nominal case - should delete without exception")
        void deleteNominal() {
            String id = "res1";
            Reservation res = sampleReservation();
            when(reservationRepo.findAll()).thenReturn(Arrays.asList(res));

            assertDoesNotThrow(() -> reservationService.deleteReservation(id));
            verify(reservationRepo).delete(res);
        }

        @Test
        @DisplayName("Null or empty id - should throw IllegalArgumentException")
        void deleteInvalidId() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation(null));
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation("  "));
        }

        @Test
        @DisplayName("Non-existing reservation - should throw IllegalArgumentException")
        void deleteNonExisting() {
            when(reservationRepo.findAll()).thenReturn(Collections.emptyList());
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation("resX"));
        }
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {
        @Test
        @DisplayName("Nominal case - should return Optional with reservation")
        void findByIdNominal() {
            Reservation res = sampleReservation();
            when(reservationRepo.findAll()).thenReturn(Arrays.asList(res));

            Optional<Reservation> opt = reservationService.findById("res1");
            assertTrue(opt.isPresent());
            assertEquals(res, opt.get());
        }

        @Test
        @DisplayName("Not found - should return empty Optional")
        void findByIdNotFound() {
            when(reservationRepo.findAll()).thenReturn(Collections.emptyList());

            Optional<Reservation> opt = reservationService.findById("resX");
            assertFalse(opt.isPresent());
        }

        @Test
        @DisplayName("Null or empty id - should throw IllegalArgumentException")
        void findByIdInvalid() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.findById(null));
            assertThrows(IllegalArgumentException.class, () -> reservationService.findById("  "));
        }
    }
}
