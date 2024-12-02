package ch.epfl.cs107.icoop;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Positionable;

import javax.swing.text.Position;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    public void disparition() {
        if (isCollected()) {
            this.getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }
}
