package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.Collections;
import java.util.List;

public class Portail extends Door {
    private DialogHandler jeu;

    public Portail(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation, DialogHandler jeu) {
        super(airTransitoire, signal, coordArrive, area, positionPrincipale, orientation);
        this.jeu = jeu;
    }

    public Dialog getVictoryDialog() {
        return new Dialog("victory");
    }

    public Dialog getRequiredDialog() {
        return new Dialog("key_required");
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    public DialogHandler getJeu() {
        return jeu;
    }
}
