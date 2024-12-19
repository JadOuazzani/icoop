package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

/**
 * The Projectile class represents a generic projectile entity in the game.
 * It handles movement, interaction, and lifecycle management.
 */
public abstract class Projectile extends MovableAreaEntity implements Interactor, Unstoppable {
    private final int distanceMax;
    private int remainingDistance;
    private float speed;
    private final static int MOVE_DURATION = 15;

    /**
     * Constructor for the Projectile class.
     *
     * @param area        (Area): The area where the projectile exists.
     * @param orientation (Orientation): The direction the projectile moves.
     * @param position    (DiscreteCoordinates): The initial position of the projectile.
     * @param speed       (float): The speed of the projectile.
     * @param distanceMax (int): The maximum distance the projectile can travel.
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, float speed, int distanceMax) {
        super(area, orientation, position);
        this.speed = speed;
        this.distanceMax = distanceMax;
        remainingDistance = distanceMax;


    }

    /**
     * Updates the projectile's state, including movement and lifecycle.
     *
     * @param deltatime (float): Time elapsed since the last update.
     */
    public void update(float deltatime) {
        super.update(deltatime);
        remainingDistance -= 1;
        if (remainingDistance == 0) {
            getOwnerArea().unregisterActor(this);
        }
        move((int) (MOVE_DURATION / speed));
    }


    /**
     * Retrieves the current cells occupied by the projectile.
     *
     * @return (List < DiscreteCoordinates >): A singleton list of the projectile's current main cell.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Retrieves the field of view cells for the projectile.
     *
     * @return (List<DiscreteCoordinates>): A list of cells in the projectile's field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * Checks if the projectile wants to interact with a cell.
     *
     * @return (boolean): Always false, as the projectile does not interact with cells directly.
     */
    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    /**
     * Checks if the projectile wants to interact via view.
     *
     * @return (boolean): Always false, as the projectile does not interact via view.
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Determines whether the projectile takes up cell space.
     *
     * @return (boolean): Always false, as the projectile does not block cell space.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Checks if the projectile can be interacted with via cell.
     *
     * @return (boolean): True if the projectile has remaining distance to travel.
     */
    @Override
    public boolean isCellInteractable() {
        return (remainingDistance != 0);
    }

    /**
     * Checks if the projectile can be interacted with via view.
     *
     * @return (boolean): Always false, as the projectile does not allow view interaction.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }


}
