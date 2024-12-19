package ch.epfl.cs107.icoop.actor.object;




 import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Rock represents a destructible obstacle in the game world.
 *
 * <p>A Rock is an `Obstacle` that can be destroyed by certain interactions, such as explosions.
 * Once destroyed, it is unregistered from the game area.</p>
 */

public class Rock extends Obstacle {
    /** Sprite representing the rock's visual appearance. */

    private Sprite sprite;
    /** Indicates if the rock has been destroyed. */

    private boolean destroyed;

    /**
     * Constructor for a Rock.
     *
     * @param area        (Area): The area where the rock is located.
     * @param orientation (Orientation): The orientation of the rock.
     * @param position    (DiscreteCoordinates): The position of the rock in the area.
     */

    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("rock.1", 1f, 1f, this);
        destroyed = false;
    }

    @Override
    /**
     * Updates the state of the rock.
     *
     * <p>If the rock is marked as destroyed, it will be unregistered from the area.</p>
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (destroyed){
           getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    /**
     * Draws the rock on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw the rock.
     */
    public void draw(Canvas canvas) {
            sprite.draw(canvas);
    }
    /**
     * Marks the rock as destroyed.
     *
     * <p>This flag determines whether the rock should be removed from the game area.</p>
     *
     * @param destroyed (boolean): True to mark the rock as destroyed, false otherwise.
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if the interaction occurs in the same cell.
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
