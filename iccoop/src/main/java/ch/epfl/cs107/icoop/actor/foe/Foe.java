package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.ElementalEntity;
import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.perso.Health;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.sql.SQLOutput;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

public abstract class
Foe extends MovableAreaEntity implements Interactor {
    protected Health health;
    protected int MAX_LIFE;
    private boolean isDead;
    private boolean immunite;
    private static int periodeImmunite = 24;
    final Vector anchor = new Vector(-0.5f, -0.5f);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    protected Animation deathAnimation;
    List<String> vulnerabilite;
    protected static final int ANIMATION_DURATION = 24;
    private FoeInteractionHandler handler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */

    Foe(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, final int MAX_LIFE) {
        super(area, orientation, position);
        this.MAX_LIFE = MAX_LIFE;
        this.vulnerabilite = vulnerabilite;
        deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), ANIMATION_DURATION / 9, false);

        health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, false);
        isDead = false;
        immunite = false;
        handler = new FoeInteractionHandler();
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        health.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (health.isOff()) {
            deathAnimation.update(deltaTime);
            if (deathAnimation.isCompleted()) {
                getOwnerArea().unregisterActor(this);
            }
            isDead = true;
        }
        if (immunite) {
            periodeImmunite -= (int) (deltaTime / 2);
        }

        if (periodeImmunite <= 0) {
            immunite = false;
            periodeImmunite = 24;
        }

    }




    public boolean getImmunite() {
        return immunite;
    }


    @Override
    public boolean takeCellSpace() {
        return !isDead;
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
    public void interactWith(Interactable other, boolean isCellInteraction) {
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

    public void perteDegats(int degats, String type) {
        for (int i = 0; i < vulnerabilite.size(); i++) {
            if (vulnerabilite.get(i).equals(type)) {
                health.decrease(degats);
            }
        }
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

}
