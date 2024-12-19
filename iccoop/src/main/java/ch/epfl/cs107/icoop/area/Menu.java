package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;


import java.util.List;

import static java.awt.ComponentOrientation.getOrientation;

/**
 * The Ending class represents the final area of the game.
 */
public class Menu extends ICoopArea {

    /**
     * Default constructor for the Ending class.
     */
    public Menu() {
    }

    /**
     * Creates the area by registering its foreground.
     */
    @Override
    public void createArea() {
        registerActor(new Foreground(this));

    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "Menu";
    }

    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List < DiscreteCoordinates >): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6));
    }

    /**
     * Checks if the area is currently "on."
     *
     * @return (boolean): Always true for this area.
     */
    @Override
    public boolean isOn() {
        return true;
    }

    /**
     * Checks if the area is currently "off."
     *
     * @return (boolean): Always false for this area.
     */
    @Override
    public boolean isOff() {
        return false;
    }
}
