package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations");

        try {
            System.out.println("\n=== Vérification des relations et du cascade persist ===");
            testRelationsEtCascade(emf);

            System.out.println("\n=== Vérification du mécanisme orphanRemoval ===");
            testSuppressionOrpheline(emf);

            System.out.println("\n=== Vérification de la relation ManyToMany (Salle - Équipement) ===");
            testRelationManyToMany(emf);

        } finally {
            emf.close();
        }
    }

    private static void testRelationsEtCascade(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            System.out.println("Initialisation des entités...");

            Utilisateur utilisateur = new Utilisateur("Benali", "Yasmine", "yasmine.benali@mail.com");

            Salle salle = new Salle("Salle Innovation", 28);
            salle.setDescription("Espace collaboratif moderne avec écran numérique");

            Reservation reservation = new Reservation(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(2),
                    "Atelier de planification"
            );

            utilisateur.addReservation(reservation);
            salle.addReservation(reservation);

            em.persist(utilisateur);
            em.persist(salle);

            em.getTransaction().commit();
            System.out.println("✔ Enregistrement effectué avec succès !");

            em.clear();

            System.out.println("\nContrôle des données enregistrées :");
            Utilisateur utilisateurPersiste = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Utilisateur enregistré : " + utilisateurPersiste);
            System.out.println("Total réservations utilisateur : " + utilisateurPersiste.getReservations().size());

            Salle sallePersistee = em.find(Salle.class, salle.getId());
            System.out.println("Salle enregistrée : " + sallePersistee);
            System.out.println("Total réservations salle : " + sallePersistee.getReservations().size());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testSuppressionOrpheline(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Utilisateur utilisateur = new Utilisateur("Karim", "Imane", "imane.karim@mail.com");

            Salle salle1 = new Salle("Salle Horizon", 18);
            em.persist(salle1);

            Salle salle2 = new Salle("Salle Atlas", 22);
            em.persist(salle2);

            Reservation reservation1 = new Reservation(
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusHours(1),
                    "Réunion stratégique"
            );

            Reservation reservation2 = new Reservation(
                    LocalDateTime.now().plusDays(3),
                    LocalDateTime.now().plusDays(3).plusHours(2),
                    "Session de formation interne"
            );

            utilisateur.addReservation(reservation1);
            utilisateur.addReservation(reservation2);
            salle1.addReservation(reservation1);
            salle2.addReservation(reservation2);

            em.persist(utilisateur);

            em.getTransaction().commit();
            System.out.println("✔ Utilisateur avec deux réservations enregistré !");

            em.getTransaction().begin();

            Utilisateur utilisateurAModifier = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Réservations avant suppression : " + utilisateurAModifier.getReservations().size());

            Reservation reservationASupprimer = utilisateurAModifier.getReservations().get(0);
            utilisateurAModifier.removeReservation(reservationASupprimer);

            em.getTransaction().commit();

            em.clear();
            Utilisateur utilisateurApresModification = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Réservations après suppression : " + utilisateurApresModification.getReservations().size());

            Long reservationId = reservationASupprimer.getId();
            Reservation reservationSupprimee = em.find(Reservation.class, reservationId);
            System.out.println("La réservation a-t-elle été supprimée de la base ? " + (reservationSupprimee == null));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testRelationManyToMany(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Equipement projecteur = new Equipement("Vidéoprojecteur 4K", "Projection haute résolution");
            Equipement ecran = new Equipement("Tableau numérique", "Surface tactile interactive");
            Equipement visioconference = new Equipement("Module visioconférence", "Caméra HD intégrée");

            Salle salleReunion = new Salle("Salle Executive", 26);
            Salle salleFormation = new Salle("Salle Learning", 38);

            salleReunion.addEquipement(projecteur);
            salleReunion.addEquipement(visioconference);

            salleFormation.addEquipement(projecteur);
            salleFormation.addEquipement(ecran);

            em.persist(salleReunion);
            em.persist(salleFormation);

            em.getTransaction().commit();
            System.out.println("✔ Salles et équipements enregistrés !");

            em.clear();

            System.out.println("\nContrôle des associations ManyToMany :");

            Salle salleReunionPersistee = em.find(Salle.class, salleReunion.getId());
            System.out.println("Salle : " + salleReunionPersistee.getNom());
            System.out.println("Équipements associés :");
            for (Equipement equipement : salleReunionPersistee.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            Salle salleFormationPersistee = em.find(Salle.class, salleFormation.getId());
            System.out.println("\nSalle : " + salleFormationPersistee.getNom());
            System.out.println("Équipements associés :");
            for (Equipement equipement : salleFormationPersistee.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            Equipement projecteurPersiste = em.createQuery(
                            "SELECT e FROM Equipement e WHERE e.nom = :nom", Equipement.class)
                    .setParameter("nom", "Vidéoprojecteur 4K")
                    .getSingleResult();

            System.out.println("\nÉquipement sélectionné : " + projecteurPersiste.getNom());
            System.out.println("Salles disposant de cet équipement :");
            for (Salle salle : projecteurPersiste.getSalles()) {
                System.out.println("- " + salle.getNom());
            }

            em.getTransaction().begin();
            salleReunionPersistee.removeEquipement(projecteurPersiste);
            em.getTransaction().commit();

            em.clear();

            Salle salleApresModification = em.find(Salle.class, salleReunion.getId());
            System.out.println("\nAprès suppression d'un équipement : " + salleApresModification.getNom());
            System.out.println("Équipements restants :");
            for (Equipement equipement : salleApresModification.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            Equipement projecteurApresModification = em.find(Equipement.class, projecteurPersiste.getId());
            System.out.println("\nL'équipement est-il toujours présent en base ? " + (projecteurApresModification != null));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}