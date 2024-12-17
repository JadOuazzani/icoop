package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.foe.FireBall;
import ch.epfl.cs107.icoop.actor.foe.Foe;
import ch.epfl.cs107.icoop.actor.foe.HellSkull;
import ch.epfl.cs107.icoop.actor.foe.WaterBall;
import ch.epfl.cs107.icoop.actor.object.Door;
import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.object.Rochers;
import ch.epfl.cs107.icoop.actor.object.Staff;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

import static java.awt.ComponentOrientation.getOrientation;

public class Spawn extends ICoopArea {
    private DialogHandler jeu;
    private Dialog welcome = new Dialog("Welcome");

    public Spawn(DialogHandler jeu) {
        this.jeu = jeu;
        jeu.publish(welcome);
    }

    @Override
    protected void createArea() {

        registerActor(new Background(this));
        registerActor(new Foreground(this));
        Door door = new Door("OrbWay", Logic.TRUE, List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)), this, new DiscreteCoordinates(19, 15), Orientation.DOWN, new DiscreteCoordinates(19, 16));
        registerActor(door);
        Explosif explosif1 = new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(11, 10));
        registerActor(explosif1);
        Explosif explosif2 = new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(6, 7));
        registerActor(explosif2);
        Explosif explosif3 = new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(14, 4));
        registerActor(explosif3);
        Rochers rochers = new Rochers(this, Orientation.DOWN, new DiscreteCoordinates(10, 10));
        registerActor(rochers);
        Door door2 = new Door("Maze", Logic.TRUE, List.of(new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39)), this, new DiscreteCoordinates(4, 0), Orientation.DOWN, new DiscreteCoordinates(5, 0));
        registerActor(door2);

    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6));
    }

}
