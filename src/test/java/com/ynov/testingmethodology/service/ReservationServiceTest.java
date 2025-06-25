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
import org.mockito.MockedStatic;
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
        @DisplayName("Valid reservation - should save and return reservation")
        void createReservation_withValidReservation_shouldReturnReservation() {
            //Given
            Reservation res1 = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            when(reservationRepo.save(res1)).thenReturn(res1);

            Reservation result = reservationService.createReservation(res1);
            assertEquals(res1, result);
            verify(reservationRepo).save(res1);
        }

        @Test
        @DisplayName("Null reservation - should throw IllegalArgumentException")
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
        void createReservation_withNullStartTime_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When and then
            res.setStartTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("Null start or end time - should throw IllegalArgumentException")
        void createReservation_withNullEndTime_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When and then
            res.setEndTime(null);
            assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
        }

        @Test
        @DisplayName("End time before start time - should throw IllegalArgumentException")
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
        @DisplayName("End time equal to start time - should throw IllegalArgumentException")
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

        @Test
        @DisplayName("Start time before now - should throw IllegalArgumentException")
        void createReservation_withStartTimeBeforeNow_ShouldThrowIllegalArgumentException() {
            try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {
                // Given
                LocalDateTime fixedDateTime = LocalDateTime.of(2025, 6, 20, 11, 0);
                mockedDateTime.when(LocalDateTime::now).thenReturn(fixedDateTime);

                Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                        LocalDateTime.of(2025, 6, 25, 10, 0),
                        LocalDateTime.of(2025, 6, 25, 12, 0));

                // When
                LocalDateTime start = LocalDateTime.of(2025, 6, 19, 10, 0);
                res.setStartTime(start);

                // Then
                assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
                verify(reservationRepo).save(res);
            }
        }

        @Test
        @DisplayName("Both start and end time before now - should throw IllegalArgumentException")
        void createReservation_withEndTimeBeforeNow_ShouldThrowIllegalArgumentException() {
            try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {
                // Given
                LocalDateTime fixedDateTime = LocalDateTime.of(2025, 6, 20, 11, 0);
                mockedDateTime.when(LocalDateTime::now).thenReturn(fixedDateTime);

                Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                        LocalDateTime.of(2025, 6, 25, 10, 0),
                        LocalDateTime.of(2025, 6, 25, 12, 0));

                // When
                LocalDateTime start = LocalDateTime.of(2025, 6, 19, 10, 0);
                res.setStartTime(start);
                LocalDateTime end = LocalDateTime.of(2025, 6, 19, 8, 0);
                res.setEndTime(end);

                // Then
                assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(res));
                verify(reservationRepo).save(res);
            }
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
            when(reservationRepo.findById("res1")).thenReturn(Optional.of(res));
            when(reservationRepo.save(res)).thenReturn(res);

            // When
            Reservation result = reservationService.updateReservation(res);

            // Then
            assertEquals(res, result);
            verify(reservationRepo).save(res);
        }

        @Test
        @DisplayName("Empty id - should throw IllegalArgumentException")
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
        @DisplayName("Null id - should throw IllegalArgumentException")
        void updateReservation_withNullId_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));

            // When
            res.setId(null);

            // Then
            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }

        @Test
        @DisplayName("Null reservation - should throw IllegalArgumentException")
        void updateReservation_withNullReservation_ShouldThrowIllegalArgumentException() {
            // Given
            Reservation res = null;

            // Then
            assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservation(res));
        }

        @Test
        @DisplayName("Non-existing reservation - should throw IllegalArgumentException")
        void updateReservation_withNonExistingReservation_ShouldThrowIllegalArgumentException() {
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            when(reservationRepo.findById("res1")).thenReturn(Optional.empty());

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
        void getByStudentNominal_shouldReturnList() {
            String studentId = "s1";
            Student s1 = new Student("s1", "John", "Doe");
            Room room = new Room("r1", "Room A", 30);
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            Reservation res2 = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 12, 0),
                    LocalDateTime.of(2025, 6, 25, 14, 0));
            when(reservationRepo.findByStudentId(studentId)).thenReturn(Arrays.asList(res,res2));

            List<Reservation> result = reservationService.getReservationsByStudent(studentId);
            assertEquals(Arrays.asList(res,res2), result);
        }

        @Test
        @DisplayName("Null studentId - should throw IllegalArgumentException")
        void getByStudentNullId_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByStudent(null));
        }

        @Test
        @DisplayName("Empty studentId - should throw IllegalArgumentException")
        void getByStudentEmptyId() {
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
            Student s1 = new Student("s1", "John", "Doe");
            Room room = new Room("r1", "Room A", 30);
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));
            Reservation res2 = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 12, 0),
                    LocalDateTime.of(2025, 6, 25, 14, 0));

            when(reservationRepo.findByRoomId(roomId)).thenReturn(Arrays.asList(res,res2));

            List<Reservation> result = reservationService.getReservationsByRoom(roomId);
            assertEquals(Arrays.asList(res,res2), result);
        }

        @Test
        @DisplayName("Null roomId - should throw IllegalArgumentException")
        void getByRoomNullId_ShouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByRoom(null));
        }

        @Test
        @DisplayName("Empty roomId - should throw IllegalArgumentException")
        void getByRoomEmptyId_ShouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByRoom("  "));
        }
    }

    @Nested
    @DisplayName("deleteReservation Tests")
    class DeleteTests {


        @Test
        @DisplayName("Nominal case - should delete")
        void deleteNominal() {
            String id = "res1";
            Student s1 = new Student("s1", "John", "Doe");
            Room room = new Room("r1", "Room A", 30);
            Reservation res = new Reservation("res1", Arrays.asList(s1), room,
                    LocalDateTime.of(2025, 6, 25, 10, 0),
                    LocalDateTime.of(2025, 6, 25, 12, 0));


            when(reservationRepo.findById(id)).thenReturn(Optional.of(res));

            assertDoesNotThrow(() -> reservationService.deleteReservation(id));
            verify(reservationRepo).delete(res);
        }

        @Test
        @DisplayName("Null  - should throw IllegalArgumentException")
        void deleteNullId_ShouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation(null));
        }

        @Test
        @DisplayName("Empty id - should throw IllegalArgumentException")
        void deleteEmptyId_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation("  "));
        }


        @Test
        @DisplayName("Non-existing reservation - should throw IllegalArgumentException")
        void deleteNonExisting() {
            String id = "res1";
            when(reservationRepo.findById(id)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> reservationService.deleteReservation("res1"));
        }
    }

    @Nested
    @DisplayName("getUpcomingReservations Tests")
    class GetUpcomingReservationsTests {
        @Test
        @DisplayName("Nominal case - should return list")
        void getUpcomingReservations_ShouldReturnUpcomingReservations() {
            try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {
                // Given
                LocalDateTime fixedDateTime = LocalDateTime.of(2025, 6, 15, 11, 0);
                mockedDateTime.when(LocalDateTime::now).thenReturn(fixedDateTime);

                when(reservationRepo.findAfterDate(fixedDateTime)).thenReturn(Arrays.asList(testReservation,savedReservation));

                List<Reservation> result = reservationService.getUpcomingReservations();
                assertEquals(Arrays.asList(testReservation, savedReservation), result);
            }
        }
    }
}
