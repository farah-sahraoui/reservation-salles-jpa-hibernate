package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int capacite;
    private String description;

    @OneToMany(mappedBy = "salle",
            cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "salle_equipement",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private List<Equipement> equipements = new ArrayList<>();

    public Salle() {}

    public Salle(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    // Méthodes utilitaires
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setSalle(this);
    }

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
        equipement.getSalles().add(this);
    }

    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
        equipement.getSalles().remove(this);
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public List<Reservation> getReservations() { return reservations; }
    public List<Equipement> getEquipements() { return equipements; }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Salle : " + nom + " (Capacité: " + capacite + ")";
    }
}