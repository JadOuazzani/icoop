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

    private enum darkLordEtat {
        idle("idle"),
        spell("spell");

        private String etat;

        darkLordEtat(String etat) {
            this.etat = etat;
        }
    }

    private darkLordEtat etat;
    private OrientedAnimation currentAnimation;
    private OrientedAnimation idleAnimation;
    private OrientedAnimation spellAnimation;
    private darkLordInteractionHandler handler;
    final Orientation[] orders = {UP, LEFT, DOWN, RIGHT};
    private float randomFloat;
    private int alternate;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, int MAX_LIFE) {
        super(area, orientation, position, vulnerabilite, MAX_LIFE);
        etat = darkLordEtat.idle;
        idleAnimation = new OrientedAnimation("icoop/darkLord", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        spellAnimation = new OrientedAnimation("icoop/darkLord.spell", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        currentAnimation = idleAnimation;
        handler = new darkLordInteractionHandler();
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        alternate = 0;
    }

    public void setEtat(darkLordEtat etat) {
        this.etat = etat;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        if (etat == darkLordEtat.spell || isDisplacementOccurs()) {
//            currentAnimation.update(deltaTime);
//        } else {
//            currentAnimation.reset();
//        }
        behavior(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        currentAnimation.draw(canvas);
    }

    private void behavior(float deltaTime) {
        if (etat == darkLordEtat.spell) {
            Fire flammes1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 15, "icoop/fireproj", "fire");
            Fire flammes2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 20, "icoop/fireproj", "fire");
            Fire flammes3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 15, "icoop/fireproj", "fire");
            Fire flammes4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 15, "icoop/fireproj", "fire");
            Fire eau1 = new Fire(getOwnerArea(), UP, getCurrentMainCellCoordinates().jump(UP.toVector()), 3, 15, "icoop/waterproj", "water");
            Fire eau2 = new Fire(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(DOWN.toVector()), 3, 20, "icoop/waterproj", "water");
            Fire eau3 = new Fire(getOwnerArea(), LEFT, getCurrentMainCellCoordinates().jump(LEFT.toVector()), 3, 15, "icoop/waterproj", "water");
            Fire eau4 = new Fire(getOwnerArea(), RIGHT, getCurrentMainCellCoordinates().jump(RIGHT.toVector()), 3, 15, "icoop/waterproj", "water");
            randomFloat -= deltaTime;
            if (randomFloat <= 0) {
                randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
                if (alternate % 2 == 0) {
                    if (getOwnerArea().canEnterAreaCells(flammes1, flammes1.getCurrentCells())) {
                        getOwnerArea().registerActor(flammes1);
                    }
                    if (getOwnerArea().canEnterAreaCells(flammes2, flammes2.getCurrentCells())) {
                        getOwnerArea().registerActor(flammes2);
                    }
                    if (getOwnerArea().canEnterAreaCells(flammes3, flammes3.getCurrentCells())) {
                        getOwnerArea().registerActor(flammes3);
                    }
                    if (getOwnerArea().canEnterAreaCells(flammes4, flammes4.getCurrentCells())) {
                        getOwnerArea().registerActor(flammes4);
                    }
                }

                if (alternate % 2 == 1) {
                    if (getOwnerArea().canEnterAreaCells(eau1, eau1.getCurrentCells())) {
                        getOwnerArea().registerActor(eau1);
                    }
                    if (getOwnerArea().canEnterAreaCells(eau2, eau2.getCurrentCells())) {
                        getOwnerArea().registerActor(eau2);
                    }
                    if (getOwnerArea().canEnterAreaCells(eau3, eau3.getCurrentCells())) {
                        getOwnerArea().registerActor(eau3);
                    }
                    if (getOwnerArea().canEnterAreaCells(eau4, eau4.getCurrentCells())) {
                        getOwnerArea().registerActor(eau4);
                    }
                }
                alternate++;
            }
        }
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
            }
        }
    }
}



