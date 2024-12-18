package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Fleche extends Projectile {
    private Animation animation;
    private FlecheInteractionHandler handler;

    public Fleche(Area area, Orientation orientation, DiscreteCoordinates position, float vitesse, int distancemax) {
        super(area, orientation, position, vitesse, distancemax);
        if (orientation == Orientation.RIGHT) {
            animation = new Animation("icoop/arrowRight", 4, 1, 1, this, 32, 32, 4, true);
        }else if (orientation == Orientation.LEFT){
            animation = new Animation("icoop/arrowLeft", 4, 1, 1, this, 32, 32, 4, true);
        }
        handler = new FlecheInteractionHandler();
    }

    @Override
    public void update(float deltatime) {
        super.update(deltatime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    private class FlecheInteractionHandler implements ICoopInteractionVisitor {

        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.perteDegats(2, "physique");
                getOwnerArea().unregisterActor(Fleche.this);

            }
        }

        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosif.setRetardateur(0);
                getOwnerArea().unregisterActor(Fleche.this);

            }
        }

        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (isCellInteraction) {
                foe.perteDegats(1, "physique");
                getOwnerArea().unregisterActor(Fleche.this);
            }
        }
    }
}
