package com.ynov.testingmethodology.repository;

import com.ynov.testingmethodology.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(String id);
    List<Student> findAll();
    void deleteById(String id);

    boolean existsByID(String id);
}
