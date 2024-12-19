package ch.epfl.cs107.icoop.actor.object;


/**
 * Teleportation represents a door-like entity that allows transition to another area.
 *
 * <p>A Teleportation is a specialized `Door` that displays a visual marker when active,
 * allowing entities to transition to a specified area when triggered. The marker is controlled by a `Logic` signal.</p>
 */
 import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Teleportation extends Door {
    /** Sprite representing the teleportation marker. */

    private Sprite sprite;

    /**
     * Constructor for a Teleportation.
     *
     * @param transitoryArea (String): The name of the transitory area linked by the teleportation.
     * @param signal         (Logic): The logic signal controlling the teleportation's activation.
     * @param arrivalCoords  (List<DiscreteCoordinates>): The coordinates of the arrival area.
     * @param area           (Area): The area where the teleportation is located.
     * @param mainPosition   (DiscreteCoordinates): The main position of the teleportation in the area.
     * @param orientation    (Orientation): The orientation of the teleportation.
     */

    public Teleportation(String transitoryArea, Logic signal, List<DiscreteCoordinates> arrivalCoords, Area area, DiscreteCoordinates mainPosition, Orientation orientation) {
        super(transitoryArea, signal, arrivalCoords, area, mainPosition, orientation);
        sprite = new RPGSprite("shadow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

    @Override
    /**
     * Gets the current cells occupied by the teleportation.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    /**
     * Draws the teleportation marker on the canvas if the teleportation is active.
     *
     * @param canvas (Canvas): The canvas on which to draw the teleportation marker.
     */

    public void draw(Canvas canvas) {
        if (getSignal().isOn()) {
            sprite.draw(canvas);
        }

    }
}
