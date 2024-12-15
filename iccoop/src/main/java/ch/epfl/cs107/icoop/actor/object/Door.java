package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Door extends AreaEntity {
    private String airTransitoire;
    private List<DiscreteCoordinates> coordArrive;
    private Logic signal;
    private List<DiscreteCoordinates> coordAdditionelles;
    private DiscreteCoordinates positionPrincipale;


    public Door(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation) {
        super(area, orientation, positionPrincipale);
        this.airTransitoire = airTransitoire;
        this.coordArrive = coordArrive;
        this.signal = signal;
        this.positionPrincipale = positionPrincipale;
    }


    public Door(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation, DiscreteCoordinates... coordAdditionelles) {
        this(airTransitoire, signal, coordArrive, area, positionPrincipale, orientation);
        this.coordAdditionelles = List.of(coordAdditionelles);
    }

    public List<DiscreteCoordinates> getCoordArrive() {
        return coordArrive;
    }

    public String getAirTransitoire() {
        return airTransitoire;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        List<DiscreteCoordinates> allCoords = new ArrayList<>(coordAdditionelles);
        allCoords.add(positionPrincipale);
        return allCoords;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public Logic getSignal() {
        return signal;
    }
}
