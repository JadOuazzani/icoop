package ch.epfl.cs107.icoop.actor.object;

import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utilities.ICoopCollectable;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Heart represents a collectible item that increases the player's health when collected.
 *
 * <p>The Heart interacts with players to restore health points if their current health
 * is below the maximum. It features an animation while active.</p>
 */

public class Heart extends ICoopCollectable implements Interactor {
    /** Duration for the heart animation. */

    private static final int ANIMATION_DURATION = 24;
    /** Animation for the heart entity. */

    private final Animation animation;
    /** Interaction handler for the heart. */

    private final CoeurInteractionHandler handler;

    /**
     * Constructor for a Heart collectible.
     *
     * @param area        (Area): The area where the heart is located.
     * @param orientation (Orientation): The orientation of the heart.
     * @param position    (DiscreteCoordinates): The position of the heart in the area.
     */

    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16,
                ANIMATION_DURATION / 4, true);
        handler = new CoeurInteractionHandler();
    }

    /**
     * Gets the current cells occupied by the heart.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */


    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override

    /**
     * Gets the cells in the field of view of the heart.
     *
     * @return (List<DiscreteCoordinates>): List of cells in the field of view.
     */
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    /**
     * Indicates whether the heart is interactable by view.
     *
     * @return (boolean): Always returns false, as the heart is not view-interactable.
     */
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if the interaction occurs in the same cell.
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Indicates whether the heart wants cell interactions.
     *
     * @return (boolean): Always returns true, as the heart interacts in its cell.
     */
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Indicates whether the heart wants view interactions.
     *
     * @return (boolean): Always returns false, as the heart does not interact through view.
     */

    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Handles interactions with other interactables.
     *
     * @param other             (Interactable): The interactable entity.
     * @param isCellInteraction (boolean): True if the interaction occurs in the same cell.
     */

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    /**
     * Updates the state of the heart, including animation and disappearance after collection.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        animation.update(deltaTime);
        if (isCollected()) {
            disappears();
        }
    }
    @Override
    /**
     * Draws the heart animation
     *
     * @param canvas (Canvas): The canvas on which to draw.
     */
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }
    /**
     * CoeurInteractionHandler defines how the heart interacts with the player.
     */
    private class CoeurInteractionHandler implements ICoopInteractionVisitor {
        @Override
        /**
         * Interacts with the player to restore health.
         *
         * <p>The heart restores 1 health point to the player if their current health is below
         * the maximum health. Once collected, the heart disappears.</p>
         *
         * @param player            (ICoopPlayer): The player interacting with the heart.
         * @param isCellInteraction (boolean): True if the interaction occurs in the same cell.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsCellInteraction() && player.getHealth().getHealthPoints() != player.getHealth().getMaxHealth()) { //nvl option: npp recup coeur quand max HP
                player.getHealth().increase(1);
                collect();
            }
        }
    }

}




