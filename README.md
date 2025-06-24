Cahier des charges – Projet de Réservation de Salles
1. Objectif du projet
Développer une application Java (non graphique) permettant à des étudiants de réserver des salles. Le cœur du projet repose sur une modélisation simple orientée objet avec une gestion efficace des créneaux horaires.

2. Fonctionnalités attendues
2.1 Gestion des entités principales
Étudiant (Student)

Attributs : nom, prénom, identifiant

Fonctionnalités : créer un étudiant, lister les réservations effectuées

Salle (Room)

Attributs : nom, capacité, identifiant

Fonctionnalités : créer une salle, lister les disponibilités

2.2 Réservation (Booking)
Attributs : étudiant, salle, date, heure de début, heure de fin

Fonctionnalités :

Créer une réservation (si la salle est disponible)

Annuler une réservation

Lister les réservations d’une salle ou d’un étudiant

Vérifier les conflits d’horaires lors de la réservation

3. Règles de gestion
Une réservation ne peut pas chevaucher une autre réservation dans la même salle.

Un étudiant peut réserver plusieurs salles, à condition que les horaires ne soient pas en conflit.

Les réservations sont limitées entre 08h00 et 20h00 (jours ouvrables uniquement).

Durée minimale d’une réservation : 30 minutes.

Les créneaux sont à précision de 30 minutes.

4. Architecture et conception
Langage : Java

Modèle orienté objet : classes principales Student, Room, Booking

Stockage en mémoire (liste statique ou singleton) – pas de base de données

Application en ligne de commande ou en test unitaire (JUnit recommandé)

5. Estimation de charge
Durée estimée : 3 à 4 heures

Équipe : 2 développeurs

Répartition conseillée :

Dév. 1 : Classes Student, Room + logique de réservation

Dév. 2 : Classe Booking + validation horaires + tests

6. Critères de validation
Pas de conflit de réservation possible

Réservations bien associées aux étudiants et aux salles

Respect des règles de gestion horaires

Code clair, commenté et bien structuré

Scénarios de test démontrant toutes les fonctionnalités