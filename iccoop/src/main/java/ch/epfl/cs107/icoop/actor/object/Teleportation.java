package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Teleportation extends Door {
    private Sprite sprite;

    public Teleportation(String airTransitoire, Logic signal, List<DiscreteCoordinates> coordArrive, Area area, DiscreteCoordinates positionPrincipale, Orientation orientation) {
        super(airTransitoire, signal, coordArrive, area, positionPrincipale, orientation);
        sprite = new RPGSprite("shadow", 1, 1, this, new RegionOfInterest(0, 0, 32, 32));
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public void draw(Canvas canvas) {
        if (getSignal().isOn()) {
            sprite.draw(canvas);
        }

    }
}
