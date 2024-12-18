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

public class DarkLord extends Foe {

    private float tempsInaction;

    private enum darkLordEtat {
        idle("idle", 1),
        spell("spell", 2);

        private String etat;
        private double speed;

        darkLordEtat(String etat, double speed) {
            this.etat = etat;
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private static final int ANIMATION_DURATION = 12;
    private darkLordEtat etat;
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

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, int MAX_LIFE) {
        super(area, orientation, position, vulnerabilite, MAX_LIFE);
        etat = darkLordEtat.idle;
        this.orientation = orientation;
        idleAnimation = new OrientedAnimation("icoop/darkLord", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        spellAnimation = new OrientedAnimation("icoop/darkLord.spell", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        currentAnimation = idleAnimation;
        handler = new darkLordInteractionHandler();
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        alternate = 0;
        tempsInaction = 0;
    }

    public void setEtat(darkLordEtat etat) {
        this.etat = etat;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (darkLordEtat.spell == etat || isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }
        behavior(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        currentAnimation.draw(canvas);
    }

    private void behavior(float deltaTime) {
        if (etat == darkLordEtat.spell) {
            targetedMoving();
            Fire flammes1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flammes2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flammes3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 30, "icoop/fireproj", "fire");
            Fire flammes4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 30, "icoop/fireproj", "fire");
            List<Fire> flammes = List.of(flammes1, flammes2, flammes3, flammes4);
            Fire eau1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 30, "icoop/waterproj", "water");
            Fire eau2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 30, "icoop/waterproj", "water");
            Fire eau3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 15, "icoop/waterproj", "water");
            Fire eau4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 15, "icoop/waterproj", "water");
            List<Fire> eau = List.of(eau1, eau2, eau3, eau4);
            randomFloat -= deltaTime;
            if (randomFloat <= 0) {
                randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
                if (alternate % 2 == 0) {
                    registerProjectiles(flammes);
                }

                if (alternate % 2 == 1) {
                    registerProjectiles(eau);
                }
                alternate++;
            }
        }else if (etat == darkLordEtat.idle){
            randomMoving();
        }
    }

    private void registerProjectiles(List<Fire> projectils) {
        for (Fire projectil : projectils) {
            if (getOwnerArea().canEnterAreaCells(projectil, projectil.getCurrentCells())) {
                getOwnerArea().registerActor(projectil);
            }
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
            move(ANIMATION_DURATION);
        }

    }

    private int getAdjustedSpeed() {
        return (int) (ANIMATION_DURATION / etat.getSpeed());
    }

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

    @Override
    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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

    private void setInactionBehavior() {
        tempsInaction = RandomGenerator.getInstance().nextInt(0, TIME_TRESHOLD);
        resetMotion();
    }


    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class darkLordInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                setEtat(darkLordEtat.spell);
                currentAnimation = spellAnimation;
                setPlayer(player);
            }
        }
    }
}



