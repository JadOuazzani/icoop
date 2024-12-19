package ch.epfl.cs107.icoop.area;


import ch.epfl.cs107.icoop.actor.object.Coin;
import ch.epfl.cs107.icoop.actor.object.Door;
import ch.epfl.cs107.icoop.actor.object.Key;
import ch.epfl.cs107.icoop.actor.object.Teleportation;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

/**
 * The Arena class represents a more complex game area with interactive elements.
 */
public class Arena extends ICoopArea {
    private Key redKey;// The red key required for unlocking the teleportation
    private Key blueKey;// The blue key required for unlocking the teleportation
    private boolean isRegistered; // Tracks if coins are already registered

    /**
     * Updates the area, adding a coin once both keys are active.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.isOn() && !isRegistered) {
            Coin coin = new Coin(this, Orientation.DOWN, new DiscreteCoordinates(10, 11));
            registerActor(coin);
            coin.update(deltaTime);
            isRegistered = true;

        }
    }

    /**
     * Creates the area by registering its elements.
     */
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        redKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 16), "fire");
        registerActor(redKey);
        blueKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 4), "water");
        registerActor(blueKey);

        Door teleporter = new Teleportation("Spawn", new Logic() {
            @Override
            public boolean isOn() {
                return (redKey.isOn() && blueKey.isOn());
            }

            @Override
            public boolean isOff() {
                return (redKey.isOff() || blueKey.isOff());
            }
        }, List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6)), this, new DiscreteCoordinates(10, 10), Orientation.DOWN);
        registerActor(teleporter);
    }


    /**
     * Checks if the area is "on," based on the state of the keys.
     *
     * @return (boolean): True if both keys are active.
     */
    @Override
    public boolean isOn() {
        if (redKey != null && blueKey != null) {
            return (redKey.isOn() && blueKey.isOn());
        } else {
            return false;
        }
    }

    /**
     * Checks if the area is "off," based on the state of the keys.
     *
     * @return (boolean): True if either key is inactive.
     */
    @Override
    public boolean isOff() {
        if (redKey != null && blueKey != null) {
            return (redKey.isOff() || blueKey.isOff());
        } else {
            return true;
        }
    }


    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List<DiscreteCoordinates>): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14, 15));
    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "Arena";
    }

}
