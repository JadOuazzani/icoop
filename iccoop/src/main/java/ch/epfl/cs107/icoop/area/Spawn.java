package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Rochers;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.utilities.ICoop;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

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
