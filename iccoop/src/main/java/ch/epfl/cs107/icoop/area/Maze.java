package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.foe.HellSkull;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

public class Maze extends ICoopArea {
    private DialogHandler jeu;

    public Maze(DialogHandler jeu) {
        this.jeu = jeu;
    }


    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        HellSkull hellSkull1 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 33), List.of("physique", "eau"));
        registerActor(hellSkull1);
        HellSkull hellSkull2 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 31), List.of("physique", "eau"));
        registerActor(hellSkull2);
        HellSkull hellSkull3 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 29), List.of("physique", "eau"));
        registerActor(hellSkull3);
        HellSkull hellSkull4 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 27), List.of("physique", "eau"));
        registerActor(hellSkull4);
        HellSkull hellSkull5 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 25), List.of("physique", "eau"));
        registerActor(hellSkull5);
        HellSkull hellSkull6 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 34), List.of("physique", "eau"));
        registerActor(hellSkull6);
        HellSkull hellSkull7 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 32), List.of("physique", "eau"));
        registerActor(hellSkull7);
        HellSkull hellSkull8 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 30), List.of("physique", "eau"));
        registerActor(hellSkull8);
        HellSkull hellSkull9 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 28), List.of("physique", "eau"));
        registerActor(hellSkull9);
        HellSkull hellSkull10 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 26), List.of("physique", "eau"));
        registerActor(hellSkull10);
    }

    public String getTitle() {
        return "Maze";
    }

    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39));
    }

}


