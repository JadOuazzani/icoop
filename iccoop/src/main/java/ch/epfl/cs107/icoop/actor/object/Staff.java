package ch.epfl.cs107.icoop.actor.object;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * Staff represents a collectible elemental item with animated visuals.
 *
 * <p>A Staff is an `ElementalItem` that cycles through a series of animation frames to display
 * a dynamic visual representation. Each staff has a specific type, such as "water" or "fire",
 * with unique animations.</p>
 */

 public class Staff extends ElementalItem {

    /** Array of sprites representing the animation frames of the staff. */

    private final Sprite[] sprites;
    /** Index of the currently displayed animation frame. */

    private int currentImage;
    /** Counter to control the duration of each animation frame. */

    private int imageCounter;
    /** Threshold to switch to the next animation frame. */

    private final static int IMAGE_LIMIT = 4;
    /** Total number of animation frames for the staff. */

    private final static int IMAGE_NUMBER = 8;

    /**
     * Enumeration of staff types, defining their elemental type and associated sprite.
     */

    public enum StaffType {
        WATER("water", "staff_water"),
        FIRE("fire", "staff_fire");

        /** Name of the sprite file for this staff type. */

        private final String spriteName;
        /** Elemental type of the staff. */

        private final String item;

        /**
         * Constructor for a StaffType.
         *
         * @param item       (String): The elemental type of the staff.
         * @param spriteName (String): The name of the sprite file associated with the staff type.
         */

        StaffType(String item, String spriteName) {
            this.spriteName = spriteName;
            this.item = item;
        }

        /**
         * Gets the name of the sprite file associated with this staff type.
         *
         * @return (String): The sprite file name.
         */

        public String getSpriteName() {
            return spriteName;
        }

        /**
         * Gets the elemental type of the staff.
         *
         * @return (String): The elemental type.
         */

        public String getItem() {
            return item;
        }
    }

    /**
     * Constructor for a Staff.
     *
     * @param area        (Area): The area where the staff is located.
     * @param orientation (Orientation): The orientation of the staff.
     * @param position    (DiscreteCoordinates): The position of the staff in the area.
     * @param staffType   (StaffType): The type of the staff (e.g., water or fire).
     */
    public Staff(Area area, Orientation orientation, DiscreteCoordinates position, StaffType staffType) {
        super(area, orientation, position, staffType.getItem());

        sprites = new Sprite[IMAGE_NUMBER];

        for (int i = 0; i < IMAGE_NUMBER; i++) {
            sprites[i] = new RPGSprite(
                    staffType.getSpriteName(),
                    2, 2,
                    this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0)
            );
        }

        currentImage = 0;
        imageCounter = 0;
    }

    @Override
    /**
     * Updates the animation of the staff by cycling through its frames.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        imageCounter++;
        if (imageCounter >= IMAGE_LIMIT) {
            imageCounter = 0;
            currentImage++;
            if (currentImage >= IMAGE_NUMBER) {
                currentImage = 0;
            }
        }
    }

    @Override
    /**
     * Draws the current animation frame of the staff on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw the staff.
     */
    public void draw(Canvas canvas) {
        sprites[currentImage].draw(canvas);
    }

    @Override
    /**
     * Gets the elemental type of the staff.
     *
     * @return (String): The elemental type of the staff.
     */
    public String item() {
        return super.getItem();
    }


    @Override
    /**
     * Gets the current cells occupied by the staff.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    /**
     * Indicates whether the staff is interactable by view.
     *
     * @return (boolean): Always returns false, as the staff does not interact through view.
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
}