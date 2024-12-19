package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Explosive;
import ch.epfl.cs107.icoop.actor.perso.Health;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

/**
 * The Foe class represents a generic enemy in the game.
 * It includes health management, interaction handling, and movement behavior.
 */
public abstract class Foe extends MovableAreaEntity implements Interactor {
    protected static final int ANIMATION_DURATION = 24;
    private static int imunityPeriod = 24;
    protected Health health;
    protected int MAX_LIFE;
    private boolean isDead;
    private boolean imunity;
    final Vector anchor = new Vector(-0.5f, -0.5f);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    protected Animation deathAnimation;
    List<String> vulnerabilite;
    private FoeInteractionHandler handler;

    /**
     * Constructor for the Foe class.
     *
     * @param area          (Area): The area where the Foe exists.
     * @param orientation   (Orientation): Initial orientation of the Foe.
     * @param position      (DiscreteCoordinates): Initial position of the Foe.
     * @param vulnerabilite (List<String>): List of vulnerabilities of the Foe.
     * @param MAX_LIFE      (int): Maximum health points of the Foe.
     */

    Foe(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite, final int MAX_LIFE) {
        super(area, orientation, position);
        this.MAX_LIFE = MAX_LIFE;
        this.vulnerabilite = vulnerabilite;
        deathAnimation = new Animation("icoop/vanish", 7, 2, 2, this, 32, 32, new Vector(-0.5f, 0f), ANIMATION_DURATION / 9, false);

        health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, false);
        isDead = false;
        imunity = false;
        handler = new FoeInteractionHandler();
    }

    /**
     * Checks if the Foe is dead.
     *
     * @return (boolean): True if the Foe is dead, otherwise false.
     */
    public boolean isDead() {
        return isDead;
    }


    /**
     * Draws the Foe and its health on the given canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        health.draw(canvas);
    }

    /**
     * Updates the Foe's state, handling death and immunity.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (health.isOff()) {
            deathAnimation.update(deltaTime);
            if (deathAnimation.isCompleted()) {
                getOwnerArea().unregisterActor(this);
            }
            isDead = true;
        }
        if (imunity) {
            imunityPeriod -= (int) (deltaTime / 2);
        }

        if (imunityPeriod <= 0) {
            imunity = false;
            imunityPeriod = 24;
        }

    }


    /**
     * Checks if the Foe has immunity.
     *
     * @return (boolean): True if the Foe has immunity, otherwise false.
     */
    public boolean getImunity() {
        return imunity;
    }


    /**
     * Determines if the Foe occupies a cell.
     *
     * @return (boolean): True if the Foe occupies a cell and is not dead.
     */
    @Override
    public boolean takeCellSpace() {
        return !isDead;
    }

    /**
     * Retrieves the current cells occupied by the Foe.
     *
     * @return (List < DiscreteCoordinates >): A singleton list of the Foe's current main cell.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());

    }

    /**
     * Retrieves the field of view cells for the Foe.
     *
     * @return (List < DiscreteCoordinates >): A list of cells in the Foe's field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));

    }

    /**
     * Checks if the Foe wants to interact with a cell.
     *
     * @return (boolean): True if the Foe wants to interact with a cell.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }


    /**
     * Checks if the Foe wants to interact via view.
     *
     * @return (boolean): True if the Foe wants to interact via view.
     */
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    /**
     * Handles interactions with other interactable objects.
     *
     * @param other             (Interactable): The object to interact with.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
    }


    /**
     * Checks if the Foe can be interacted with via cell.
     *
     * @return (boolean): True, as the Foe can always be interacted with via cell.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Checks if the Foe can be interacted with via view.
     *
     * @return (boolean): True, as the Foe can always be interacted with via view.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Accepts an interaction visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Reduces the Foe's health if the type of attack matches its vulnerabilities.
     *
     * @param degats (int): The amount of damage to reduce.
     * @param type   (String): The type of the attack.
     */
    public void looseHp(int degats, String type) {
        for (int i = 0; i < vulnerabilite.size(); i++) {
            if (vulnerabilite.get(i).equals(type)) {
                health.decrease(degats);
            }
        }
    }


    /**
     * Inner class handling interactions for the Foe.
     */
    public class FoeInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Interacts with an explosive, triggering its disappearance.
         *
         * @param explosive         (Explosive): The explosive to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {
                explosive.disappears();
            }
        }

    }

}
