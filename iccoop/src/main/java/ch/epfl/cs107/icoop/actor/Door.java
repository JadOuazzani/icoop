package ch.epfl.cs107.icoop.actor;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Door extends AreaEntity {
    private String airTransitoire;
    private List<DiscreteCoordinates> coordArrive;
    private Logic signal;

    public Door(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation) {
        super(area, orientation, positionPrincipale);
        this.airTransitoire = airTransitoire;
        this.coordArrive = coordArrive;
        this.signal = signal;
    }

    public Door(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation, DiscreteCoordinates... coordAdditionelles) {
        this(airTransitoire, signal, coordArrive, area, positionPrincipale, orientation);
        this.coordAdditionelles = List.of(coordAdditionelles);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of() + positionPrinci;
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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

}
