package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Explosive;
import ch.epfl.cs107.icoop.actor.object.Rochers;
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
 * The WaterBall class represents a projectile that deals water-based damage.
 * It interacts with explosives, foes, and rocks to trigger various effects.
 */
public class WaterBall extends Projectile {
    private Animation animation;
    private WaterBall.WaterBallInteractionHandler handler;
    private final static int ANIMATION_DURATION = 12;

    /**
     * Constructor for the FireBall class.
     *
     * @param area        (Area): The area where the FireBall exists.
     * @param orientation (Orientation): The direction the FireBall is moving.
     * @param position    (DiscreteCoordinates): The initial position of the FireBall.
     * @param speed       (float): The speed of the FireBall.
     * @param distanceMax (int): The maximum distance the FireBall can travel.
     */
    public WaterBall(Area area, Orientation orientation, DiscreteCoordinates position, float speed, int distanceMax) {
        super(area, orientation, position, speed, distanceMax);
        animation = new Animation("icoop/magicWaterProjectile", 4, 1, 1, this, 32, 32,
                ANIMATION_DURATION / 4, true);

        handler = new WaterBallInteractionHandler();
    }

    /**
     * Updates the WaterBall's state.
     *
     * @param deltatime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltatime) {
        super.update(deltatime);
        animation.update(deltatime);
    }

    /**
     * Draws the WaterBall on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw the FireBall on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Retrieves the current cells occupied by the FireBall.
     *
     * @return (List < DiscreteCoordinates >): The list of cells currently occupied by the FireBall.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    /**
     * Interacts with another interactable object.
     *
     * @param other             (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Determines if the FireBall wants to interact with a cell.
     *
     * @return (boolean): True, as FireBall always wants to interact with cells.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }


    /**
     * Accepts interactions from an interaction visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    /**
     * Inner class handling interactions for the WaterBall.
     */
    private class WaterBallInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with an explosive, triggering its detonation timer.
         *
         * @param explosive         (Explosive): The explosive to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.setRetardateur(0);
                getOwnerArea().unregisterActor(WaterBall.this);

            }
        }

        /**
         * Interacts with a foe, dealing water damage.
         *
         * @param foe (Foe): The foe to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (isCellInteraction) {
                foe.looseHp(1, "water");
                getOwnerArea().unregisterActor(WaterBall.this);
            }
        }

        /**
         * Interacts with a rock, destroying it upon impact.
         *
         * @param rochers (Rochers): The rock to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(Rochers rochers, boolean isCellInteraction) {
            if (isCellInteraction) {
                rochers.setDetruit(true);
                getOwnerArea().unregisterActor(WaterBall.this);

            }
        }


    }
}


