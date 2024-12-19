package ch.epfl.cs107.icoop.actor.object;


/**
 * Portal represents a special type of door that includes dialogues for game events such as victory or requirements.
 *
 * <p>A Portal inherits from the `Door` class and adds functionalities for displaying game-related dialogues,
 * such as a victory message or a key requirement prompt. It is controlled by a `Logic` signal and interacts with other entities.</p>
 */

 import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.Collections;
import java.util.List;

public class Portal extends Door {
    /** Handler for managing game-related dialogues. */

    private DialogHandler game;

    /**
     * Constructor for a Portal.
     *
     * @param transitoryArea (String): The name of the transitory area linked by the portal.
     * @param signal         (Logic): The logic signal controlling the portal's activation.
     * @param arrivalCoords  (List<DiscreteCoordinates>): The coordinates of the arrival area.
     * @param area           (Area): The area where the portal is located.
     * @param mainPosition   (DiscreteCoordinates): The main position of the portal in the area.
     * @param orientation    (Orientation): The orientation of the portal.
     * @param game           (DialogHandler): The handler for managing game-related dialogues.
     */

    public Portal(String transitoryArea, Logic signal, List<DiscreteCoordinates> arrivalCoords, Area area, DiscreteCoordinates mainPosition, Orientation orientation, DialogHandler game) {
        super(transitoryArea, signal, arrivalCoords, area, mainPosition, orientation);
        this.game = game;
    }

    /**
     * Gets the victory dialogue associated with this portal.
     *
     * @return (Dialog): The victory message displayed when the portal is used successfully.
     */

    public Dialog getVictoryDialog() {
        return new Dialog("victory");
    }
    /**
     * Gets the key requirement dialogue associated with this portal.
     *
     * @return (Dialog): The message displayed when a key is required to use the portal.
     */
    public Dialog getRequiredDialog() {
        return new Dialog("key_required");
    }

    @Override
    /**
     * Gets the current cells occupied by the portal.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    /**
     * Updates the state of the portal.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
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
     * Indicates whether the portal is interactable by cell.
     *
     * @return (boolean): Always returns true, as the portal interacts in its cell.
     */
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    /**
     * Indicates whether the portal is interactable by view.
     *
     * @return (boolean): Always returns false, as the portal does not interact through view.
     */
    public boolean isViewInteractable() {
        return false;
    }
    /**
     * Gets the dialogue handler associated with the portal.
     *
     * @return (DialogHandler): The handler for managing dialogues.
     */
    public DialogHandler getGame() {
        return game;
    }
}
