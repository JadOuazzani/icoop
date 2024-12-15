package ch.epfl.cs107.icoop.actor.object;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Orb extends ElementalItem {
    private final static int ANIMATION_FRAMES = 6;
    private final static int ANIMATION_DURATION = 24;
    final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
    private final Animation animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES, sprites);
    private final DialogHandler jeu;
    private final Dialog dialog;

    public enum OrbType {
        EAU("water", new Dialog("orb_water_msg"), 0),
        FEU("fire", new Dialog("orb_fire_msg"), 64);

        private final String element;
        private final int spriteYDelta;
        private final Dialog dialogue;

        OrbType(String item, Dialog dialogue, int spriteYDelta) {
            this.element = item;
            this.spriteYDelta = spriteYDelta;
            this.dialogue = dialogue;
        }

        public int getSpriteYDelta() {
            return spriteYDelta;
        }

        public String getItem() {
            return element;
        }

        public Dialog getDialogue() {
            return dialogue;
        }
    }

    public Orb(Area area, Orientation orientation, DiscreteCoordinates position, OrbType orbType, DialogHandler jeu) {
        super(area, orientation, position, orbType.getItem());
        this.jeu = jeu;
        this.dialog = orbType.getDialogue();
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this, new RegionOfInterest(i * 32, orbType.getSpriteYDelta(), 32, 32));
        }
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public String item() {
        return "";
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
    }

    @Override
    public void collectBy(ElementalEntity item) {
        super.collectBy(item);
    }

    public DialogHandler getJeu() {
        return jeu;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
