package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;

    @ManyToMany(mappedBy = "equipements")
    private List<Salle> salles = new ArrayList<>();

    public Equipement() {}

    public Equipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getter
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public List<Salle> getSalles() { return salles; }

    @Override
    public String toString() {
        return "Equipement : " + nom;
    }
}
