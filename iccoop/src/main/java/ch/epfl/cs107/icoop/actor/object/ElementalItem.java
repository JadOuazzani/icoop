package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.utilities.ICoopCollectable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {
    private String item;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, String item) {
        super(area, orientation, position);
        this.item = item;
    }

    public void collectBy(ElementalEntity item) {
        if (getItem().equals(item.item())) {
            disparition();
        }
    }

    @Override
    public boolean isCollected() {
        return super.isCollected();
    }


    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isOff() {
        return false;
    }

    public String getItem() {
        return item;
    }

}
