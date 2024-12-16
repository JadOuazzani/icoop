package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements Interactor, Unstoppable {
    private final int distanceMax;
    private int distanceParcourir;
    private final static int DEGATS_PERDUE = 1;
    private float vitesse;
    private final static int MOVE_DURATION = 15;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, float vitesse, int distanceMax) {
        super(area, orientation, position);
        this.vitesse = vitesse;
        this.distanceMax = distanceMax;
        distanceParcourir = distanceMax;


    }

    public void update(float deltatime) {
        super.update(deltatime);
        distanceParcourir -= 1;
        if (distanceParcourir == 0) {
            getOwnerArea().unregisterActor(this);
        }
        move((int) (MOVE_DURATION / vitesse));
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return (distanceParcourir != 0);
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }


}
