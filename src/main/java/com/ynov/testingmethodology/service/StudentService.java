package com.ynov.testingmethodology.service;

import model.Student;
import com.ynov.testingmethodology.model.Reservation;
import com.ynov.testingmethodology.repository.StudentRepository;
import com.ynov.testingmethodology.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

    @Autowired
    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student registerStudent(Student student) {
        validate(student);

        if (studentRepo.existsByID(student.getId())){
            throw new IllegalArgumentException("Student already exists");
        }

        return studentRepo.save(student);
    }


    public void deleteStudent(String id) {
        if (!studentRepo.existsByID(id)) {
            throw new IllegalArgumentException("Student does not exists with id :" + id);
        }

        studentRepo.deleteById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        if (id ==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Student id is null");
        }

        return studentRepo.findById(id);
    }




    private void validate(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is null");
        }
        if (student.getId() == null || student.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Student id is required");
        }
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }
}
