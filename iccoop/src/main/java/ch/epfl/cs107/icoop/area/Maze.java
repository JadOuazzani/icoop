package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosif;
import ch.epfl.cs107.icoop.actor.Rochers;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class Maze extends ICoopArea {
    private DialogHandler jeu;

    public Maze(DialogHandler jeu) {
        this.jeu = jeu;
    }


    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    public String getTitle() {
        return "Maze";
    }

    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates (2,39), new DiscreteCoordinates(3,39) );
    }

}


