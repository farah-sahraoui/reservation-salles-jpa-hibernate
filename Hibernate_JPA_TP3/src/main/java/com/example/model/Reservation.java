package com.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String motif;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    public Reservation() {}

    public Reservation(LocalDateTime dateDebut,
                       LocalDateTime dateFin,
                       String motif) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        return "Reservation : " + motif +
                " du " + dateDebut +
                " au " + dateFin;
    }
}