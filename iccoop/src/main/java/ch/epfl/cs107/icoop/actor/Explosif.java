package ch.epfl.cs107.icoop.actor;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Explosif extends AreaEntity implements Interactor {
    private float retardateur;
    private boolean activer;
    private boolean etatExplosion;
    private Animation animation;
    private static final int ANIMATION_DURATION = 24;
    private final ExplosifInteractionHandler handler;

    public Explosif(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        retardateur = 2f;
        activer = false;
        etatExplosion = false;
        animation = new Animation("icoop/explosive", 2, 1, 1, this, 16, 16, ANIMATION_DURATION / 2, true);
        handler = new ExplosifInteractionHandler();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (activer) {
            retardateur -= deltaTime;
            animation.update(deltaTime);
        }
        if (retardateur <= 0 && !etatExplosion) {
            etatExplosion = true;
            animation = new Animation("icoop/explosion", 7, 1, 1, this, 32, 32, ANIMATION_DURATION / 7, false);
            animation.update(deltaTime);
        }
        if (animation.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    public void setActiver(boolean activer) {
        this.activer = activer;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return this.getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return etatExplosion;
    }

    @Override
    public boolean wantsViewInteraction() {
        return etatExplosion;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return (!activer && !etatExplosion);
    }

    @Override
    public boolean isViewInteractable() {
        return !etatExplosion;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    private class ExplosifInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Rochers rochers, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                rochers.setDetruit(true);
            }
        }
    }
}

