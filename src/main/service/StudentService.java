package main.service;

import model.Student;
import model.Reservation;
import repository.StudentRepository;
import repository.ReservationRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final ReservationRepository reservationRepo;

    @Autowired
    public StudentService(StudentRepository studentRepo, ReservationRepository reservationRepo) {
        this.studentRepo = studentRepo;
        this.reservationRepo = reservationRepo;
    }

    public void registerStudent(String id, String firstName, String lastName) {
        Student student = new Student(id, firstName, lastName);
        studentRepo.save(student);
    }


    public boolean deleteStudent(String id) {
        Optional<Student> studentOpt = studentRepo.findById(id);
        if (studentOpt.isPresent()) {
            studentRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepo.findById(id);
    }

    public List<Reservation> getReservations(String studentId) {
        return reservationRepo.findByStudentId(studentId);
    }

        public boolean exists(String studentId) {
        return studentRepo.findById(studentId).isPresent();
    }
}
