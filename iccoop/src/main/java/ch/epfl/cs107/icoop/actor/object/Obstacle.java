package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * Obstacle represents a physical barrier in the game world.
 *
 * <p>An Obstacle is an `AreaEntity` that takes space in its cell and can interact with other entities.
 * It is visually represented as a static sprite (e.g., a rock).</p>
 */
 public class Obstacle extends AreaEntity {
    /** Sprite representing the obstacle's appearance. */

    private Sprite sprite;

    /**
     * Constructor for an Obstacle.
     *
     * @param area        (Area): The area where the obstacle is located.
     * @param orientation (Orientation): The orientation of the obstacle.
     * @param position    (DiscreteCoordinates): The position of the obstacle in the area.
     */

    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("rock.2", 1f, 1f, this) ;
    }

    @Override
    /**
     * Updates the state of the obstacle.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    /**
     * Draws the obstacle.
     *
     * @param canvas (Canvas): The canvas on which to draw the obstacle.
     */
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    /**
     * Gets the current cells occupied by the obstacle.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    /**
     * Indicates whether the obstacle takes space in its cell.
     *
     * @return (boolean): Always returns true, as the obstacle blocks movement.
     */
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    /**
     * Indicates whether the obstacle is interactable by cell.
     *
     * @return (boolean): Always returns true, as the obstacle interacts in its cell.
     */
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    /**
     * Indicates whether the obstacle is interactable by view.
     *
     * @return (boolean): Always returns true, as the obstacle can be seen and interacted with.
     */
    public boolean isViewInteractable() {
        return true;
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
}

