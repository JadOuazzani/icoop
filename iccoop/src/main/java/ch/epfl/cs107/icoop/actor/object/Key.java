package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends ElementalItem {

    private Sprite sprite;

    public Key(Area area, Orientation orientation, DiscreteCoordinates position, String item) {
        super(area, orientation, position, item);
        if (item.equals("water")) {
            sprite = new Sprite("icoop/key_blue", 0.6f, 0.6f, this);
        } else if (item.equals("fire")) {
            sprite = new Sprite("icoop/key_red", 0.6f, 0.6f, this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }

    @Override
    public String item() {
        return getItem();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


}
