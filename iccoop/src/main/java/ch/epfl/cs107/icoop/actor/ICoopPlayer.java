package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.utilities.KeyBindings;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;


public class ICoopPlayer extends MovableAreaEntity implements Interactor, ElementalEntity {

    private final static int MOVE_DURATION = 8;
    public String item;
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
    private final Health health;
    private static final int MAX_LIFE = 5;
    private boolean invulnerable;
    private boolean immunite;
    private int periodeImmunite = 24;


    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String prefixe, String item, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.item = item;
        animation = new OrientedAnimation(prefixe, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32,
                true);
        this.keys = keys;
        this.keyboard = owner.getKeyboard();
        resetMotion();
        handler = new ICoopPlayerInteractionHandler();
        health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);
        invulnerable = false;
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

        if (immunite) {
            periodeImmunite -= deltaTime / 2;
        }

        if (periodeImmunite <= 0) { // Possiblité d'ameliorer ?
            immunite = false;
            periodeImmunite = 24;
        }
    }

    public Health getHealth() {
        return health;
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
        other.acceptInteraction(handler, isCellInteraction);
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
        if (!immunite) {
            animation.draw(canvas);
            health.draw(canvas);
        }
        if (immunite && periodeImmunite % 7 == 0) {
            animation.draw(canvas);
            health.draw(canvas);
        }
    }


    public void perteDegats(int degats) {
        if (!invulnerable && !immunite) {
            health.decrease(degats);
            immunite = true;
        }
    }


    @Override
    public String item() {
        return item;
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                contactPorte = true;
                joueurPosition = door.getCoordArrive();
                areaKey = door.getAirTransitoire();
            }
        }


        @Override
        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                explosif.setActiver(true);
            }

            if (wantsCellInteraction() && isCellInteraction) {
                explosif.disparition();
            }
        }


        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {
                orb.getJeu().publish(orb.getDialog());
                orb.collectBy(ICoopPlayer.this::getItem);
            }
        }

//        public void interactWith(PressurePlate plate, boolean isCellInteractive) {
//            if (wantsCellInteraction()) {
//                plate.getSignal().isOn();
//            }
//        }
    }


    public String getAreaKey() {
        return areaKey;
    }

    public String getItem() {
        return item;
    }

    public boolean isContactPorte() {
        return contactPorte;
    }

    public List<DiscreteCoordinates> getJoueurPosition() {
        return joueurPosition;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean getInvulnerable() {
        return invulnerable;
    }


}



