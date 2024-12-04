package ch.epfl.cs107.icoop.actor;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class ElementalWall extends Obstacle implements ElementalEntity, Interactor {
    private Sprite[] wallSprites;
    private String item;
    private boolean takeCellSpace;
    private WallInteractionHandler handler;
    private Logic actif;

    public enum WallType {
        EAU("water", "water_wall"),
        FEU("fire", "fire_wall");

        private String spriteName;
        private String item;

        WallType(String item, String spriteName) {
            this.spriteName = spriteName;
            this.item = item;
        }

        public String getSpriteName() {
            return spriteName;
        }


    }

    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, Logic actif, WallType wallType) {
        super(area, orientation, position);
        item = wallType.item;
        wallSprites = RPGSprite.extractSprites(wallType.getSpriteName(), 4, 1, 1, this, Vector.ZERO, 256, 256);
        handler = new WallInteractionHandler();
        this.actif = actif;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (actif.isOn()){
            wallSprites[getOrientation().ordinal()].draw(canvas);
        }
    }

    @Override
    public String item() {
        return item;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return this.getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return takeCellSpace;
    }

    public void setTakeCellSpace(boolean variable) {
        takeCellSpace = variable;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class WallInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            setTakeCellSpace(!player.getItem().equals(item) && actif.isOn());
            if (isCellInteraction && wantsCellInteraction() && actif.isOn()) {
                player.perteDegats(1);
            }
        }
    }
}

