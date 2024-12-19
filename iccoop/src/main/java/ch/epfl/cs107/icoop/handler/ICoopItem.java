package ch.epfl.cs107.icoop.handler;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

/**
 * ICoopItem represents the various items available in the ICoop game.
 *
 * <p>This enum implements the `InventoryItem` interface and defines a set of items, each
 * with a unique name and associated sprite for rendering in the game. The items are stored
 * in the inventory system of the game.</p>
 */

 public enum ICoopItem implements InventoryItem {
    /** A sword item. */

    SWORD("Sword", "sword.icon"),
    /** A fire key item. */

    FIRE_KEY("Fire Key", "icoop/key_red"),
    /** A water key item. */

    WATER_KEY("Water Key","icoop/key_blue"),
    /** A fire staff item. */

    FIRE_STAFF("Fire Staff", "staff_fire.icon"),
    /** A water staff item. */

    WATER_STAFF("Water Staff", "staff_water.icon"),
    /** An explosive item. */

    EXPLOSIVE("Explosive", "icoop/explosive");

    /** Name of the sprite associated with this item. */

    private String spriteName;

    /** Name of the item. */

    private String name;
    /**
     * Constructor for an ICoopItem.
     *
     * @param name       (String): The name of the item.
     * @param spriteName (String): The sprite file associated with the item.
     */
    ICoopItem(String name, String spriteName) {
        this.spriteName = spriteName;
        this.name = name;
    }
    /**
     * Gets the sprite name associated with this item.
     *
     * @return (String): The sprite name.
     */
    public String getSpriteName() {
        return spriteName;
    }

    @Override
    /**
     * Gets the pocket ID for this item.
     *
     * <p>In this implementation, all items default to pocket ID `0`.</p>
     *
     * @return (int): The pocket ID, always `0` in this case.
     */
    public int getPocketId() {
        return 0; // sont tous à 0 par default
    }

    @Override
    /**
     * Gets the name of this item.
     *
     * @return (String): The name of the item.
     */
    public String getName() {
        return getSpriteName();
    }
}
