package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Explosive;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * The Arrow class represents a projectile in the form of an arrow.
 * It handles movement, interactions with other objects, and animations.
 */
public class Arrow extends Projectile {
    private Animation animation; // Animation object for the arrow
    private ArrowInteractionHandler handler; // Interaction handler for the arrow


    /**
     * Constructor for the Arrow class.
     *
     * @param area        (Area): The area in which the arrow exists.
     * @param orientation (Orientation): The direction the arrow is facing.
     * @param position    (DiscreteCoordinates): The initial position of the arrow.
     * @param speed     (float): The speed of the arrow.
     * @param maxDistance (int): The maximum distance the arrow can travel.
     */
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, float speed, int maxDistance) {
        super(area, orientation, position, speed, maxDistance);
        if (orientation == Orientation.RIGHT) {
            animation = new Animation("icoop/arrowRight", 4, 1, 1, this, 32, 32, 4, true);
        } else if (orientation == Orientation.LEFT) {
            animation = new Animation("icoop/arrowLeft", 4, 1, 1, this, 32, 32, 4, true);
        }
        handler = new ArrowInteractionHandler();
    }

    /**
     * Updates the arrow's state.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Draws the arrow on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw the arrow on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }


    /**
     * Retrieves the current cells occupied by the arrow.
     *
     * @return (List < DiscreteCoordinates >): The list of cells currently occupied by the arrow.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    /**
     * Interacts with another interactable object.
     *
     * @param other             (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Determines if the arrow wants to interact with a cell.
     *
     * @return (boolean): True if the arrow wants to interact with a cell.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Accepts interactions from an interaction visitor.
     *
     * @param v (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Inner class FlecheInteractionHandler handles interactions specific to the arrow.
     */
    private class ArrowInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with a player:
         * The player loses HP if there is a cell interaction
         *
         * @param player (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.looseHp(2, "physique");
                getOwnerArea().unregisterActor(Arrow.this);

            }
        }

        /**
         * Interacts with an explosive:
         * activates the explosion if cellInteraction
         *
         *
         * @param explosive (Explosive): The explosive to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.setRetardateur(0);
                getOwnerArea().unregisterActor(Arrow.this);

            }
        }

        /**
         * Interacts with a foe.
         * The foe loses HP if there is a cell interaction
         *
         * @param foe (Foe): The foe to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (isCellInteraction) {
                foe.looseHp(1, "physique");
                getOwnerArea().unregisterActor(Arrow.this);
            }
        }
    }
}
