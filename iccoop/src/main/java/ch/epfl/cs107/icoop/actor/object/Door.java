package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Door class represents a door that links areas and allows interaction.
 *
 * <p>A Door can have a transitory air (area), an arrival coordinate list,
 * and additional interaction logic through a signal.</p>
 */

public class Door extends AreaEntity {
    /** The name of the transitory area the door links to. */
    private String transitoryArea;
    /** Coordinates of the destination area when passing through the door. */
    private List<DiscreteCoordinates> arrivalCoords;
    /** Signal determining if the door is active or not. */
    private Logic signal;
    /** Additional discrete coordinates occupied by the door. */
    private List<DiscreteCoordinates> additionalCoords;
    /** The main coordinate position of the door. */
    private DiscreteCoordinates mainPosition;

    /**
     * Constructor for a door without additional occupied coordinates.
     *
     * @param transitoryArea       (String): The transitory area linked to the door
     * @param signal               (Logic): Logic signal that activates the door
     * @param arrivalCoords          (List<DiscreteCoordinates>): Destination coordinates in the next area
     * @param area                 (Area): Current area where the door is placed
     * @param mainPosition   (DiscreteCoordinates): Main position of the door
     * @param orientation          (Orientation): Orientation of the door
     */

    public Door(String transitoryArea, Logic signal, List<DiscreteCoordinates> arrivalCoords, Area area, DiscreteCoordinates mainPosition, Orientation orientation) {
        super(area, orientation, mainPosition);
        this.transitoryArea = transitoryArea;
        this.arrivalCoords = arrivalCoords;
        this.signal = signal;
        this.mainPosition = mainPosition;
    }

    /**
     * Constructor for a door with additional occupied coordinates.
     *
     * @param transitoryArea       (String): The transitory area linked to the door
     * @param signal               (Logic): Logic signal that activates the door
     * @param arrivalCoords          (List<DiscreteCoordinates>): Destination coordinates in the next area
     * @param area                 (Area): Current area where the door is placed
     * @param mainPosition   (DiscreteCoordinates): Main position of the door
     * @param orientation          (Orientation): Orientation of the door
     * @param additionalCoords   (DiscreteCoordinates...): Additional coordinates occupied by the door
     */
    public Door(String transitoryArea, Logic signal, List<DiscreteCoordinates> arrivalCoords, Area area, DiscreteCoordinates mainPosition, Orientation orientation, DiscreteCoordinates... additionalCoords) {
        this(transitoryArea, signal, arrivalCoords, area, mainPosition, orientation);
        this.additionalCoords = List.of(additionalCoords);
    }

    /**
     * Gets the destination coordinates.
     *
     * @return (List<DiscreteCoordinates>): List of destination coordinates.
     */

    public List<DiscreteCoordinates> getArrivalCoords() {
        return arrivalCoords;
    }

    /**
     * Gets the transitory area name.
     *
     * @return (String): Name of the transitory area.
     */

    public String getTransitoryArea() {
        return transitoryArea;
    }

    @Override
    /**
     * Gets the current cells occupied by the Door, including the main position and any additional coordinates.
     *
     * @return (List<DiscreteCoordinates>): List of occupied cells.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        List<DiscreteCoordinates> allCoords = new ArrayList<>(additionalCoords);
        allCoords.add(mainPosition);
        return allCoords;
    }

    @Override
    /**
     * Indicates whether the Door takes space in the grid.
     *
     * @return (boolean): Always returns false, as the Door does not block movement.
     */
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    /**
     * Indicates whether the Door can interact with entities in its cell.
     *
     * @return (boolean): Always true, as the Door is cell-interactable.
     */
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    /**
     * Indicates whether the Door can interact with entities in its field of view.
     *
     * @return (boolean): Always false, as the Door is not view-interactable.
     */
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    /**
     * Accepts an interaction from a visitor (entity).
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if the interaction occurs in the same cell, false otherwise.
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    /**
     * Draws the Door on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw.
     */
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * Gets the signal associated with the door.
     *
     * @return (Logic): Logic signal controlling the door's state.
     */

    public Logic getSignal() {
        return signal;
    }
}
