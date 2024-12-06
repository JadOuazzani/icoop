package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

public class Cranes extends Foe{
    Cranes(Area area, Orientation orientation, DiscreteCoordinates position, List<ElementalEntity> vulnerabilite, String prefixe) {
        super(area, orientation, position, vulnerabilite, prefixe);

    }
}
