package ch.epfl.cs107.icoop.actor.foe;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.object.Explosive;
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

import static ch.epfl.cs107.play.math.DiscreteCoordinates.distanceBetween;
import static ch.epfl.cs107.play.math.Orientation.DOWN;

/**
 * The BombFoe class represents a specialized type of enemy that drops explosives
 * and has different states, including idle, attack, and protection.
 */
public class BombFoe extends Foe {

    /**
     * Enum representing the different states of BombFoe.
     */
    private enum BombFoeState {
        IDLE("idle", 2),
        ATTACK("attaque", 4),
        PROTECTION("protection", 1);

        private String state;
        private float speedFactor;

        BombFoeState(String state, float speedFactor) {
            this.state = state;
            this.speedFactor = speedFactor;
        }

        /**
         * Retrieves the speed factor for the state.
         *
         * @return (float): The speed factor.
         */

        public float getSpeedFactor() {
            return speedFactor;
        }

    }

    private static final float NBR_CELL_FRONT = 7;
    private static final int ANIMATION_DURATION = 24;
    private final static int TIME_TRESHOLD = 50;

    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT};
    private OrientedAnimation orientedAnimation;
    private OrientedAnimation orientedAnimationProtection;
    private BombFoeState state;
    private final BombFoeInteractionHandler handler;
    private float inactionTime;
    private OrientedAnimation currentAnimation;

    private Orientation orientation;
    private ICoopPlayer player;
    private float elapsedTime = 0; // Temps écoulé depuis la dernière mise à jour

    private final float randomMoveCooldown;
    private float randomMoveTimer = 0f;
    private boolean dropped;
    private float protectionTime;


    /**
     * Constructor for BombFoe.
     *
     * @param area          (Area): The area where the BombFoe exists.
     * @param orientation   (Orientation): The initial orientation of the BombFoe.
     * @param position      (DiscreteCoordinates): The initial position of the BombFoe.
     * @param vulnerabilite (List<String>): List of vulnerabilities of the BombFoe.
     */

    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 2);
        this.state = BombFoeState.IDLE;
        orientedAnimation = new OrientedAnimation("bombFoe", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        orientedAnimationProtection = new OrientedAnimation("bombFoe.protecting", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, false);
        handler = new BombFoeInteractionHandler();
        inactionTime = 0;
        this.orientation = orientation;
        randomMoveCooldown = RandomGenerator.getInstance().nextFloat(0.2f, 0.6f);
        dropped = false;
        currentAnimation = orientedAnimation;

    }

    @Override
    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    /**
     * Updates the BombFoe's state and behavior.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (state == BombFoeState.PROTECTION || isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }
        behavior();
    }

    /**
     * Sets the BombFoe's state.
     *
     * @param state (BombFoeEtat): The new state for the BombFoe.
     */
    private void setState(BombFoeState state) {
        this.state = state;
    }

    /**
     * Sets the current animation for the BombFoe.
     *
     * @param currentAnimation (OrientedAnimation): The new animation to set.
     */
    public void setCurrentAnimation(OrientedAnimation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    /**
     * Assigns the targeted player for the BombFoe.
     *
     * @param player (ICoopPlayer): The targeted player.
     */
    public void setPlayer(ICoopPlayer player) {
        this.player = player;
    }

    /**
     * Handles the BombFoe's behavior based on its current state.
     */
    private void behavior() {
        if (inactionTime > 0) {
            inactionTime--;
            return; // Ignore other behavior until inactionTime is zero
        }
        if (!getImunity() && inactionTime <= 0) {
            switch (state) {
                case IDLE:
                    setCurrentAnimation(orientedAnimation);
                    randomMoving();
                    break;

                case ATTACK:
                    targetedMoving();
                    if (dropBombe()) {
                        setCurrentAnimation(orientedAnimationProtection);
                        setState(BombFoeState.PROTECTION);
                        protectionTime = RandomGenerator.getInstance().nextInt(72, 120);
                    }
                    break;

                case PROTECTION:
                    if (protectionTime > 0) {
                        System.out.println(protectionTime);
                        protectionTime -= 1.5F;
                    } else {
                        System.out.println("done");
                        resetPlayerCoords();
                        setState(BombFoeState.IDLE);
                        dropped = false;
                    }
                    break;
            }
        } else if (getImunity()) {
            setState(BombFoeState.IDLE);
            setInactionTime(0);
        }
    }

    /**
     * Retrieves the adjusted speed based on the BombFoe's current state.
     *
     * @return (int): The adjusted speed value.
     */

    private int getAdjustedSpeed() {
        return (int) (ANIMATION_DURATION / state.getSpeedFactor());
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Handles random movement for the BombFoe when in the IDLE state.
     */
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

    /**
     * Handles targeted movement for the BombFoe when in the ATTACK state.
     */
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


    /**
     * Drops a bomb if the player is within a certain distance.
     *
     * @return (boolean): True if the bomb was successfully dropped, false otherwise.
     */
    private boolean dropBombe() {
        if (distanceBetween(getCurrentMainCellCoordinates(), player.getCurrentMainCellCoordinates()) < 3) {
            resetMotion();
            Explosive bombe = new Explosive(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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


    /**
     * Resets the player coordinates to null, clearing the target.
     */
    public void resetPlayerCoords() {
        this.player = null;
    }

    /**
     * Sets the inaction behavior for the BombFoe.
     */
    private void setInactionBehavior() {
        inactionTime = RandomGenerator.getInstance().nextInt(0, TIME_TRESHOLD);
        resetMotion();
    }

    /**
     * Sets the inaction time for the BombFoe.
     *
     * @param inactionTime (float): The duration of inaction.
     */
    public void setInactionTime(float inactionTime) {
        this.inactionTime = inactionTime;
    }

    /**
     * Draws the BombFoe on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw the BombFoe on.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (health.isOff()) {
            deathAnimation.draw(canvas);
        } else {
            currentAnimation.draw(canvas);
        }
    }

    /**
     * Determines if the BombFoe wants cell interactions.
     *
     * @return (boolean): False as BombFoe does not require cell interactions.
     */
    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    /**
     * Determines if the BombFoe wants view interactions based on its state.
     *
     * @return (boolean): True for IDLE and ATTACK states, false for PROTECTION state.
     */
    @Override
    public boolean wantsViewInteraction() {
        return switch (state) {
            case BombFoeState.IDLE, BombFoeState.ATTACK -> true;
            case BombFoeState.PROTECTION -> false;
        };
    }

    /**
     * Interacts with another interactable object.
     *
     * @param other             (Interactable): The interactable object to interact with.
     * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    /**
     * Retrieves the cells within the BombFoe's field of view.
     *
     * @return (List < DiscreteCoordinates >): The list of cells within the BombFoe's field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return switch (state) {
            case BombFoeState.ATTACK ->
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
            case BombFoeState.PROTECTION, BombFoeState.IDLE -> {
                List<DiscreteCoordinates> cells = new ArrayList<>();
                for (int i = 0; i <= NBR_CELL_FRONT; i++) {
                    cells.add(getCurrentMainCellCoordinates().jump((getOrientation().toVector().mul(i))));
                }
                yield cells;
            }
        };
    }

    /**
     * Inner class handling BombFoe interactions with players and other objects.
     */
    private class BombFoeInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with a player:
         * BombFoe's state switch to ATTACK if there is a player
         *
         * @param player            (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                setState(BombFoeState.ATTACK);
                setPlayer(player);
            }
        }
    }

}