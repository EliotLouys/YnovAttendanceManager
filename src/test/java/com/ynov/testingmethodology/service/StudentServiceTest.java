package com.ynov.testingmethodology.service;


import com.ynov.testingmethodology.service.StudentService;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ynov.testingmethodology.repository.StudentRepository;
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

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Student Service Unit Tests")

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Nested
    @DisplayName("registerStudentTest")
    class registerStudentTest{
        @Test
        @DisplayName("NominalCase - should return student")
        public void registerStudentTestNominalCase(){
            //Given
            Student student = new Student("1","Eliot","Louys");
            when(studentRepository.save(student)).thenReturn(student);

            //When

            Student result = studentService.registerStudent(student);
            //Then
            assertEquals(student,result);
        }

        @Test
        @DisplayName("Student already exists - should return Illegal argument exception")
        public void registerStudentTestStudentAlreadyExists(){
            //Given
            Student student = new Student("1","Eliot","Louys");
            when(studentRepository.existsByID(student.getId())).thenReturn(true);

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Null student - should return Illegal argument exception")
        public void registerStudentTestnullStudent(){
            //Given
            Student student = null;

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student id null - should return Illegal argument exception")
        public void registerStudentTestStudentIdNull(){
            //Given
            Student student = new Student(null,"Eliot","Louys");

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student id empty - should return Illegal argument exception")
        public void registerStudentTestStudentIdEmpty(){
            //Given
            Student student = new Student("  ","Eliot","Louys");

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student First Name null - should return Illegal argument exception")
        public void registerStudentTestStudentFirstNameNull(){
            //Given
            Student student = new Student("1",null,"Louys");

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student First Name empty - should return Illegal argument exception")
        public void registerStudentTestStudentFirstNameEmpty(){
            //Given
            Student student = new Student("1","  ","Louys");

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student Last Name null - should return Illegal argument exception")
        public void registerStudentTestStudentLastNameNull(){
            //Given
            Student student = new Student("1","Eliot",null);

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

        @Test
        @DisplayName("Student Last Name empty - should return Illegal argument exception")
        public void registerStudentTestStudentLastNameEmpty(){
            //Given
            Student student = new Student("1","Eliot","  ");

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.registerStudent(student);});
        }

    }

    @Nested
    @DisplayName("Delete student test")
    class  deleteStudentTest{
        @Test
        @DisplayName("Nominal case")
        public void deleteStudentTestNominalCase(){
            //Given
            String id = "1";
            when(studentRepository.existsByID(id)).thenReturn(true);

            //When
            assertDoesNotThrow(()->{studentService.deleteStudent(id);});

            //Then
            verify(studentRepository).existsByID(id);
            verify(studentRepository).deleteById(id);
        }

        @Test
        @DisplayName("Non existant student - should throww illegal argument exception")
        public void deleteStudentTestNonExistantStudent(){
            //Given
            String id = "1";
            when(studentRepository.existsByID(id)).thenReturn(false);

            //When
            assertThrows(IllegalArgumentException.class,()->{studentService.deleteStudent(id);});

        }
    }

    @Nested
    @DisplayName("Get all students")
    class getAllStudentsTest{
        @Test
        @DisplayName("Nominal case - Should return a list of students")
        public void getAllStudentsTestNominalCase(){
            //Given
            Student student = new Student("1","Eliot","Louys");
            Student student2 = new Student("2","Samuel","Leobon");
            List<Student> students = Arrays.asList(student,student2);
            when(studentRepository.findAll()).thenReturn(students);

            //When
            List<Student> result = studentService.getAllStudents();

            //Then
            assertEquals(students,result);

        }
    }

    @Nested
    @DisplayName("Get student by id")
    class getStudentByIdTest{
        @Test
        @DisplayName("Nominal case - should return an optional student")
        public void getStudentByIdTestNominalCase(){
            //Given
            String id = "1";
            Student student = new Student("1","Eliot","Louys");
            when(studentRepository.findById(id)).thenReturn(Optional.of(student));

            //When
            Optional<Student> res = studentService.getStudentById(id);

            //Then
            assertEquals(Optional.of(student),res);
        }

        @Test
        @DisplayName("Null id - should throw illegal argument exception")
        public void getStudentByIdTestNullId(){
            //Given
            String id = null;

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.getStudentById(id);});
        }

        @Test
        @DisplayName("Empty id - should throw illegal argument exception")
        public void getStudentByIdTestEmptyId(){
            //Given
            String id = "  ";

            //Then
            assertThrows(IllegalArgumentException.class,()->{studentService.getStudentById(id);});
        }
    }

}
