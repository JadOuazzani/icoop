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
    private Fire flammes;
    private Fire eau;
    final Orientation[] orders = {UP, LEFT, DOWN, RIGHT};
    private float randomFloat;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, int MAX_LIFE) {
        super(area, orientation, position, vulnerabilite, MAX_LIFE);
        etat = darkLordEtat.idle;
        idleAnimation = new OrientedAnimation("icoop/darkLord", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        spellAnimation = new OrientedAnimation("icoop/darkLord.spell", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        currentAnimation = idleAnimation;
        handler = new darkLordInteractionHandler();
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
    }

    public void setEtat(darkLordEtat etat) {
        this.etat = etat;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (etat == darkLordEtat.spell || isDisplacementOccurs()) {
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
            flammes = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 10, "icoop/fireproj", "fire");
            eau = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 10, "icoop/waterproj", "water");
            randomFloat -= deltaTime;
            if (randomFloat <= 0) {
                randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 1f);

                if (getOwnerArea().canEnterAreaCells(flammes, flammes.getCurrentCells())) {
                    getOwnerArea().registerActor(flammes);

                }

                if (getOwnerArea().canEnterAreaCells(eau, eau.getCurrentCells())) {
                    getOwnerArea().registerActor(eau);
                }
            }

        }
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> cells = new ArrayList<>();
        int radius = 3;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                Vector newVector = new Vector(i, j);
                cells.add(getCurrentMainCellCoordinates().jump(newVector));
            }
        }
        return cells;
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



