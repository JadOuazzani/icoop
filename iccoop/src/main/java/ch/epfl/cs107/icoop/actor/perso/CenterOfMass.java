package ch.epfl.cs107.icoop.actor.perso;

import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;

/**
 * CenterOfMass represents a virtual actor that calculates the center of mass and average velocity of a group of actors.
 *
 * <p>This class implements the `Actor` interface and calculates its position and velocity
 * as the average of the positions and velocities of the provided actors. It does not have its own independent behavior
 * but serves as a utility for grouping actors and analyzing their collective properties.</p>
 */

 public class CenterOfMass implements Actor {
    /** Array of actors whose collective properties are represented by this center of mass. */

    private final Actor[] actors;

    /**
     * Constructor for a CenterOfMass.
     *
     * @param actor        (Actor): The first actor to include in the center of mass calculation.
     * @param restOfActors (Actor...): Additional actors to include in the center of mass calculation.
     */

    public CenterOfMass(Actor actor, Actor... restOfActors) {
        this.actors = new Actor[restOfActors.length + 1];
        actors[0] = actor;
        System.arraycopy(restOfActors, 0, this.actors, 1, restOfActors.length);
    }

    @Override
    /**
     * Calculates the average position of all actors in the group.
     *
     * @return (Vector): The position of the center of mass.
     */
    public Vector getPosition() {
        Vector position = Vector.ZERO;
        for (Actor actor : actors) {
            position = position.add(actor.getPosition());
        }
        return position.mul(1f / actors.length);
    }

    @Override
    /**
     * Calculates the transformation of the center of mass, based on its position.
     *
     * @return (Transform): A transform object representing the position of the center of mass.
     */
    public Transform getTransform() {
        return Transform.I.translated(getPosition());
    }

    @Override
    /**
     * Calculates the average velocity of all actors in the group.
     *
     * @return (Vector): The velocity of the center of mass.
     */
    public Vector getVelocity() {
        Vector velocity = Vector.ZERO;
        for (Actor actor : actors) {
            velocity = velocity.add(actor.getVelocity());
        }
        return velocity.mul(1f / actors.length);
    }
}
