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
 * The HellSkull class represents a flame skull enemy that shoots fire projectiles.
 */
public class HellSkull extends Foe {
    private Orientation[] orders = new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT};
    private OrientedAnimation orientedAnimation;
    private float randomFloat;
    private Fire flames;
    private static final int ANIMATION_DURATION = 12;
    private HellSkullInteractionHandler handler;

    /**
     * Constructor for the HellSkull class.
     *
     * @param area          (Area): The area where the HellSkull exists.
     * @param orientation   (Orientation): Initial orientation of the HellSkull.
     * @param position      (DiscreteCoordinates): Initial position of the HellSkull.
     * @param vulnerabilite (List<String>): List of vulnerabilities for the HellSkull.
     */
    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 1);
        orientedAnimation = new OrientedAnimation("icoop/flameskull", ANIMATION_DURATION / 3, this, new Vector(-0.5f, -0.5f), orders, 3, 2, 2, 32, 32, true);
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        handler = new HellSkullInteractionHandler();
    }

    /**
     * Updates the HellSkull's state and fires projectiles periodically.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        flames = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 40, "icoop/fire", "fire");
        randomFloat -= deltaTime;
        if (randomFloat <= 0) {
            randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
            if (getOwnerArea().canEnterAreaCells(flames, flames.getCurrentCells())) {
                getOwnerArea().registerActor(flames);
            }
        }
    }

    /**
     * Draws the HellSkull and its death animation if applicable.
     *
     * @param canvas (Canvas): The canvas to draw the HellSkull on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (health.isOff()) {
            deathAnimation.draw(canvas);
        } else {
            orientedAnimation.draw(canvas);
        }

    }


    /**
     * Determines whether the HellSkull takes up cell space.
     *
     * @return (boolean): Always false, as HellSkull does not block cell space.
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
     * Inner class handling interactions for the HellSkull.
     */
    private static class HellSkullInteractionHandler implements ICoopInteractionVisitor {
        /**
         * Interacts with a player, dealing fire damage.
         *
         * @param player            (ICoopPlayer): The player to interact with.
         * @param isCellInteraction (boolean): True if the interaction occurs at the cell level.
         */
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.looseHp(1, "fire");
            }
        }
    }
}
