package ch.epfl.cs107.icoop.actor.object;


/**
 * Orb represents a collectible item with elemental properties and specific abilities.
 *
 * <p>An Orb is an `ElementalItem` that can be collected by players or entities matching its elemental type.
 * Each orb has a unique animation, appearance, and dialogue associated with its type.</p>
 */

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Orb represents a collectible item with elemental properties and specific abilities.
 *
 * <p>An Orb is an `ElementalItem` that can be collected by players or entities matching its elemental type.
 * Each orb has a unique animation, appearance, and dialogue associated with its type.</p>
 */

 public class Orb extends ElementalItem {
    /** Number of frames in the orb's animation. */

    private final static int ANIMATION_FRAMES = 6;
    /** Duration of the orb's animation (in frames). */

    private final static int ANIMATION_DURATION = 24;
    /** Array of sprites used in the orb's animation. */

    final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
    /** Animation for the orb's appearance. */

    private final Animation animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES, sprites);

    /** Handler for managing in-game dialogues. */
    private final DialogHandler game;
    /** Dialogue associated with this orb. */
    private final Dialog dialog;

    /**
     * Enumeration of orb types with their properties.
     */

    public enum OrbType {
        WATER("water", new Dialog("orb_water_msg"), 0),
        SPEED("speed", new Dialog("orb_speed_msg"), 32),
        FIRE("fire", new Dialog("orb_fire_msg"), 64);


        /** Element associated with the orb type. */
        private final String element;
        /** Vertical offset in the sprite sheet for this orb type. */

        private final int spriteYDelta;
        /** Dialogue associated with the orb type. */

        private Dialog dialog;

        /**
         * Constructor for an OrbType.
         *
         * @param item         (String): The elemental type of the orb.
         * @param dialog       (Dialog): The dialogue associated with the orb.
         * @param spriteYDelta (int): The vertical offset in the sprite sheet.
         */

        OrbType(String item, Dialog dialog, int spriteYDelta) {
            this.element = item;
            this.spriteYDelta = spriteYDelta;
            this.dialog = dialog;
        }

        /**
         * Gets the vertical offset in the sprite sheet for this orb type.
         *
         * @return (int): The vertical offset in pixels.
         */

        public int getSpriteYDelta() {
            return spriteYDelta;
        }

        /**
         * Gets the elemental type associated with this orb type.
         *
         * @return (String): The elemental type of the orb.
         */

        public String getItem() {
            return element;
        }

        /**
         * Resets the dialogue for this orb type.
         */


        public void resetDialogue() {
            switch (getItem()) {
                case "water":
                    this.dialog = new Dialog("orb_water_msg");
                    break;
                case "fire":
                    this.dialog = new Dialog("orb_fire_msg");
                    break;
                case "speed":
                    this.dialog = new Dialog("orb_speed_msg");
                    break;
            }

        }
    }

    /**
     * Constructor for an Orb.
     *
     * @param area        (Area): The area where the orb is located.
     * @param orientation (Orientation): The orientation of the orb.
     * @param position    (DiscreteCoordinates): The position of the orb in the area.
     * @param orbType     (OrbType): The type of the orb (element, sprite, dialogue).
     * @param game        (DialogHandler): The handler for managing dialogues.
     */

    public Orb(Area area, Orientation orientation, DiscreteCoordinates position, OrbType orbType, DialogHandler game) {
        super(area, orientation, position, orbType.getItem());
        this.game = game;
        orbType.resetDialogue();
        this.dialog = orbType.dialog;
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, orbType.getSpriteYDelta(), 32, 32));
        }
    }

    @Override
    /**
     * Gets the current cells occupied by the orb.
     *
     * @return (List<DiscreteCoordinates>): List of cells currently occupied.
     */
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    /**
     * Indicates whether the orb is interactable by view.
     *
     * @return (boolean): Always returns false, as the orb does not interact through view.
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

    @Override
    /**
     * Gets the elemental type of the orb.
     *
     * @return (String): The elemental type of the orb.
     */
    public String item() {
        return getItem();
    }

    @Override
    /**
     * Draws the orb's animation on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw the orb.
     */
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    /**
     * Updates the orb's animation.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }

    @Override
    /**
     * Collects the orb if the interacting entity matches its elemental type.
     *
     * @param item (ElementalEntity): The entity attempting to collect the orb.
     */
    public void collectBy(ElementalEntity item) {
        super.collectBy(item);
    }
    /**
     * Gets the dialogue handler associated with the orb.
     *
     * @return (DialogHandler): The handler for managing dialogues.
     */
    public DialogHandler getGame() {
        return game;
    }
    /**
     * Gets the dialogue associated with this orb.
     *
     * @return (Dialog): The dialogue of the orb.
     */
    public Dialog getDialog() {
        return dialog;
    }
}
