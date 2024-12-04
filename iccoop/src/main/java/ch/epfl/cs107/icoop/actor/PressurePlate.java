package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class PressurePlate extends AreaEntity implements Logic {
    private Sprite sprite;
    private boolean interactorHere;
    private MovableAreaEntity occupier;


    public PressurePlate(Area area, DiscreteCoordinates positionPrincipale, Orientation orientation) {
        super(area, orientation, positionPrincipale);
        sprite = new Sprite("GroundPlateOff", 1f, 1f, this);
        interactorHere = false;
    }

    public void setOccupier(MovableAreaEntity occupier) {
        this.occupier = occupier;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (occupier != null && !(getCurrentMainCellCoordinates().equals(occupier.getCurrentMainCellCoordinates()))) {
            occupier = null;
        }
        if (occupier == null){
            setInteractorHere(false);
        }
    }

    public void setInteractorHere(boolean interactorHere) {
        this.interactorHere = interactorHere;
    }

    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
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
    public boolean isOn() {
        return interactorHere;
    }

    @Override
    public boolean isOff() {
        return !interactorHere;
    }

}