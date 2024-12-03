package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ElementalItem;
import ch.epfl.cs107.icoop.utilities.ICoopBehavior;
import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    default void interactWith(Door door, boolean isCellInteraction) {}
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}
    default void interactWith(Obstacle obstacle, boolean isCellInteraction){}
    default void interactWith(Rochers rochers, boolean isCellInteraction){}
    default void interactWith(Explosif explosif, boolean isCellInteraction){}
    default void interactWith(Orb orb, boolean isCellInteraction){};
}
