package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class logMonster extends Foe {
    private Orientation[] orders = new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT};
    private OrientedAnimation orientedAnimation;
    private float randomFloat;
    private Fleche fleches;
    private static final int ANIMATION_DURATION = 24;
    private logMonsterInteractionHandler handler;
    private OrientedAnimation animation;


    public logMonster(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 5);
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        handler = new logMonsterInteractionHandler();
        animation = new OrientedAnimation("icoop/logMonster", ANIMATION_DURATION / 3, this, new Vector(-0.5f, -0.5f), orders, 9, 2, 2, 32, 32, true);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fleches = new Fleche(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 40);
        randomFloat -= deltaTime;
        if (randomFloat <= 0) {
            randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
            if (getOwnerArea().canEnterAreaCells(fleches, fleches.getCurrentCells())) {
                getOwnerArea().registerActor(fleches);
            }
        }

        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            animation.reset();
        }
        if (health.isOff()) {
            deathAnimation.update(deltaTime);
        } else {
            animation.update(deltaTime);
        }

        if (health.isOff() && deathAnimation.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }


    }


    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        super.acceptInteraction(v, isCellInteraction);
    }


    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class logMonsterInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.perteDegats(1, "physique");
            }
        }
    }
}
