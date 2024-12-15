package ch.epfl.cs107.icoop.actor.foe;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ch.epfl.cs107.play.math.DiscreteCoordinates.distanceBetween;
import static ch.epfl.cs107.play.math.Orientation.DOWN;

public class BombFoe extends Foe {

    private enum BombFoeEtat {
        idle("icoop/bombMonster", "idle", 2),
        attaque("icoop/bombMonster", "attaque", 6),
        protection("icoop/bombMonster.protecting", "protection", 1);

        private String spriteName;
        private String etat;
        private int speedFactor;

        BombFoeEtat(String spriteName, String etat, int speedFactor) {
            this.spriteName = spriteName;
            this.etat = etat;
            this.speedFactor = speedFactor;
        }

        public int getSpeedFactor() {
            return speedFactor;
        }

        public String getSpriteName() {
            return spriteName;
        }
    }


    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT};
    private OrientedAnimation orientedAnimation;
    private BombFoeEtat etat;
    private static final float NBR_CELL_FRONT = 8;
    private final BombFoeInteractionHandler handler;
    private float tempsInaction;
    private final static int TIME_TRESHOLD = 24;
    private int randomInt;
    private double randDouble;
    private static final int ANIMATION_DURATION = 12;
    private int speedFactor;
    private Orientation orientation;
    private boolean randomBool;
    private DiscreteCoordinates playerCoords;
    private int randomInt2;


    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, String spriteName) {
        super(area, orientation, position, vulnerabilite, 2);
        this.etat = BombFoeEtat.idle;
        this.orientedAnimation = new OrientedAnimation(etat.getSpriteName(), ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        handler = new BombFoeInteractionHandler();
        speedFactor = etat.getSpeedFactor();

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        switch (etat) {
            case BombFoeEtat.idle:
                this.orientedAnimation = new OrientedAnimation(etat.getSpriteName(), ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
                break;
            case BombFoeEtat.attaque, BombFoeEtat.protection:
                this.orientedAnimation = new OrientedAnimation(etat.getSpriteName(), ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, false);
                break;
        }
        orientedAnimation.update(deltaTime);
        speedFactor = etat.getSpeedFactor();
        randDouble = RandomGenerator.getInstance().nextDouble();
        randomInt = RandomGenerator.getInstance().nextInt(TIME_TRESHOLD);
        orientation = Orientation.fromInt(RandomGenerator.getInstance().nextInt());
        randomBool = RandomGenerator.getInstance().nextBoolean();
        randomInt2 = RandomGenerator.getInstance().nextInt(72, 120);
        behavior(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        orientedAnimation.draw(canvas);
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return switch (etat) {
            case BombFoeEtat.idle, BombFoeEtat.attaque -> true;
            case BombFoeEtat.protection -> false;
        };
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }


    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return switch (etat) {
            case BombFoeEtat.attaque ->
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
            case BombFoeEtat.protection, BombFoeEtat.idle -> {
                List<DiscreteCoordinates> cells = new ArrayList<>();
                for (int i = 0; i <= NBR_CELL_FRONT; i++) {
                    cells.add(getCurrentMainCellCoordinates().jump((getOrientation().toVector().mul(i))));
                }
                yield cells;
            }
        };
    }

    public void setEtat(BombFoeEtat etat) {
        this.etat = etat;
    }

    public void setPlayerCoords(DiscreteCoordinates playerCoords) {
        this.playerCoords = playerCoords;
    }

    private void behavior(float deltatime) {
        if (!getImmunite() && tempsInaction == 0) {
            switch (etat) {
                case BombFoeEtat.idle:
                    randomMoving();
                    if (randomBool) {
                        setInactionBehavior(deltatime);
                    }
                    break;
                case BombFoeEtat.attaque:
                    targetedMoving();
                    if (distanceBetween(getCurrentMainCellCoordinates(), playerCoords) <= 3) {
                        Explosif bombe = new Explosif(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
                        if (getOwnerArea().canEnterAreaCells(bombe, bombe.getCurrentCells())) {
                            getOwnerArea().registerActor(bombe);
                            setEtat(BombFoeEtat.protection);
                        }
                    }
                    break;
                case protection:
                    if (randomInt2 > 0) {
                        randomMoving();
                        resetPlayerCoords();
                        randomInt2 -= deltatime;
                    }
                    setEtat(BombFoeEtat.idle);
                    break;
            }
        } else if (getImmunite()) {
            setEtat(BombFoeEtat.idle);
            setTempsInaction(0);
        }
    }

    private void randomMoving() {
        if (!isDisplacementOccurs()) {
            if (randDouble < 0.4) {
                orientate(orientation);
            }
            move(ANIMATION_DURATION / speedFactor);
        }
    }

    private void targetedMoving() {
        Vector targetPosition = playerCoords.toVector();
        Vector currentPosition = getCurrentMainCellCoordinates().toVector();
        Vector v = targetPosition.sub(currentPosition);
        float deltaX = v.getX();
        float deltaY = v.getY();

        Vector chosenDirection;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            chosenDirection = new Vector(deltaX, 0);
        } else {
            chosenDirection = new Vector(0, deltaY);
        }

        Orientation newOrientation = Orientation.fromVector(chosenDirection);
        orientate(newOrientation);
        move(ANIMATION_DURATION / speedFactor);

        if (!orientate(newOrientation)) {
            move(ANIMATION_DURATION / speedFactor);
        }

    }

    public void resetPlayerCoords() {
        this.playerCoords = null;
    }

    private void setInactionBehavior(float deltaTime) {
        if (tempsInaction > 0) {
            move(0);
            tempsInaction -= deltaTime;
        }
    }

    public void setTempsInaction(float tempsInaction) {
        this.tempsInaction = tempsInaction;
    }

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                setEtat(BombFoeEtat.attaque);
                setPlayerCoords(player.getCurrentMainCellCoordinates());
            }
        }
    }

}