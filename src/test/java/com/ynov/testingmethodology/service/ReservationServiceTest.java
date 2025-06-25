package com.ynov.testingmethodology.service;

import com.ynov.testingmethodology.model.Reservation;
import com.ynov.testingmethodology.model.Room;
import com.ynov.testingmethodology.model.Student;
import com.ynov.testingmethodology.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Student s1;
    private Student s2;
    private Room room;
    private Room room2;

    private Reservation testReservation;
    private Reservation savedReservation;

    @BeforeEach
    void setUp() {
        // Create test data before each test
        s1 = new Student("s1", "John", "Doe");
        s2 = new Student("s2", "Jane", "Smith");
        room2 = new Room("r1", "Room A", 30);
        room = new Room("r2", "Room B", 40);

        testReservation = new Reservation("res1", Arrays.asList(s1), room,
                LocalDateTime.of(2025, 6, 25, 10, 0),
                LocalDateTime.of(2025, 6, 25, 12, 0));


        savedReservation = new Reservation("res2", Arrays.asList(s1,s2), room2,
                LocalDateTime.of(2025, 7, 15, 10, 0),
                LocalDateTime.of(2025, 7, 15, 12, 0));
    }

    @Nested
    @DisplayName("createReservation Tests")
    class CreateReservationTests {
        @Test
        @DisplayName("Should save and return reservation")
        void createReservation_withValidReservation_shouldReturnReservation() {
            when(reservationRepo.save(testReservation)).thenReturn(testReservation);

            Reservation result = reservationService.createReservation(new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0)));

            assertEquals(testReservation, result);
            verify(reservationRepo).save(testReservation);
        }

        @Test
        @DisplayName("should throw IllegalArgumentException")
        void createReservation_withNull_ShouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(null));
        }

        @Test
        @DisplayName("Invalid id - should throw IllegalArgumentException")
        void createReservation_withInvalidId_ShouldThrowIllegalArgumentException() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            res.setId("  ");
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null id - should throw IllegalArgumentException")
        void createReservation_withNullId_ShouldThrowIllegalArgumentException() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            res.setId(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null room - should throw IllegalArgumentException")
        void createReservation_withNullRoom_ShouldThrowIllegalArgumentException() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            res.setRoom(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null start or end time - should throw IllegalArgumentException")
        void createReservation_withNullStartEndTime_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When and then
            res.setStartTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));

            res.setStartTime(LocalDateTime.now());
            res.setEndTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("End time before or equal to start time - should throw IllegalArgumentException")
        void createReservation_withEndTimeBeforeStartTime_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When
            LocalDateTime start = LocalDateTime.of(2025, 6, 25, 10, 0);
            res.setStartTime(start);
            res.setEndTime(start.minusHours(1));

            // Then
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("End time before or equal to start time - should throw IllegalArgumentException")
        void createReservation_withEndTimeEqualStartTime_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When
            LocalDateTime start = LocalDateTime.of(2025, 6, 25, 10, 0);
            res.setStartTime(start);
            res.setEndTime(start);

            // Then
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }
    }

    @Nested
    @DisplayName("updateReservation Tests")
    class UpdateReservationTests {
        @Test
        @DisplayName("Nominal case - should update and return reservation")
        void updateReservation_withValidDate_ShouldReturnUpdatedReservation() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            when(reservationRepo.findAll()).thenReturn(Arrays.asList(res));
            when(reservationRepo.save(res)).thenReturn(res);

            // When
            Reservation result = reservationService.updateReservation(res);

            // Then
            assertEquals(res, result);
            verify(reservationRepo).save(res);
        }

        @Test
        @DisplayName("Null or empty id - should throw IllegalArgumentException")
        void updateReservation_withInvalidId_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When
            res.setId(" ");

            // Then
            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }

        @Test
        @DisplayName("Non-existing reservation - should throw IllegalArgumentException")
        void updateReservation_withNonExistingReservation_ShouldThrowIllegalArgumentException() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            when(reservationRepo.findAll()).thenReturn(Collections.emptyList());

            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }
    }

    @Nested
    @DisplayName("getAllReservations Tests")
    class GetAllTests {
        @Test
        @DisplayName("Should return all reservations")
        void getAllReservations_shouldReturnAllReservations() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
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
