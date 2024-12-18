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
        idle("idle", 2),
        attaque("attaque", 4),
        protection("protection", 1);


        private String etat;
        private float speedFactor;

        BombFoeEtat(String etat, float speedFactor) {
            this.etat = etat;
            this.speedFactor = speedFactor;
        }

        public float getSpeedFactor() {
            return speedFactor;
        }

    }


    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT};
    private OrientedAnimation orientedAnimation;
    private OrientedAnimation orientedAnimationProtection;
    private BombFoeEtat etat;
    private static final float NBR_CELL_FRONT = 7;
    private static final int ANIMATION_DURATION = 24;
    private final static int TIME_TRESHOLD = 50;
    private final BombFoeInteractionHandler handler;
    private float tempsInaction;
    private OrientedAnimation currentAnimation;


    private Orientation orientation;
    private ICoopPlayer player;
    private float elapsedTime = 0; // Temps écoulé depuis la dernière mise à jour

    private final float randomMoveCooldown;
    private float randomMoveTimer = 0f;
    private boolean dropped;
    private float protectionTime;


    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 2);
        this.etat = BombFoeEtat.idle;
        orientedAnimation = new OrientedAnimation("bombFoe", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        orientedAnimationProtection = new OrientedAnimation("bombFoe.protecting", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, false);
        handler = new BombFoeInteractionHandler();
        tempsInaction = 0;
        this.orientation = orientation;
        randomMoveCooldown = RandomGenerator.getInstance().nextFloat(0.2f, 0.6f);
        dropped = false;
        currentAnimation = orientedAnimation;

    }

    @Override
    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (etat == BombFoeEtat.protection || isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }

        behavior();

        if (health.isOff()) {
            deathAnimation.update(deltaTime);
        } else {
            orientedAnimation.update(deltaTime);
        }

        if (health.isOff() && deathAnimation.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }
    }


    public void setEtat(BombFoeEtat etat) {
        this.etat = etat;
    }

    public void setCurrentAnimation(OrientedAnimation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public void setPlayer(ICoopPlayer player) {
        this.player = player;
    }

    private void behavior() {
        if (tempsInaction > 0) {
            tempsInaction--;
            return; // Ignore tout autre comportement jusqu'à ce que le temps d'innation soit nul
        }
        if (!getImmunite() && tempsInaction <= 0) {
            switch (etat) {
                case idle:
                    setCurrentAnimation(orientedAnimation);
                    randomMoving();
                    break;
                case attaque:
                    targetedMoving();
                    if (dropBombe()) {
                        setCurrentAnimation(orientedAnimationProtection);
                        setEtat(BombFoeEtat.protection);
                        protectionTime = RandomGenerator.getInstance().nextInt(72, 120);
                    }
                    break;
                case protection:
                    if (protectionTime > 0) {
                        System.out.println(protectionTime);
                        protectionTime -= 1.5F;
                    } else {
                        System.out.println("done");
                        resetPlayerCoords();
                        setEtat(BombFoeEtat.idle);
                        dropped = false;
                    }
                    break;


            }
        } else if (getImmunite()) {
            setEtat(BombFoeEtat.idle);
            setTempsInaction(0);
        }

    }

    private int getAdjustedSpeed() {
        return (int) (ANIMATION_DURATION / etat.getSpeedFactor());
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    private void randomMoving() {
        if (!isDisplacementOccurs()) {
            double randDouble = RandomGenerator.getInstance().nextDouble();
            if (randDouble < 0.4) {
                orientation = Orientation.fromInt(RandomGenerator.getInstance().nextInt(0, 4));
                orientate(orientation);
                setInactionBehavior();
            }
            move(getAdjustedSpeed());
        }
    }


    private void targetedMoving() {
        Vector targetPosition = player.getCurrentMainCellCoordinates().toVector();
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

        if (orientate(newOrientation)) {
            move(getAdjustedSpeed());
        } else {
            move(ANIMATION_DURATION / 4);
        }

    }


    private boolean dropBombe() {
        if (distanceBetween(getCurrentMainCellCoordinates(), player.getCurrentMainCellCoordinates()) < 3) {
            resetMotion();
            Explosif bombe = new Explosif(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
            if (getOwnerArea().canEnterAreaCells(bombe, bombe.getCurrentCells()) && !dropped) {
                System.out.println("22222");

                getOwnerArea().registerActor(bombe);
                bombe.setActiver(true);
                dropped = true;
                return dropped;
            }
        }
        return false;
    }

    public void resetPlayerCoords() {
        this.player = null;
    }

    private void setInactionBehavior() {
        tempsInaction = RandomGenerator.getInstance().nextInt(0, TIME_TRESHOLD);
        resetMotion();
    }

    public void setTempsInaction(float tempsInaction) {
        this.tempsInaction = tempsInaction;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (health.isOff()) {
            deathAnimation.draw(canvas);
        } else {
            currentAnimation.draw(canvas);
        }
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

    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                setEtat(BombFoeEtat.attaque);
                setPlayer(player);
            }
        }
    }

}