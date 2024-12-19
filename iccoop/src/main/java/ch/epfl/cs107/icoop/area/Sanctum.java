package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.foe.DarkLord;
import ch.epfl.cs107.icoop.actor.object.Coin;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

/**
 * The Sanctum class represents a boss area featuring the Dark Lord as the final challenge.
 */
public class Sanctum extends ICoopArea {
    private DialogHandler game;// Handler for managing in-game dialogs
    private boolean isRegistered = false;// Tracks if the coin has been registered
    private DarkLord darkLord;// The boss of the area

    /**
     * Constructor for the Sanctum class.
     *
     * @param game (DialogHandler): Handler for in-game dialogs.
     */
    public Sanctum(DialogHandler game){
        this.game = game;
    }

    /**
     * Creates the area by registering its elements, including the Dark Lord.
     */
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        darkLord = new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(7, 9), List.of("physical", "fire", "water"), 16, game);
        registerActor(darkLord);
    }

    /**
     * Updates the area, adding a coin if the Dark Lord is defeated.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.isOn() && !isRegistered) {
            Coin coin = new Coin(this, Orientation.DOWN, new DiscreteCoordinates(7, 9));
            registerActor(coin);
            coin.update(deltaTime);
            isRegistered = true;

        }
    }

    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List < DiscreteCoordinates >): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8, 0));
    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "Sanctum";
    }

    /**
     * Checks if the area is "on," which occurs when the Dark Lord is defeated.
     *
     * @return (boolean): True if the Dark Lord is dead.
     */
    @Override
    public boolean isOn() {
        return darkLord.isDead();
    }

    /**
     * Checks if the area is "off." This area is never "off."
     *
     * @return (boolean): Always false.
     */
    @Override
    public boolean isOff() {
        return false;
    }
}
