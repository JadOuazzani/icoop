package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

/**
 * The logMonster class represents a foe that shoots arrows periodically.
 */
public class logMonster extends Foe {
    private Orientation[] orders = new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT};
    private OrientedAnimation orientedAnimation;
    private float randomFloat;
    private Arrow fleches;
    private static final int ANIMATION_DURATION = 24;
    private logMonsterInteractionHandler handler;
    private OrientedAnimation animation;


    /**
     * Constructor for the logMonster class.
     *
     * @param area          (Area): The area where the logMonster exists.
     * @param orientation   (Orientation): Initial orientation of the logMonster.
     * @param position      (DiscreteCoordinates): Initial position of the logMonster.
     * @param vulnerabilite (List<String>): List of vulnerabilities for the logMonster.
     */
    public logMonster(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 5);
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        handler = new logMonsterInteractionHandler();
        animation = new OrientedAnimation("icoop/logMonster", ANIMATION_DURATION / 3, this, new Vector(-0.5f, -0.5f), orders, 9, 2, 2, 32, 32, true);
    }

    /**
     * Updates the logMonster's state and fires arrows periodically.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fleches = new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 40);
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


    /**
     * Draws the logMonster and its animation.
     *
     * @param canvas (Canvas): The canvas to draw the logMonster on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }


    /**
     * Determines whether the logMonster takes up cell space.
     *
     * @return (boolean): Always false, as logMonster does not block cell space.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Accepts an interaction visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        super.acceptInteraction(v, isCellInteraction);
    }

    /**
     * Interacts with another interactable object.
     *
     * @param other             (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Inner class handling interactions for the logMonster.
     */
    private static class logMonsterInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with a player, dealing physical damage.
         *
         * @param player            (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.looseHp(1, "physical");
            }
        }
    }
}
