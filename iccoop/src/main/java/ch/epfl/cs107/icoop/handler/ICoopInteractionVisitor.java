package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    default void interactWith(Door door, boolean isCellInteraction) {}
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}
}
