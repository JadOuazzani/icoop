package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.Coeur;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Orb;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.DOWN;

public class OrbWay extends ICoopArea {
    private DialogHandler jeu;

    public OrbWay(DialogHandler jeu) {
        this.jeu = jeu;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        Door door1 = new Door("Spawn", Logic.TRUE, List.of(
                new DiscreteCoordinates(18, 16),
                new DiscreteCoordinates(18, 15)),
                this,
                new DiscreteCoordinates(0, 14),
                DOWN,
                new DiscreteCoordinates(0, 13), new DiscreteCoordinates(0, 12), new DiscreteCoordinates(0, 11), new DiscreteCoordinates(0, 10));

        Door door2 = new Door("Spawn", Logic.TRUE, List.of(
                new DiscreteCoordinates(18, 16),
                new DiscreteCoordinates(18, 15)),
                this,
                new DiscreteCoordinates(0, 8),
                DOWN,
                new DiscreteCoordinates(0, 7), new DiscreteCoordinates(0, 6), new DiscreteCoordinates(0, 5), new DiscreteCoordinates(0, 4));
        registerActor(door1);
        registerActor(door2);
        Orb orbeFeu1 = new Orb(this, Orientation.UP, new DiscreteCoordinates(17,12), Orb.OrbType.FEU, jeu);
        registerActor(orbeFeu1);
        Orb orbeEau1 = new Orb(this, Orientation.UP, new DiscreteCoordinates(17,6), Orb.OrbType.EAU, jeu);
        registerActor(orbeEau1);
        Coeur coeur1 = new Coeur(this,DOWN,new DiscreteCoordinates(8,4));
        registerActor(coeur1);
        PressurePlate plate1 = new PressurePlate(Logic.FALSE,this, new DiscreteCoordinates ( 5,7), DOWN );
        registerActor(plate1);
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }

    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5));
    }



}
