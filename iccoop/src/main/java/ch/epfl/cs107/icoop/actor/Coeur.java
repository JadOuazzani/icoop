package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utilities.ICoopCollectable;
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

public class Coeur extends ICoopCollectable implements Interactor {
    private static final int ANIMATION_DURATION = 24;
    private final Animation animation;
    private final CoeurInteractionHandler handler;

    public Coeur(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16,
                ANIMATION_DURATION / 4, true);
        handler = new CoeurInteractionHandler();
    }


    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public boolean isViewInteractable() {
        return false;
    }


    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    public boolean wantsCellInteraction() {
        return true;
    }

    public boolean wantsViewInteraction() {
        return false;
    }


    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    private class CoeurInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsCellInteraction()) {
                player.getHealth().increase(1);
                collect();
            }
        }
    }


    public void update(float deltaTime) {
        animation.update(deltaTime);
        if (isCollected()){
            disparition();
        }
    }

    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }


}




