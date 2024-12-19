package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * ElementalWall is an obstacle that interacts with the player based on elemental logic.
 *
 * <p>An ElementalWall can block the player, cause damage, or disappear based on its associated
 * elemental type and logical activation state. It implements `ElementalEntity` and acts as
 * an `Interactor` to handle player interactions.</p>
 */

public class ElementalWall extends Obstacle implements ElementalEntity, Interactor {
    /** The array of sprites representing the wall in different orientations. */

    private Sprite[] wallSprites;
    /** The elemental type associated with this wall. */

    private String item;
    /** Determines whether the wall blocks movement. */

    private boolean takeCellSpace;
    /** The interaction handler for the wall. */

    private WallInteractionHandler handler;
    /** Logic signal indicating if the wall is active. */

    private Logic active;
    /** Indicates if the wall is destroyed. */

    private boolean destroyed;

    /**
     * Enumeration of the possible types of elemental walls.
     */

    public enum WallType {
        EAU("water", "water_wall"),
        FEU("fire", "fire_wall");

        /** The name of the sprite for this wall type. */

        private String spriteName;
        /** The elemental type associated with this wall. */

        private String item;

        /**
         * Constructor for WallType.
         *
         * @param item       (String): The elemental type of the wall.
         * @param spriteName (String): The name of the sprite for this wall type.
         */

        WallType(String item, String spriteName) {
            this.spriteName = spriteName;
            this.item = item;
        }
        /**
         * Gets the sprite name for this wall type.
         *
         * @return (String): The name of the sprite.
         */

        public String getSpriteName() {
            return spriteName;
        }


        /**
         * Gets the elemental type for this wall type.
         *
         * @return (String): The elemental type.
         */

        public String getItem() {
            return item;
        }
    }

    /**
     * Constructor for an ElementalWall.
     *
     * @param area        (Area): The area where the wall is located.
     * @param orientation (Orientation): The orientation of the wall.
     * @param position    (DiscreteCoordinates): The position of the wall.
     * @param actif       (Logic): The logical state determining if the wall is active.
     * @param wallType    (WallType): The type of the wall (element and sprite).
     */

    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, Logic actif, WallType wallType) {
        super(area, orientation, position);
        item = wallType.item;
        wallSprites = RPGSprite.extractSprites(wallType.getSpriteName(), 4, 1, 1, this, Vector.ZERO, 256, 256);
        handler = new WallInteractionHandler();
        this.active = actif;
        destroyed = false;
    }

    /**
     * Gets the logical state of the wall.
     *
     * @return (Logic): The logic controlling the wall's activation.
     */

    public Logic getActif() {
        return active;
    }

    @Override
    /**
     * Updates the wall's state, unregistering it if it is destroyed.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (destroyed) {
            getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    /**
     * Draws the wall if it is active.
     *
     * @param canvas (Canvas): The canvas on which to draw the wall.
     */
    public void draw(Canvas canvas) {
        if (active.isOn()) {
            wallSprites[getOrientation().ordinal()].draw(canvas);
        }
    }



    @Override
    /**
     * Gets the elemental type of the wall.
     *
     * @return (String): The elemental type of the wall.
     */
    public String item() {
        return item;
    }

    @Override

    /**
     * Gets the neighboring cells in the wall's field of view.
     *
     * @return (List<DiscreteCoordinates>): List of neighboring cells.
     */
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return this.getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override

    /**
     * Indicates whether the wall wants cell interactions.
     *
     * @return (boolean): Always true, as the wall interacts in its cell.
     */
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    /**
     * Indicates whether the wall wants view interactions.
     *
     * @return (boolean): Always false, as the wall does not interact through view.
     */
    public boolean wantsViewInteraction() {
        return false;
    }


    @Override
    /**
     * Indicates whether the wall blocks movement.
     *
     * @return (boolean): True if the wall blocks movement, false otherwise.
     */
    public boolean takeCellSpace() {
        return takeCellSpace;
    }
    /**
     * Sets whether the wall blocks movement.
     *
     * @param variable (boolean): True to make the wall block movement, false otherwise.
     */
    public void setTakeCellSpace(boolean variable) {
        takeCellSpace = variable;
    }

    @Override
    /**
     * Handles interactions with other interactables.
     *
     * @param other             (Interactable): The interactable entity.
     * @param isCellInteraction (boolean): True if the interaction is in the same cell.
     */
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if the interaction is in the same cell.
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
    /**
     * Sets the destroyed state of the wall.
     *
     * @param destroyed (boolean): True to destroy the wall, false otherwise.
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * WallInteractionHandler handles interactions with the wall.
     */

    private class WallInteractionHandler implements ICoopInteractionVisitor {
        /**
         * Interacts with a player. Blocks movement or causes damage based on the elemental type.
         *
         * @param player            (ICoopPlayer): The player interacting with the wall.
         * @param isCellInteraction (boolean): True if the interaction is in the same cell.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            setTakeCellSpace(!player.getItem().equals(item) && active.isOn());
            if (isCellInteraction && active.isOn()) {
                player.looseHp(1, item);
            }
        }
    }
}

