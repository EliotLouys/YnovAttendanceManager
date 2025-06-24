package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un étudiant pouvant effectuer des réservations de salles.
 */
public class Student {
    private String id;
    private String firstName;
    private String lastName;

    // Liste des réservations effectuées par cet étudiant
    private List<Reservation> reservations;


    public Student(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.reservations = new ArrayList<>();
    }

    // === Getters / Setters ===
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Reservation> getBookings() {
        return reservations;
    }

    // === Méthodes de logique ===

    /**
     * Ajoute une réservation à l'étudiant.
     * @param reservation Réservation à ajouter
     */
    public void addBooking(Reservation reservation) {
        reservations.add(reservation);
    }

/**
