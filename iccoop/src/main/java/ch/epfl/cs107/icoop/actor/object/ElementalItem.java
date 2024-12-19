package ch.epfl.cs107.icoop.actor.object;


/**
 * ElementalItem is an abstract class that represents an elemental collectable item.
 *
 * <p>An ElementalItem is both an `ICoopCollectable` and a `Logic` entity,
 * meaning it can be collected by players and used in logical conditions.</p>
 *
 * <p>This class provides the base implementation for items with a specific elemental type
 * and logic-based interactions. Subclasses can extend this functionality as needed.</p>
 */

import ch.epfl.cs107.icoop.utilities.ICoopCollectable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {
    /** The elemental type or name of the item. */
    private String item;

    /**
     * Default constructor for an ElementalItem.
     *
     * @param area       Area: The area where the item is located.
     * @param orientation Orientation: The orientation of the item in the area.
     * @param position   DiscreteCoordinates: The position of the item in the area.
     * @param item       String: The elemental type or name of the item.
     */

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, String item) {
        super(area, orientation, position);
        this.item = item;
    }

    /**
     * Collects this item if the provided `ElementalEntity` has the matching elemental type.
     *
     * @param item ElementalEntity: The entity attempting to collect the item.
     */

    public void collectBy(ElementalEntity item) {
        if (getItem().equals(item.item())) {
            disappears();
        }
    }

    /**
     * Checks if the item has been collected.
     *
     * @return boolean: True if the item has been collected, false otherwise.
     */

    @Override
    public boolean isCollected() {
        return super.isCollected();
    }

    /**
     * Checks if the item's logic state is "on".
     *
     * @return boolean: True if the item is collected, false otherwise.
     */

    @Override
    public boolean isOn() {
        return isCollected();
    }

    /**
     * Checks if the item's logic state is "off".
     *
     * @return boolean: True if the item is not collected, false otherwise.
     */

    @Override
    public boolean isOff() {
        return !isCollected();
    }

    /**
     * Gives the elemental type or name of this item.
     *
     * @return String: The item's elemental type or name.
     */


    public String getItem() {
        return item;
    }

}
