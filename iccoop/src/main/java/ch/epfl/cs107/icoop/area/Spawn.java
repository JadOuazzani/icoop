package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


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

public class Spawn extends ICoopArea {
    private DialogHandler jeu;
    private Dialog welcome = new Dialog("Welcome");
    private Portail portail;
    private Logic signalJeu;

    public Spawn(DialogHandler jeu, Logic signalJeu) {
        this.jeu = jeu;
        jeu.publish(welcome);
        this.signalJeu = signalJeu;
    }

    @Override
    public void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
//        Coin coin1 = new Coin(this,Orientation.DOWN, new DiscreteCoordinates(8,8));
//        Coin coin2 = new Coin(this,Orientation.DOWN, new DiscreteCoordinates(7,8));
//        Coin coin3 = new Coin(this,Orientation.DOWN, new DiscreteCoordinates(6,8));
//        registerActor(coin1);
//        registerActor(coin2);
//        registerActor(coin3);


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
        }, List.of(new DiscreteCoordinates(2,1), new DiscreteCoordinates(2,2)), this, new DiscreteCoordinates(6, 11), Orientation.DOWN, jeu);
        registerActor(portail);
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6));
    }

    @Override
    public boolean isOn() {
        return signalJeu.isOn();
    }

    @Override
    public boolean isOff() {
        return signalJeu.isOff();
    }
}
