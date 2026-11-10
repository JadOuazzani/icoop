# ICoop

A two-player cooperative 2D adventure game in Java, inspired by *Fireboy and Watergirl*, built for EPFL's CS-107 course (*Introduction à la programmation*, fall 2024) by Jad Chahed Ouazzani and Riad Elhariry.

Two complementary heroes, **Red** (fire) and **Blue** (water), play on the same keyboard. They solve puzzles together, fight enemies and avoid traps to collect three gold coins and defeat the final boss.

## Gameplay

- **Goal**: collect three gold coins. Along the way, find the two elemental keys that activate the teleporter to the final boss.
- **Six areas**:
  - **Spawn**: introduction to the controls
  - **OrbWay**: puzzle with elemental orbs, pressure plates and elemental walls
  - **Maze**: puzzles and combat against HellSkulls (short-range fireballs) and BombFoes (follow the players and drop bombs)
  - **Arena**: find the two keys and break rocks with the magic staffs
  - **SanctumEntrance**: dodge the arrows of the Log Monsters
  - **Sanctum**: final fight against the **Dark Lord**, who alternates fire and water projectiles with melee attacks
- **Items**: sword, fire and water staffs, remote-detonated explosives, elemental keys, orbs (temporary immunity to fire or water, or a speed boost) and hearts.
- **Mechanics**: pressure plates wired to logic signals, teleporters that need both keys, and an inventory per player.

## Extensions beyond the assignment

- Start and end screens
- Gold coins with a global counter shown on screen, which end the game at three
- Two new areas, SanctumEntrance and Sanctum, with new enemies (Log Monsters) and the Dark Lord boss
- Background music and a pause menu in every area
- Speed orb and new contextual dialogues
- Hearts that can only be picked up when a player's health is not full

## Controls

| Action | Red | Blue |
|---|---|---|
| Move | `W` `A` `S` `D` | `I` `J` `K` `L` |
| Attack / interact / use item | `E` | `O` |
| Switch item | `Q` | `U` |

`Space` skips dialogues, `T` resets the current area, `R` restarts the game, and a left click starts the game.

## Running

Requirements: Java 21 and Maven.

Open the project in your IDE as a Maven project, or build it from the root with `mvn compile`, then run `ch.epfl.cs107.Play` in the `iccoop` module.

## Project structure

```
game-engine/   2D game engine provided by the course
iccoop/        the game: areas, actors (players, enemies, objects), handlers (inventory, dialogues, HUD)
tutos/         course tutorials
```

`iccoop/src/main/java/ch/epfl/cs107/icoop/` also contains `HELP.md` (full game guide, in French) and `CONCEPTION.md` (design notes and extensions, in French).
