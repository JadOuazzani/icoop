package ch.epfl.cs107.icoop.actor.object;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;
/**
 * PressurePlate represents a logic-based entity that can detect the presence of a movable entity.
 *
 * <p>The PressurePlate acts as a trigger mechanism, which is activated when a movable entity
 * occupies its cell. It implements the `Logic` interface to indicate its state (on/off).</p>
 */
 public class PressurePlate extends AreaEntity implements Logic {
    /** Sprite representing the pressure plate's visual appearance. */

    private Sprite sprite;
    /** Indicates whether there is an interactor (occupier) currently on the plate. */

    private boolean interactorHere;
    /** The movable entity currently occupying the pressure plate. */

    private MovableAreaEntity occupier;

    /**
     * Constructor for a PressurePlate.
     *
     * @param area               (Area): The area where the pressure plate is located.
     * @param positionPrincipale (DiscreteCoordinates): The main position of the pressure plate in the area.
     * @param orientation        (Orientation): The orientation of the pressure plate.
     */
    public PressurePlate(Area area, DiscreteCoordinates positionPrincipale, Orientation orientation) {
        super(area, orientation, positionPrincipale);
        sprite = new Sprite("GroundPlateOff", 1f, 1f, this);
        interactorHere = false;
    }
    /**
     * Sets the movable entity currently occupying the pressure plate.
     *
     * @param occupier (MovableAreaEntity): The entity currently on the plate.
     */
    public void setOccupier(MovableAreaEntity occupier) {
        this.occupier = occupier;
    }

    @Override
    /**
     * Updates the state of the pressure plate, including checking for an occupier.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (occupier != null && !(getCurrentMainCellCoordinates().equals(occupier.getCurrentMainCellCoordinates()))) {
            occupier = null;
        }
        if (occupier == null){
            setInteractorHere(false);
        }
    }
    /**
     * Sets whether an interactor is currently on the plate.
     *
     * @param interactorHere (boolean): True if an interactor is present, false otherwise.
     */
    public void setInteractorHere(boolean interactorHere) {
        this.interactorHere = interactorHere;
    }
    /**
     * Draws the pressure plate on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw.
     */
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    @Override
    /**
     * Gets the current cells occupied by the pressure plate.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }


    @Override
    /**
     * Indicates whether the pressure plate takes space in its cell.
     *
     * @return (boolean): Always returns false, as the pressure plate does not block movement.
     */
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    /**
     * Indicates whether the pressure plate is interactable by cell.
     *
     * @return (boolean): Always returns true, as it interacts in its cell.
     */
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    /**
     * Indicates whether the pressure plate is interactable by view.
     *
     * @return (boolean): Always returns false, as it does not interact through view.
     */
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if the interaction occurs in the same cell.
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    @Override
    /**
     * Indicates whether the pressure plate is active (on).
     *
     * @return (boolean): True if an interactor is present, false otherwise.
     */
    public boolean isOn() {
        return interactorHere;
    }

    @Override
    /**
     * Indicates whether the pressure plate is inactive (off).
     *
     * @return (boolean): True if no interactor is present, false otherwise.
     */ 
    public boolean isOff() {
        return !interactorHere;
    }

}