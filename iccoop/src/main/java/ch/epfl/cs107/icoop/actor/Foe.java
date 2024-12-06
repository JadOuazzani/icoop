package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

public abstract class Foe extends MovableAreaEntity implements Interactor {
    private Health health;
    private int MAX_LIFE;
    private OrientedAnimation animationorientee;
    private boolean isdead;
    private boolean invulnerable;
    private boolean immunite;
    private int periodeImmunite = 24;
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private static final int ANIMATION_DURATION = 4;
    private Animation animation;



    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */

    Foe(Area area, Orientation orientation, DiscreteCoordinates position, List<ElementalEntity> vulnerabilite,String prefixe ) {
        super(area, orientation, position);
        animationorientee = new OrientedAnimation(prefixe, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32,
                true);
        animation = new Animation("icoop/vanish", 7, 2, 2, this , 32, 32, new
                Vector(-0.5f, 0f), ANIMATION_DURATION/7, false);
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
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {}

    @Override
    public boolean takeCellSpace() {
        if(isdead){return false;}
        else {return true;}
    }

    @Override
    public void draw(Canvas canvas) {
        if (!immunite) {
            animationorientee.draw(canvas);
            health.draw(canvas);
        }
        if (immunite && periodeImmunite % 7 == 0) {
            animationorientee.draw(canvas);
            health.draw(canvas);
        }
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }

    public class FoeInteractionHandler implements ICoopInteractionVisitor {


        @Override
        public void interactWith(Explosif explosif, boolean isCellInteraction) {

            if (wantsCellInteraction() && isCellInteraction) {
                explosif.disparition();
            }
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            ICoopInteractionVisitor.super.interactWith(player, isCellInteraction);
        }
    }



    public void setDead () {
        if (health.getHealthPoints()<=0){
        getOwnerArea().unregisterActor(this);
        isdead=true;}
    }
}
