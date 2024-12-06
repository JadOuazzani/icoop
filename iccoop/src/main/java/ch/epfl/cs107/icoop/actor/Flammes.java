package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Flammes extends Projectile {
    private Animation animation;

    public Flammes(Area area, Orientation orientation, DiscreteCoordinates position, int vitesse, int distancemax) {
        super(area, orientation, position, vitesse, distancemax);
        animation = new Animation("icoop/fire", 7, 1, 1, this, 16, 16, 4, true);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
    }

    private class FlammesInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.getHealth().decrease(1);
            }
        }
        public void interactWith(Explosif explosif,boolean isCellInteraction) {
            if(isCellInteraction) {
                explosif.setActiver(true);
            }
        }

    }
}
