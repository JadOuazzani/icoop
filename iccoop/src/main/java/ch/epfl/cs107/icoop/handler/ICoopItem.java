package ch.epfl.cs107.icoop.handler;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

public enum ICoopItem implements InventoryItem {
    SWORD("Sword", "sword.icon"),
    FIRE_KEY("Fire Key", "icoop/key_red"),
    WATER_KEY("Water Key","icoop/key_blue"),
    FIRE_STAFF("Fire Staff", "staff_fire.icon"),
    WATER_STAFF("Water Staff", "staff_water.icon"),
    EXPLOSIVE("Explosive", "icoop/explosive");

    private String spriteName;
    private String name;

    ICoopItem(String name, String spriteName) {
        this.spriteName = spriteName;
        this.name = name;
    }

    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public int getPocketId() {
        return 0; // sont tous à 0 par default
    }

    @Override
    public String getName() {
        return getSpriteName();
    }
}
