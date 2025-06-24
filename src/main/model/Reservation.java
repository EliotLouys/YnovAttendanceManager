package model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private String id;
    private List<Student> students;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructeur
    public Reservation(String id, List<Student> students, Room room, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.students = students;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter pour id
    public String getId() {
        return id;
    }

    // Setter pour id
    public void setId(String id) {
        this.id = id;
    }

    // Getter pour students
    public List<Student> getStudents() {
        return students;
    }

    // Setter pour students
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    // Getter pour room
    public Room getRoom() {
        return room;
    }

    // Setter pour room
    public void setRoom(Room room) {
        this.room = room;
    }

    // Getter pour startTime
    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Setter pour startTime
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // Getter pour endTime
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Setter pour endTime
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
