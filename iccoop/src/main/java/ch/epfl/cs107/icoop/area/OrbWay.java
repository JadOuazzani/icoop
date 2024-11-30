package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class OrbWay extends ICoopArea {

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5));
    }

    Door door1 = new Door("Spawn", Logic.TRUE, List.of(
            new DiscreteCoordinates(18, 16),
            new DiscreteCoordinates(18, 15)),
            this,
            new DiscreteCoordinates(0, 14),
            Orientation.DOWN,
            List.of(new DiscreteCoordinates(0, 13), new DiscreteCoordinates(0, 12), new DiscreteCoordinates(0, 11), new DiscreteCoordinates(0, 10)));

}
