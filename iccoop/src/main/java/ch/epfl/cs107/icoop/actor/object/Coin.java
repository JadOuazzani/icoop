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
 * Represents a Coin object in the iCoop game.
 * A Coin can be collected by a player and increments the player's global coin count
 */


public class Coin extends ICoopCollectable implements Interactor {
    private static final int ANIMATION_DURATION = 24;
    private final Animation animation;
    private final CoinInteractionHandler handler;


    /**
     * Creates a new coin at the specified position and orientation.
     *
     * @param area the area the coin belongs to
     * @param orientation the orientation of the coin
     * @param position the position of the coin in the area
     */


    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        animation = new Animation("icoop/coin", 4, 1, 1, this, 16, 16,
                ANIMATION_DURATION / 4, true);
        handler = new CoinInteractionHandler();
    }


    /**
     * Returns the current cells the coin occupies.
     *
     * @return a list of discrete coordinates where the coin is located
     */


    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    /**
     * Returns the cells in the coin's field of view.
     *
     * @return a list of discrete coordinates in the coin's field of view
     */

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    /**
     * Determines if the coin can be interacted with in the view.
     *
     * @return false as the coin is not view interactable
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Accepts an interaction from the area interaction visitor.
     *
     * @param v the area interaction visitor
     * @param isCellInteraction true if the interaction is with the cell
     */

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Determines if the coin wants to interact with a cell.
     *
     * @return true, as the coin wants to interact with the cell
     */

    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Determines if the coin wants to interact with the view.
     *
     * @return false, as the coin does not interact with the view
     */

    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Interacts with another interactable object
     *
     * @param other the other interactable object
     * @param isCellInteraction true if there is an interaction with the cell
     */

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Updates the coin's animation.
     * If the coin is collected, it disappears.
     *
     * @param deltaTime the time since the last update
     */

    public void update(float deltaTime) {
        animation.update(deltaTime);
        if (isCollected()) {
            disappears();
        }
    }

    /**
     * Draws the coin animation
     *
     * @param canvas the canvas to draw on
     */

    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Handles interactions with a player.
     * If the player interacts with the coin and it is not already collected, the coin is collected.
     */

    private class CoinInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {

            if (!isCollected() && wantsCellInteraction()) {
                player.setGlobalCoinCounter(player.getGlobalCoinCount()+1);
                collect();
            }
        }
    }


}


