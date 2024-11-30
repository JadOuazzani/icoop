package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.icoop.KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;
import static ch.epfl.cs107.play.math.Orientation.*;


public class ICoopPlayer extends MovableAreaEntity implements Interactor, ElementalEntity {

    private final static int MOVE_DURATION = 8;
    private final TextGraphics message;
    private float hp;
    private String element;
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private OrientedAnimation animation;
    private static final int ANIMATION_DURATION = 4;
    private KeyBindings.PlayerKeyBindings keys;
    private Keyboard keyboard;
    private boolean contactPorte;
    private List<DiscreteCoordinates> joueurPosition;
    private String areaKey;
    private final ICoopPlayerInteractionHandler handler;


    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String prefixe, String element, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.hp = 10;
        this.element = element;
        message = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        animation = new OrientedAnimation(prefixe, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32,
                true);
        this.keys = keys;
        this.keyboard = owner.getKeyboard();
        resetMotion();
        this.handler = new ICoopPlayerInteractionHandler();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        moveIfPressed(LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));
        if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            animation.reset();
        }
    }


    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return keyboard.get(keys.useItem()).isPressed();
    }


    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        if (other instanceof Door) {
            other.acceptInteraction(handler, isCellInteraction);
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }


    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }


    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
        contactPorte = false;
    }


    public void centerCamera(ICoopPlayer redPlayer, ICoopPlayer bluePlayer) {
        CenterOfMass centerOfMass = new CenterOfMass(redPlayer, bluePlayer);
        getOwnerArea().setViewCandidate(centerOfMass);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public String element() {
        return element;
    }


    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                contactPorte = true;
                joueurPosition = door.getCoordArrive();
                areaKey = door.getAirTransitoire();
            }
        }
    }



    public String getAreaKey() {
        return areaKey;
    }

    public boolean isContactPorte() {
        return contactPorte;
    }

    public List<DiscreteCoordinates> getJoueurPosition() {
        return joueurPosition;
    }
}


interface ElementalEntity {
    public String element();
}
