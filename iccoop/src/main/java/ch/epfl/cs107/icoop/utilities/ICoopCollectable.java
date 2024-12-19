package ch.epfl.cs107.icoop.utilities;


/**
 * ICoopCollectable is an abstract class representing a collectible entity in the ICoop game.
 *
 * <p>This class extends `CollectableAreaEntity` and provides additional functionality for managing
 * collectible entities, such as handling their disappearance when collected. Collectibles do not
 * occupy cell space and are interactable within their cell.</p>
 */

 import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public abstract class ICoopCollectable extends CollectableAreaEntity {
    /**
     * Constructor for an ICoopCollectable.
     *
     * @param area        (Area): The area where the collectible is located.
     * @param orientation (Orientation): The orientation of the collectible.
     * @param position    (DiscreteCoordinates): The position of the collectible in the area.
     */
    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }
    /**
     * Handles the disappearance of the collectible when it is collected.
     *
     * <p>This method marks the entity as collected and unregisters it from its owner area if
     * it has been successfully collected.</p>
     */
    public void disappears() {
        this.collect();
        if (this.isCollected()) {
            this.getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    /**
     * Indicates whether the collectible takes cell space.
     *
     * @return (boolean): Always returns false, as collectibles do not block movement.
     */
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    /**
     * Indicates whether the collectible is interactable by cell.
     *
     * @return (boolean): Always returns true, as collectibles are interactable within their cell.
     */
    public boolean isCellInteractable() {
        return true;
    }
}
