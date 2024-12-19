package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.foe.*;
import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.utilities.ICoopBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * ICoopInteractionVisitor defines interaction methods for entities in the ICoop game.
 *
 * <p>This interface extends the `AreaInteractionVisitor` and provides default interaction
 * methods for a wide range of game entities, including objects, characters, and environment elements.
 * Each interaction method can be overridden as needed to define specific behavior during interactions.</p>
 */
public interface ICoopInteractionVisitor extends AreaInteractionVisitor {

    /**
     * Interaction with a Door.
     *
     * @param door               (Door): The door entity to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Door door, boolean isCellInteraction) {}

    /**
     * Interaction with an ICoopPlayer.
     *
     * @param player             (ICoopPlayer): The player entity to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}

    /**
     * Interaction with an ICoopCell.
     *
     * @param cell               (ICoopBehavior.ICoopCell): The cell to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}

    /**
     * Interaction with an Obstacle.
     *
     * @param obstacle           (Obstacle): The obstacle to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {}

    /**
     * Interaction with a Rock.
     *
     * @param rock               (Rock): The rock to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Rock rock, boolean isCellInteraction) {}

    /**
     * Interaction with an Explosive.
     *
     * @param explosive          (Explosive): The explosive entity to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Explosive explosive, boolean isCellInteraction) {}

    /**
     * Interaction with an Orb.
     *
     * @param orb                (Orb): The orb to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Orb orb, boolean isCellInteraction) {}

    /**
     * Interaction with an ElementalWall.
     *
     * @param wall               (ElementalWall): The wall to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(ElementalWall wall, boolean isCellInteraction) {}

    /**
     * Interaction with a Heart.
     *
     * @param heart              (Heart): The heart to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Heart heart, boolean isCellInteraction) {}

    /**
     * Interaction with a PressurePlate.
     *
     * @param plate              (PressurePlate): The plate to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(PressurePlate plate, boolean isCellInteraction) {}

    /**
     * Interaction with a Foe.
     *
     * @param foe                (Foe): The foe to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Foe foe, boolean isCellInteraction) {}

    /**
     * Interaction with Fire.
     *
     * @param fire               (Fire): The fire to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Fire fire, boolean isCellInteraction) {}

    /**
     * Interaction with a BombFoe.
     *
     * @param bombFoe            (BombFoe): The bomb foe to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(BombFoe bombFoe, boolean isCellInteraction) {}

    /**
     * Interaction with a Staff.
     *
     * @param staff              (Staff): The staff to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Staff staff, boolean isCellInteraction) {}

    /**
     * Interaction with a FireBall.
     *
     * @param fireBall           (FireBall): The fireball to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(FireBall fireBall, boolean isCellInteraction) {}

    /**
     * Interaction with a WaterBall.
     *
     * @param waterBall          (WaterBall): The waterball to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(WaterBall waterBall, boolean isCellInteraction) {}

    /**
     * Interaction with a Key.
     *
     * @param key                (Key): The key to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Key key, boolean isCellInteraction) {}

    /**
     * Interaction with a Portal.
     *
     * @param portal             (Portal): The portal to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Portal portal, boolean isCellInteraction) {}

    /**
     * Interaction with an Arrow (arrow).
     *
     * @param arrow             (Arrow): The arrow to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Arrow arrow, boolean isCellInteraction) {}

    /**
     * Interaction with Coins.
     *
     * @param coins              (Coin): The coins to interact with.
     * @param isCellInteraction  (boolean): True if the interaction occurs in the same cell.
     */
    default void interactWith(Coin coins, boolean isCellInteraction) {}
}
