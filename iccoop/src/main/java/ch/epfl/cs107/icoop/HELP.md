# ICoop

Un jeu inspiré de **Fireboy and Watergirl**, mêlant puzzles en coopération et combats. Les joueurs incarnent deux héros
complémentaires : **Rouge** (élément feu) et **Bleu** (élément eau).

---

## Objectif du jeu

- Collecter **trois pièces d'or** pour terminer l’aventure.
- Résoudre des énigmes, vaincre des ennemis et éviter les pièges.
- Trouver **deux clés** nécessaires pour activer le téléporteur menant au boss final.

---

## Lancement du jeu

Exécutez le programme principal via la commande suivante :

```bash
ch.epfl.cs107.Play
```

---

## Contrôles

### Personnage Rouge

- **Déplacement** : `W`, `A`, `S`, `D`.  (Haut, Gauche, Bas, Droite).
- **Attaquer / Interagir / Utiliser un objet** : `E`.
- **Changer d’équipement** : `Q`.
- **Réinitialiser l’aire** : `T`.
- **Réinitialiser le jeu** : `R`.

### Personnage Bleu

- **Déplacement** : `I`, `J`, `K`, `L`.(Haut, Gauche, Bas, Droite)
- **Attaquer / Interagir / Utiliser un objet** : `O`.
- **Changer d’équipement** : `U`.

### Commandes générales

- **Passer les dialogues** : `Espace`.
- **Lancer le jeu** : Clic gauche de la souris.

---

## Objets et équipements

- **Épée** :
    - Arme de base pour attaquer les ennemis à courte distance.

- **Bâtons magiques** :
    - **Bâton de Feu** : Lance des boules de feu pour détruire des obstacles ou blesser des ennemis vulnérables.
    - **Bâton d’Eau** : Équivalent pour l’élément eau.

- **Explosifs** :
    - Permettent de détruire des obstacles comme les rochers.
    - Peuvent être déposés et activés à distance.
    - Font des dégats aux ennemis et au joueur.

- **Clés** :
    - Les clés élémentaires (feu et eau) servent à activer le téléporteur.

- **Orbes** :
    - Accordent une immunité temporaire contre des dégâts spécifiques (feu ou eau).
    - L’orbe de vitesse augmente la mobilité.

- **Cœurs** :
    - Restaurent la santé des personnages.

---

## Description des aires

### 1. Spawn (Accueil)

- **Type** : Aire introductive.
- **Objectifs** :
    - Se familiariser avec les commandes.
    - Tester les interactions et les armes.

### 2. OrbWay

- **Type** : Puzzle.
- **Objectifs** :
    - Récupérer deux orbes élémentaires pour obtenir une pièce d’or.
    - Désactiver des murs élémentaires via des plaques de pression.
    - Collecter des cœurs pour restaurer la santé.

### 3. Maze

- **Type** : Combinaison de puzzle et combat.
- **Ennemis** :
    - **HellSkulls** : Lancent des boules de feu à courte portée.
    - **BombFoes** : Suivent les joueurs et déposent des bombes.
- **Nouveaux objets** :
    - Les bâtons magiques deviennent accessibles.
- **Objectifs** :
    - Survivre aux pièges et ennemis.

### 4. Arena

- **Type** : Puzzle sans ennemis.
- **Objectifs** :
    - Trouver les deux clés nécessaires pour activer le téléporteur.
    - Détruire des rochers bloquant les passages à l'aide des batons.
    - Obtenir une pièce d’or.

### 5. SanctumEntrance

- **Type** : Transition vers le combat final.
- **Ennemis** :
    - **Log Monsters** : Tirent des flèches à intervalles aléatoires.
- **Récompenses** :
    - Une orbe de vitesse pour aider durant le combat final.
- **Objectifs** :
    - Esquiver les attaques ennemies.
    - Préparer le combat contre le boss.

### 6. Sanctum

- **Type** : Combat final.
- **Boss** : **Dark Lord**.
    - **Attaques** : Projectiles feu/eau et attaques au corps à corps.
    - **Particularité** : Dispose d’une grande quantité de points de vie.
- **Objectifs** :
    - Vaincre le boss.
    - Récupérer la troisième pièce d’or pour terminer le jeu.

---

## Mécaniques du jeu

- **Plaques de pression** :
    - Activent ou désactivent des mécanismes (murs, pièges, etc.).
    - Reliées à des signaux logiques.

- **Téléporteurs** :
    - Nécessitent deux clés pour fonctionner.
    - Permettent de progresser dans le jeu.

- **Bâtons** :
    - Indispensables pour briser certains obstacles, notamment dans l’Arène.