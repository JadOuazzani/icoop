package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.object.Orb;
import ch.epfl.cs107.icoop.actor.object.Rochers;
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

public class WaterBall extends Projectile{
    private Animation animation;
    private WaterBall.WaterBallInteractionHandler handler;
    private final static int ANIMATION_DURATION =12;

    public WaterBall(Area area, Orientation orientation, DiscreteCoordinates position, float vitesse, int distancemax) {
        super(area, orientation, position, vitesse, distancemax);
        animation = new Animation("icoop/magicWaterProjectile",4, 1, 1, this , 32, 32,
                ANIMATION_DURATION/4, true);

        handler = new WaterBallInteractionHandler();
    }

    @Override
    public void update(float deltatime) {
        super.update(deltatime);
        animation.update(deltatime);
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


    private class WaterBallInteractionHandler implements ICoopInteractionVisitor {


        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosif.setRetardateur(0);
                getOwnerArea().unregisterActor(WaterBall.this);

            }
        }

        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (isCellInteraction) {
                foe.perteDegats(1, "water");
                getOwnerArea().unregisterActor(WaterBall.this);
            }
        }

        public void interactWith(Rochers rochers, boolean isCellInteraction) {
            if (isCellInteraction) {
                rochers.setDetruit(true);
                getOwnerArea().unregisterActor(WaterBall.this);

            }
        }



    }
}


