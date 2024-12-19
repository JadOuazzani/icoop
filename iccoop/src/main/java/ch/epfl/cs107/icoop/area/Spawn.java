package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

import static java.awt.ComponentOrientation.getOrientation;

/**
 * The Spawn class represents the starting area of the game.
 * It introduces players to the game with initial objects and portals.
 */
public class Spawn extends ICoopArea {
    private DialogHandler jeu;// Handler for managing in-game dialogs
    private Dialog welcome = new Dialog("Welcome");
    private Portail portail;// Welcome dialog displayed to players
    private Logic signalJeu; // Signal controlling the state of the game

    public Spawn(DialogHandler jeu, Logic signalJeu) {
        this.jeu = jeu;
        jeu.publish(welcome);
        this.signalJeu = signalJeu;
    }


    /**
     * Creates the area by registering its elements, such as doors, explosives, rocks, and a portal.
     */

    @Override
    public void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        Door door = new Door("OrbWay", Logic.TRUE, List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)), this, new DiscreteCoordinates(19, 15), Orientation.DOWN, new DiscreteCoordinates(19, 16));
        registerActor(door);
        Explosive explosive1 = new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(11, 10));
        registerActor(explosive1);
        Explosive explosive2 = new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(6, 7));
        registerActor(explosive2);
        Explosive explosive3 = new Explosive(this, Orientation.DOWN, new DiscreteCoordinates(14, 4));
        registerActor(explosive3);
        Rochers rochers = new Rochers(this, Orientation.DOWN, new DiscreteCoordinates(10, 10));
        registerActor(rochers);
        Door door2 = new Door("Maze", Logic.TRUE, List.of(new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39)), this, new DiscreteCoordinates(4, 0), Orientation.DOWN, new DiscreteCoordinates(5, 0));
        registerActor(door2);
        portail = new Portail("SanctumEntrance", new Logic() {
            @Override
            public boolean isOn() {
                return true;
            }

            @Override
            public boolean isOff() {
                return signalJeu.isOff();
            }
        }, List.of(new DiscreteCoordinates(2, 1), new DiscreteCoordinates(2, 2)), this, new DiscreteCoordinates(6, 11), Orientation.DOWN, jeu);
        registerActor(portail);
    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "Spawn";
    }

    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List<DiscreteCoordinates>): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6));
    }

    /**
     * Checks if the area is "on," which is determined by the game signal.
     *
     * @return (boolean): True if the game signal is on.
     */
    @Override
    public boolean isOn() {
        return signalJeu.isOn();
    }

    /**
     * Checks if the area is "off," which is determined by the game signal.
     *
     * @return (boolean): True if the game signal is off.
     */
    @Override
    public boolean isOff() {
        return signalJeu.isOff();
    }
}
