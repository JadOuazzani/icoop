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
 * The Fire class represents a projectile in the form of fire or water.
 * It interacts with players, explosives, and foes to deal damage or trigger effects.
 */
public class Fire extends Projectile {
    private Animation animation;
    private FlammesInteractionHandler handler;
    private String type;

    /**
     * Constructor for the Fire class.
     *
     * @param area (Area): The area where the fire exists.
     * @param orientation (Orientation): The direction the fire is moving.
     * @param position (DiscreteCoordinates): The initial position of the fire.
     * @param vitesse (float): The speed of the fire.
     * @param distancemax (int): The maximum distance the fire can travel.
     * @param name (String): The name of the animation sprite.
     * @param type (String): The type of the fire (e.g., "fire", "water").
     */
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position, float vitesse, int distancemax, String name, String type) {
        super(area, orientation, position, vitesse, distancemax);
        this.type = type;
        animation = new Animation(name, 7, 1, 1, this, 16, 16, 4, true);
        handler = new FlammesInteractionHandler();
    }

    /**
     * Retrieves the type of the fire.
     *
     * @return (String): The type of the fire (e.g., "fire", "water").
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the fire's state.
     *
     * @param deltatime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltatime) {
        super.update(deltatime);
        animation.update(deltatime);
    }

    /**
     * Draws the fire projectile on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw the fire on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Retrieves the current cells occupied by the fire.
     *
     * @return (List<DiscreteCoordinates>): The list of cells currently occupied by the fire.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    /**
     * Interacts with another interactable object.
     *
     * @param other (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Determines if the fire wants to interact with a cell.
     *
     * @return (boolean): True, as fire always wants to interact with cells.
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
     * Inner class handling interactions for the Fire projectile.
     */
    private class FlammesInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with a player, dealing damage based on the fire's type.
         *
         * @param player (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.looseHp(1, getType());
            }
        }

        /**
         * Interacts with an explosive, triggering its detonation timer.
         *
         * @param explosive (Explosive): The explosive to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.setRetardateur(0);
            }
        }

        /**
         * Interacts with a foe, dealing damage based on the fire's type.
         *
         * @param foe (Foe): The foe to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (isCellInteraction) {
                foe.looseHp(1, getType());
            }
        }
    }
}
