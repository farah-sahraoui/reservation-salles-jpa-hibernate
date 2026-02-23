package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;

    @OneToMany(mappedBy = "utilisateur",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // ✅ Méthodes utilitaires correctes
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setUtilisateur(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setUtilisateur(null);
    }

    // Getters
    public Long getId() { return id; }
    public List<Reservation> getReservations() { return reservations; }

    @Override
    public String toString() {
        return "Utilisateur : " + nom + " " + prenom + " (" + email + ")";
    }
}