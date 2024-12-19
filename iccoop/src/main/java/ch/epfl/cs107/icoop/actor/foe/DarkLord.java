package ch.epfl.cs107.icoop.actor.foe;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

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

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

/**
 * The DarkLord class represents a powerful enemy in the game.
 * It alternates between idle movement and casting spells at the player.
 */
public class DarkLord extends Foe {

    /**
     * Enum representing the states of the DarkLord.
     */
    private enum darkLordState {
        idle("idle", 1),
        spell("spell", 2);

        private String state;
        private double speed;

        darkLordState(String state, double speed) {
            this.state = state;
            this.speed = speed;
        }

        /**
         * Retrieves the movement speed factor for the state.
         *
         * @return (double): The speed factor.
         */
        public double getSpeed() {
            return speed;
        }
    }

    private static final int ANIMATION_DURATION = 12;
    private darkLordState state;
    private OrientedAnimation currentAnimation;
    private OrientedAnimation idleAnimation;
    private OrientedAnimation spellAnimation;
    private darkLordInteractionHandler handler;
    final Orientation[] orders = {UP, LEFT, DOWN, RIGHT};
    private float randomFloat;
    private int alternate;
    private ICoopPlayer player;
    private final static int TIME_TRESHOLD = 50;
    private Orientation orientation;
    private float inactionTime;

    /**
     * Constructor for the DarkLord class.
     *
     * @param area (Area): The area where the DarkLord exists.
     * @param orientation (Orientation): The initial orientation of the DarkLord.
     * @param position (DiscreteCoordinates): The initial position of the DarkLord.
     * @param vulnerabilite (List<String>): List of vulnerabilities of the DarkLord.
     * @param MAX_LIFE (int): Maximum health points of the DarkLord.
     */
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, int MAX_LIFE) {
        super(area, orientation, position, vulnerabilite, MAX_LIFE);
        state = darkLordState.idle;
        this.orientation = orientation;
        idleAnimation = new OrientedAnimation("icoop/darkLord", ANIMATION_DURATION / 3, this, anchor, orders, 3, 2, 2, 32, 32, true);
        spellAnimation = new OrientedAnimation("icoop/darkLord.spell", ANIMATION_DURATION / 3, this, anchor, orders, 3, 2, 2, 32, 32, true);
        currentAnimation = idleAnimation;
        handler = new darkLordInteractionHandler();
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        alternate = 0;
        inactionTime = 0;
    }

    public void setState(darkLordState state) {
        this.state = state;
    }

    /**
     * Updates the DarkLord's state and behavior.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (darkLordState.spell == state || isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }
        behavior(deltaTime);
    }

    /**
     * Draws the DarkLord on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        currentAnimation.draw(canvas);
    }

    /**
     * Defines the DarkLord's behavior based on its state:
     * Casts projectiles depending on the DarkLord's current spell.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */

    private void behavior(float deltaTime) {
        if (state == darkLordState.spell) {
            targetedMoving();
            Fire flame1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flame2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flame3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flame4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 30, "icoop/fireproj", "fire");
            List<Fire> flames = List.of(flame1, flame2, flame3, flame4);
            Fire drop1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 30, "icoop/waterproj", "water");
            Fire drop2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 30, "icoop/waterproj", "water");
            Fire drop3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 15, "icoop/waterproj", "water");
            Fire drop4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 15, "icoop/waterproj", "water");
            List<Fire> drop = List.of(drop1, drop2, drop3, drop4);
            randomFloat -= deltaTime;
            if (randomFloat <= 0) {
                randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
                if (alternate % 2 == 0) {
                    registerProjectiles(flames);
                }

                if (alternate % 2 == 1) {
                    registerProjectiles(drop);
                }
                alternate++;
            }
        }else if (state == darkLordState.idle){
            randomMoving();
        }
    }

    /**
     * Registers a list of projectiles in the game area.
     *
     * @param projectiles (List<Fire>): The list of projectiles to register.
     */
    private void registerProjectiles(List<Fire> projectiles) {
        for (Fire projectile : projectiles) {
            if (getOwnerArea().canEnterAreaCells(projectile, projectile.getCurrentCells())) {
                getOwnerArea().registerActor(projectile);
            }
        }
    }

    /**
     * Handles targeted movement towards the player.
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
            move(ANIMATION_DURATION);
        }

    }

    /**
     * Retrieves the adjusted speed based on the DarkLord's current state.
     *
     * @return (int): The adjusted speed value.
     */
    private int getAdjustedSpeed() {
        return (int) (ANIMATION_DURATION / state.getSpeed());
    }

    /**
     * Sets the player target for the DarkLord.
     *
     * @param player (ICoopPlayer): The targeted player.
     */
    public void setPlayer(ICoopPlayer player) {
        this.player = player;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> cells = new ArrayList<>();
        int radius = 5;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                Vector newVector = new Vector(i, j);
                cells.add(getCurrentMainCellCoordinates().jump(newVector));
            }
        }
        return cells;
    }

    /**
     * Retrieves the main cell coordinates of the DarkLord.
     *
     * @return (DiscreteCoordinates): The main cell coordinates currently occupied by the DarkLord.
     */
    @Override
    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    /**
     * Retrieves the list of cells currently occupied by the DarkLord.
     *
     * @return (List<DiscreteCoordinates>): A singleton list containing the current main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Handles random movement for the DarkLord when in the IDLE state.
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
     * Sets the inaction behavior for the DarkLord.
     */
    private void setInactionBehavior() {
        inactionTime = RandomGenerator.getInstance().nextInt(0, TIME_TRESHOLD);
        resetMotion();
    }


    /**
     * Interacts with another interactable object.
     *
     * @param other (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    /**
     * Inner class handling DarkLord interactions with the player.
     */
    private class darkLordInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Handles interaction with a player:
         * if presence of a player, darkLord's state switch to Spell
         *
         * @param player (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                setState(darkLordState.spell);
                currentAnimation = spellAnimation;
                setPlayer(player);
            }
        }
    }
}



