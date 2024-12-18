package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.foe.*;
import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.utilities.ICoopBehavior;
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
    default void interactWith(ElementalWall wall, boolean isCellInteraction){};
    default void interactWith(Heart heart, boolean isCellInteraction){}
    default void interactWith(PressurePlate plate, boolean isCellInteraction){}
    default void interactWith(Foe foe, boolean isCellInteraction){}
    default void interactWith(Fire fire, boolean isCellInteraction){}
    default void interactWith(BombFoe bombFoe, boolean isCellInteraction){}
    default void interactWith(Staff staff, boolean isCellInteraction){}
    default void interactWith(FireBall fireBall, boolean isCellInteraction) {}
    default void interactWith(WaterBall waterBall, boolean isCellInteraction) {}
    default void interactWith(Key key, boolean isCellInteraction){}
    default void interactWith(Portail portail, boolean isCellInteraction) {}
    default void interactWith(Fleche fleche, boolean isCellInteraction) {}
    default void interactWith(Coin coins,boolean isCellInteraction){}
}
