package ch.epfl.cs107.icoop.area;


import ch.epfl.cs107.icoop.actor.foe.logMonster;
import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

/**
 * The SanctumEntrance class represents an area with multiple monsters and a pathway to the Sanctum.
 */
public class SanctumEntrance extends ICoopArea {

    private DialogHandler game;

    /**
     * Constructor for the SanctumEntrance class.
     *
     * @param game (DialogHandler): Handler for in-game dialogs.
     */
    public SanctumEntrance(DialogHandler game) {
        this.game = game;
    }


    /**
     * Creates the area by registering its elements, including monsters, a door, and an orb.
     */
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        logMonster monster1 = new logMonster(this, Orientation.RIGHT, new DiscreteCoordinates(6, 13), List.of("fire", "physical"));
        registerActor(monster1);
        logMonster monster2 = new logMonster(this, Orientation.RIGHT, new DiscreteCoordinates(6, 10), List.of("fire", "physical"));
        registerActor(monster2);
        logMonster monster3 = new logMonster(this, Orientation.RIGHT, new DiscreteCoordinates(6, 7), List.of("fire", "physical"));
        registerActor(monster3);
        logMonster monster4 = new logMonster(this, Orientation.LEFT, new DiscreteCoordinates(14, 9), List.of("fire", "physical"));
        registerActor(monster4);
        logMonster monster5 = new logMonster(this, Orientation.LEFT, new DiscreteCoordinates(14, 12), List.of("fire", "physical"));
        registerActor(monster5);
        Door door = new Door("Sanctum", Logic.TRUE, List.of(new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8, 0)), this,
                new DiscreteCoordinates(9, 13), Orientation.DOWN, new DiscreteCoordinates(8, 13), new DiscreteCoordinates(10, 13), new DiscreteCoordinates(11, 13));
        registerActor(door);
        Orb speedOrb = new Orb(this, Orientation.DOWN, new DiscreteCoordinates(10, 10), Orb.OrbType.VITESSE, game);
        registerActor(speedOrb);
    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "SanctumEntrance";
    }

    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List<DiscreteCoordinates>): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(2, 1), new DiscreteCoordinates(2, 2));
    }

    /**
     * Checks if the area is "on." This area is always "on."
     *
     * @return (boolean): Always true.
     */
    @Override
    public boolean isOn() {
        return true;
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
