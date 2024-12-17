package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.foe.logMonster;
import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class SanctumEntrance extends ICoopArea {

    public SanctumEntrance() {

    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        logMonster monster = new logMonster(this, Orientation.DOWN, new DiscreteCoordinates(10, 13), List.of("fire","physique"));
        registerActor(monster);
//
    }

    @Override
    public String getTitle() {
        return "SanctumEntrance";
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(9, 8), new DiscreteCoordinates(8, 7));
    }


}
