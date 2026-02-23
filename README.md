Système de Réservation de Salles

##  Objectifs du TP
Les objectifs principaux sont :

- Concevoir les entités **Salle**, **Réservation** et **Utilisateur** ainsi que leurs relations.
- Implémenter une relation **ManyToMany** entre les entités **Salle** et **Équipement**.
- Configurer et tester différentes stratégies de **cascade** (PERSIST, MERGE, REMOVE, ALL).
- Expérimenter le mécanisme de suppression orpheline (**orphanRemoval**) afin de comprendre la gestion automatique des entités dépendantes.
-
-  Analyser le comportement des relations bidirectionnelles dans un contexte JPA/Hibernate.
- Consolider les notions de modélisation orientée objet et de mapping relationnel avec JPA.

##  1-Génération des tables avec Hibernate:

![WhatsApp Image 2026-02-23 at 13 11 09](https://github.com/user-attachments/assets/bfab815e-3752-4bb6-b1ed-25048366f237)


![WhatsApp Image 2026-02-23 at 13 11 38](https://github.com/user-attachments/assets/c3c8e96f-1114-4314-9f05-a15c997e68f7)


![WhatsApp Image 2026-02-23 at 13 12 02](https://github.com/user-attachments/assets/a3796dc3-0603-4bdb-a28e-220685d054c0)


![WhatsApp Image 2026-02-23 at 13 12 26](https://github.com/user-attachments/assets/14fa143f-c5de-44db-b2b9-043d84a04aa3)





Ces captures montrent la génération automatique des tables par Hibernate au démarrage de l’application.

Hibernate supprime d’abord les anciennes tables, puis crée les nouvelles tables correspondant aux entités :
`Utilisateur`, `Salle`, `Reservation` et `Equipement`, ainsi que la table de jointure `salle_equipement` pour la relation ManyToMany.

Les clés primaires et étrangères sont automatiquement générées, ce qui confirme que les relations JPA sont correctement
configurées et que la base de données est synchronisée avec les entités Java.


## 2-Insertion des entités :


![WhatsApp Image 2026-02-23 at 13 12 53](https://github.com/user-attachments/assets/e28cceb2-e3de-4923-9e6e-c3fd8a084e35)



Cette capture montre l’insertion automatique des entités `Utilisateur`, `Reservation` et `Salle` dans la base de données.

Grâce à la configuration des stratégies de cascade, Hibernate génère automatiquement les requêtes `INSERT` sans avoir besoin
de persister chaque entité manuellement.  
Cela confirme que la relation entre les entités est correctement configurée.


## 3-Mise à jour et vérification des données:

![WhatsApp Image 2026-02-23 at 13 13 28](https://github.com/user-attachments/assets/74819d68-512e-42a8-9d02-4a45123b745f)


Cette capture affiche une requête `UPDATE` suivie d’une requête `SELECT` permettant de vérifier l’enregistrement des données.
Le message de confirmation indique que l’opération s’est déroulée avec succès, prouvant que la persistance et la récupération 
des données fonctionnent correctement.

## 4-Vérification des réservations d’un utilisateur:

![WhatsApp Image 2026-02-23 at 13 13 57](https://github.com/user-attachments/assets/981a796a-0396-4a4f-baf7-e8da4a4b3281)


Cette capture montre une requête `SELECT` avec une jointure (`LEFT OUTER JOIN`) entre `Reservation` et `Salle`.
Elle permet de récupérer les réservations associées à un utilisateur spécifique et confirme le bon fonctionnement de la relation entre 
`Utilisateur` et `Reservation`.

## 5-Vérification des réservations d’une salle:

![WhatsApp Image 2026-02-23 at 13 14 24](https://github.com/user-attachments/assets/b652ee73-7f07-4bd2-966e-15fd8d2b39a9)

Cette capture présente une requête `SELECT` avec jointure entre `Reservation` et `Utilisateur`.
Elle permet d’afficher les réservations liées à une salle donnée et confirme la cohérence des relations bidirectionnelles
dans le système.


## 6-Vérification du mécanisme orphanRemoval:

![WhatsApp Image 2026-02-23 at 13 14 58](https://github.com/user-attachments/assets/d8764add-0914-4e6e-8107-d3e5a74fd80f)


Cette capture montre l’initialisation des entités lors du test du mécanisme `orphanRemoval`.
Hibernate exécute les requêtes `INSERT` pour créer les entités nécessaires (Salle, Utilisateur, etc.) avant de tester la suppression automatique des entités dépendantes.


## 7-Test de suppression avec orphanRemoval:

![WhatsApp Image 2026-02-23 at 13 15 32](https://github.com/user-attachments/assets/e45957f9-422e-4211-80cb-350c35b4c1f2)


![WhatsApp Image 2026-02-23 at 13 16 01](https://github.com/user-attachments/assets/31665618-9d17-45b6-a5cc-5b610b6352bb)


Ces captures montrent le test du mécanisme `orphanRemoval` sur la relation entre `Utilisateur` et `Reservation`.
Dans un premier temps, Hibernate insère un utilisateur avec deux réservations associées.  
Le message confirme que deux réservations sont bien enregistrées.
Ensuite, après la suppression d’une réservation de la collection de l’utilisateur, Hibernate génère automatiquement une requête `DELETE` sur la table `Reservation`.
Cela démontre que l’option `orphanRemoval = true` fonctionne correctement :  
lorsqu’une entité enfant est retirée de la relation, elle est automatiquement supprimée de la base de données.


## 8-Vérification après suppression (orphanRemoval):

![WhatsApp Image 2026-02-23 at 13 16 28](https://github.com/user-attachments/assets/a81e15c4-2640-42f3-8370-3b5eaf595954)

![WhatsApp Image 2026-02-23 at 13 17 44](https://github.com/user-attachments/assets/a570140f-8fb0-47a4-827c-52989c11e176)


Ces captures montrent la vérification des données après la suppression d’une réservation.
Hibernate exécute une requête `SELECT` avec jointures pour récupérer les réservations liées à l’utilisateur.  
Le résultat indique que le nombre de réservations est passé de 2 à 1 après la suppression.
La seconde requête confirme que la réservation supprimée n’existe plus en base (`true`), ce qui prouve que le mécanisme `orphanRemoval` a bien supprimé automatiquement l’entité enfant.
Cette étape valide définitivement le bon fonctionnement de la suppression orpheline dans le système.

## 9-Vérification de la relation ManyToMany (Salle – Équipement):

![WhatsApp Image 2026-02-23 at 13 18 10](https://github.com/user-attachments/assets/a424ac2b-f996-4775-b016-ae5c7445d111)

![WhatsApp Image 2026-02-23 at 13 18 36](https://github.com/user-attachments/assets/4450deed-d273-4603-9c10-d3769087e569)


![WhatsApp Image 2026-02-23 at 13 19 11](https://github.com/user-attachments/assets/bc4454f4-8d89-4fe6-89a9-512d739e91fe)


Ces captures montrent le test de la relation ManyToMany entre les entités `Salle` et `Equipement`.
Hibernate commence par insérer les entités `Salle` et `Equipement` dans leurs tables respectives.  
Ensuite, il génère automatiquement des requêtes `INSERT` dans la table de jointure `salle_equipement`.
Cette table intermédiaire contient les clés étrangères `salle_id` et `equipement_id`, ce qui permet d’associer plusieurs équipements à une salle et inversement.
Le message de confirmation indique que les salles et équipements ont été correctement enregistrés, validant ainsi le bon fonctionnement de la relation ManyToMany.


## 10-Consultation des équipements associés à une salle:

![WhatsApp Image 2026-02-23 at 13 19 40](https://github.com/user-attachments/assets/61cd3ee7-2313-4122-83ee-ed23d91356fe)


![WhatsApp Image 2026-02-23 at 13 20 08](https://github.com/user-attachments/assets/ee7eeffe-64a7-49de-ada2-d4dd862f5941)


Ces captures montrent la récupération des équipements liés à une salle spécifique dans le cadre de la relation ManyToMany.
Hibernate exécute une requête `SELECT` sur la table `Salle`, puis une requête avec `INNER JOIN` entre la table de jointure `salle_equipement` et la table `Equipement`.
Les résultats affichent les équipements associés à chaque salle (ex : Vidéoprojecteur 4K, Module visioconférence), ce qui confirme que la relation ManyToMany fonctionne correctement en lecture.
Cette étape valide la cohérence des associations entre les entités `Salle` et `Equipement`.


## 11-Équipements associés à une salle:

![WhatsApp Image 2026-02-23 at 13 20 47](https://github.com/user-attachments/assets/84fa474d-01a8-4b3e-9a6e-a13a5dc9da79)

![WhatsApp Image 2026-02-23 at 13 21 29](https://github.com/user-attachments/assets/f029c482-a097-41f9-8346-074e31ca9a20)


Cette partie montre la requête Hibernate permettant de récupérer les équipements liés à une salle spécifique.

Hibernate génère automatiquement une requête SQL avec :

Une jointure (INNER JOIN) entre la table salle_equipement et equipement

Une condition where equipement0_.salle_id = ?

Cela signifie que pour une salle donnée, le système récupère tous les équipements associés via la table de liaison.


## 12-gestion dynamique des associations:

![WhatsApp Image 2026-02-23 at 13 22 17](https://github.com/user-attachments/assets/11fce5fd-dd96-428f-9970-c5efd5de1572)

![WhatsApp Image 2026-02-23 at 13 23 17](https://github.com/user-attachments/assets/4032486d-1935-4eda-a5ef-d60c2f22d782)


Ces captures illustrent les dernières étapes d’exécution de l’application avec Hibernate. On observe d’abord
l’insertion d’une association dans la table de liaison salle_equipement, confirmant la création du lien entre
une salle et un équipement dans la relation ManyToMany. Une requête de sélection est ensuite exécutée
pour recharger la salle concernée et vérifier que les modifications ont bien été prises en compte. Enfin, lors
de la phase finale, Hibernate supprime les tables existantes avec l’option CASCADE, ce qui entraîne la suppression complète des données
et garantit une réinitialisation propre de la base de données à la fermeture de l’application.


## Diagramme de classes du système de réservation:

![WhatsApp Image 2026-02-23 at 15 25 37](https://github.com/user-attachments/assets/ef955935-1e5d-4371-9441-187bbdac226d)


Cette figure présente le diagramme de classes du système de gestion des réservations. Il met en évidence quatre entités principales :
Utilisateur, Reservation, Salle et Equipement. Un utilisateur peut effectuer plusieurs réservations, tandis qu’une réservation est associée à une seule salle, 
ce qui traduit une relation OneToMany entre Utilisateur et Reservation, ainsi qu’entre Salle et Reservation. La relation entre Salle et Equipement
est de type ManyToMany, permettant à une salle de contenir plusieurs équipements et à un équipement d’être associé à plusieurs salles.
Les principales méthodes métier, telles que l’ajout ou la suppression de réservations et d’équipements, sont également représentées, illustrant la logique fonctionnelle du système.

## Conclusion:

Ce TP a permis de concevoir et développer un système de réservation de salles intégrant plusieurs relations entre entités.
Il a notamment mis en œuvre une relation ManyToMany entre les entités Salle et Équipement, tout en configurant et testant différentes stratégies de cascade afin 
d’assurer la cohérence des données.L’expérimentation de la suppression orpheline (orphanRemoval) a également permis de mieux comprendre la gestion automatique des entités dépendantes.
L’ensemble de ces notions constitue une base essentielle pour la modélisation de données avec JPA/Hibernate
et pour la conception d’applications robustes intégrant des relations complexes entre entités.





