package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.foe.DarkLord;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

public class Sanctum extends ICoopArea{

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        DarkLord darkLord = new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(7, 9), List.of("physique"), 10);
        registerActor(darkLord);
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8, 0));
    }

    @Override
    public String getTitle() {
        return "Sanctum";
    }

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isOff() {
        return false;
    }
}
