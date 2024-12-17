package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.actor.object.Door;
import ch.epfl.cs107.icoop.actor.object.ElementalEntity;
import ch.epfl.cs107.icoop.actor.perso.Health;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class HellSkull extends Foe {
    private Orientation[] orders = new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT};
    private OrientedAnimation orientedAnimation;
    private float randomFloat;
    private Fire flammes;
    private static final int ANIMATION_DURATION = 12;
    private HellSkullInteractionHandler handler;

    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position, List<String> vulnerabilite) {
        super(area, orientation, position, vulnerabilite, 1);
        orientedAnimation = new OrientedAnimation("icoop/flameskull", ANIMATION_DURATION / 3, this, new Vector(-0.5f, -0.5f), orders, 3, 2, 2, 32, 32, true);
        randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
        handler = new HellSkullInteractionHandler();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        flammes = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3, 40);
        randomFloat -= deltaTime;
        if (randomFloat <= 0) {
            randomFloat = RandomGenerator.getInstance().nextFloat(0.5f, 2f);
            if (getOwnerArea().canEnterAreaCells(flammes, flammes.getCurrentCells())) {
                getOwnerArea().registerActor(flammes);
            }
        }
        if (health.isOff()) {
            deathAnimation.update(deltaTime);
        } else {
            orientedAnimation.update(deltaTime);
        }

        if (health.isOff() && deathAnimation.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (health.isOff()) {
            deathAnimation.draw(canvas);
        } else {
            orientedAnimation.draw(canvas);
        }

    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        super.acceptInteraction(v, isCellInteraction);
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class HellSkullInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.perteDegats(1, "fire");
            }
        }
    }
}
