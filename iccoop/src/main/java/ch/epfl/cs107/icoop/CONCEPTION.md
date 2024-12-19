# Documentation des Classes et Extensions

## Classes

### Coin
- **Package** : `ch.epfl.cs107.icoop.actor.object`
- **Objectif** :
  Représente les pièces collectables dans le jeu, incluant une animation, la gestion de la collecte, et l’interaction pour augmenter le compteur global de pièces du joueur.

### DarkLord
- **Package** : `ch.epfl.cs107.icoop.actor.foe`
- **Objectif** :
  Implémente le boss final avec des comportements spécifiques : déplacements ciblés, projectiles alternant entre feu et eau, animations, interactions et gestion des mouvements.

### logMonster
- **Package** : `ch.epfl.cs107.icoop.actor.foe`
- **Objectif** :
  Représente des ennemis en bois ayant des comportements simples, comme tirer des flèches, tout en incluant les animations et les interactions pour infliger des dégâts physiques.

### MusicPlayer
- **Package** : `ch.epfl.cs107.icoop.handler`
- **Objectif** :
  Gère la lecture de musique en boucle ou de sons d’ambiance.

### Sanctum, SanctumEntrance, Accueil, Ending
- **Package** : `ch.epfl.cs107.icoop.area`
- **Objectif** :
  Modélise différentes zones du jeu, chacune ayant des objets et des caractéristiques spécifiques.

### Gestion des coeurs
- **Description** : Les coeurs ne sont récupérables que si la vie du joueur n’est pas pleine, rendant leur gestion plus stratégique.ps: SanctumEntrance Behavior a du être redéssiné car mauvais Behavior fourni.

### Compteur de pièces global
- **Description** : Ajout d’un compteur global pour suivre les pièces collectées.
- **Modifications** : La classe `ICoopPlayer` a été modifiée pour inclure un attribut dédié et une méthode d’incrémentation.

### Retardateur de l'explosif 
- **int en float** : Nous avons fait le choix d'attribuer au retardateur un type float à la place d'un int car le délai doit être exprimé avec une précision supérieure à l'unité entière. 

## Extensions Ajoutées

### Écran d’accueil et de fin
- **Contexte** : Création d’aires spécifiques (`Accueil`, `Ending`) pour marquer le début et la fin du jeu.
- **Implémentation** : Ces classes héritent de `ICoopArea` et gèrent uniquement ces moments particuliers du jeu.

### Création de pièces
- **Contexte** : Implémentation de la classe `Coin`.
- **Implémentation** :
    - Les pièces sont dispersées dans les différentes aires.
    - Une fois collectées, elles augmentent le compteur global.
    - Si le joueur en récupère trois, le jeu se termine.

### Nouvelle aire : SanctumEntrance
- **Contexte** : Contient des ennemis de type `logMonster` et un orbe de vitesse.
- **Implémentation** : Ces éléments introduisent des mécaniques plus complexes, notamment en matière de déplacement du joueur en terme de difficulté.

### Nouvelle aire : Sanctum
- **Contexte** : Inclut le boss final (`DarkLord`) et des pièces.
- **Implémentation** : La victoire est déclenchée lorsque le boss est vaincu et qu’une pièce est récupérée.

### Musique et pause
- **Contexte** : Introduction de musique d’ambiance et gestion de la pause dans toutes les aires.
- **Implémentation** : Utilisation de la classe `MusicPlayer` pour gérer les pistes sonores.

### Orbe de vitesse
- **Contexte** : Amélioration temporaire de la vitesse du joueur.
- **Implémentation** : L’orbe est placé dans l’aire `SanctumEntrance`.

### Nouveaux dialogues
- **Contexte** : Ajout de dialogues contextuels: 
l'orb de vitesse et la mort du boss final 
- **Implémentation** : Ces dialogues sont activés dans l’aire `SanctumEntrance` et `Sanctum`

### Compteur de pièces
- **Contexte** : Permet au joueur de suivre les pièces collectées.
- **Implémentation** : Affichage dans l’interface générale (`ICoop`).