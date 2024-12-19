package ch.epfl.cs107.icoop.actor.object;




import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Key represents a collectable item that can unlock certain doors or interact with specific elements.
 *
 * <p>A Key is an `ElementalItem` associated with a specific element type, such as "water" or "fire".
 * It has a unique appearance depending on its type and interacts with other elemental entities.</p>
 */
public class Key extends ElementalItem {

    /** Sprite representing the key's visual appearance. */

    private Sprite sprite;


    /**
     * Constructor for a Key.
     *
     * @param area        (Area): The area where the key is located.
     * @param orientation (Orientation): The orientation of the key.
     * @param position    (DiscreteCoordinates): The position of the key in the area.
     * @param item        (String): The elemental type of the key ("water", "fire", etc.).
     */

    public Key(Area area, Orientation orientation, DiscreteCoordinates position, String item) {
        super(area, orientation, position, item);
        if (item.equals("water")) {
            sprite = new Sprite("icoop/key_blue", 0.6f, 0.6f, this);
        } else if (item.equals("fire")) {
            sprite = new Sprite("icoop/key_red", 0.6f, 0.6f, this);
        }
    }

    @Override
    /**
     * Draws the key.
     *
     * @param canvas (Canvas): The canvas on which to draw the key.
     */
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    /**
     * Updates the state of the key.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    /**
     * Retrieves the elemental type of the key.
     *
     * @return (String): The elemental type of the key.
     */
    public String item() {
        return getItem();
    }

    @Override
    /**
     * Gets the current cells occupied by the key.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    /**
     * Indicates whether the key is interactable by view.
     *
     * @return (boolean): Always returns false, as the key is not view-interactable.
     */
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    /**
     * Indicates whether the key is interactable by cell.
     *
     * @return (boolean): Always returns true, as the key interacts in its cell.
     */
    public boolean isCellInteractable() {
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

    @Override
    /**
     * Collects the key if the interacting entity matches its elemental type.
     *
     * @param item (ElementalEntity): The entity attempting to collect the key.
     */
    public void collectBy(ElementalEntity item) {
        super.collectBy(item);
    }
}
