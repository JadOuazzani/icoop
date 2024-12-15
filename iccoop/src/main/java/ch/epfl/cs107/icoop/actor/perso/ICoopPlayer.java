package ch.epfl.cs107.icoop.actor.perso;

import ch.epfl.cs107.icoop.actor.foe.Fire;
import ch.epfl.cs107.icoop.actor.object.Door;
import ch.epfl.cs107.icoop.actor.object.ElementalEntity;
import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.object.Orb;
import ch.epfl.cs107.icoop.actor.object.PressurePlate;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.icoop.utilities.KeyBindings;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
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


public class ICoopPlayer extends MovableAreaEntity implements Interactor, ElementalEntity, Inventory.Holder {

    private final static int MOVE_DURATION = 6;
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
    private ICoopInventory inventory;
    private ICoopItem currentItem;
    private static final List<ICoopItem> NAVIGATION_LIST = List.of(ICoopItem.SWORD, ICoopItem.FIRE_KEY, ICoopItem.WATER_KEY, ICoopItem.FIRE_STAFF, ICoopItem.WATER_STAFF, ICoopItem.EXPLOSIVE);

    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String prefixe, String item, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.item = item;
        animation = new OrientedAnimation(prefixe, ANIMATION_DURATION, this, anchor, orders, 4, 1, 2, 16, 32, true);
        this.keys = keys;
        this.keyboard = owner.getKeyboard();
        resetMotion();
        handler = new ICoopPlayerInteractionHandler();
        health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);
        invulnerable = false;
        inventory = new ICoopInventory("main pocket");
        inventory.addPocketItem(ICoopItem.SWORD, 1);
        inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);// à changer dans les extensions ?
        currentItem = ICoopItem.SWORD;
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
            periodeImmunite -= (int) (deltaTime / 2);
        }

        if (periodeImmunite <= 0) { // Possiblité d'ameliorer ?
            immunite = false;
            periodeImmunite = 24;
        }
        if (keyboard.get(keys.switchItem()).isPressed()) {
            switchItem();
        }
        invokeItem(currentItem);


    }

    public void switchItem() {
        int index = NAVIGATION_LIST.indexOf(currentItem);
        do {
            index = (index + 1) % NAVIGATION_LIST.size();
        } while (!possess(NAVIGATION_LIST.get(index)));
        currentItem = NAVIGATION_LIST.get(index);

    }

    public void invokeItem(ICoopItem item) {
        if (keyboard.get(keys.useItem()).isPressed()) {
            switch (item) {
                case SWORD:
                    break;
                case FIRE_KEY:
                    break;
                case WATER_KEY:
                    break;
                case FIRE_STAFF:
                    break;
                case WATER_STAFF:
                    break;
                case EXPLOSIVE:
                    Explosif bombe = new Explosif(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
                    if (getOwnerArea().canEnterAreaCells(bombe, bombe.getCurrentCells())) {
                        getOwnerArea().registerActor(bombe);
                        bombe.setActiver(true);
                        inventory.removePocketItem(ICoopItem.EXPLOSIVE, 1);
                        if (!possess(item)) {
                            switchItem();
                        }
                    }
                    break;
            }
        }
    }

    public ICoopItem getCurrentItem() {
        return currentItem;
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

    public void perteDegats(int degats, String typeDegats) {
        if (typeDegats.equals("physique") || !(typeDegats.equals(getItem()))) {
            if (!immunite) {
                health.decrease(degats);
                immunite = true;
            }
        } else if (typeDegats.equals(getItem())) {
            if (!invulnerable && !immunite) {
                health.decrease(degats);
                immunite = true;
            }
        }
    }

    @Override
    public String item() {
        return item;
    }

    @Override
    public boolean possess(InventoryItem item) {

        return inventory.contains(item);
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
                inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);
            }

            if (wantsCellInteraction() && currentItem == ICoopItem.EXPLOSIVE) {
                explosif.setActiver(true);
            }
        }


        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {
                orb.getJeu().publish(orb.getDialog());
                orb.collectBy(ICoopPlayer.this::getItem);
                setInvulnerable(true);
            }
        }

        public void interactWith(PressurePlate plate, boolean isCellIInteraction) {
            if (wantsCellInteraction() && isCellIInteraction) {
                plate.setOccupier(ICoopPlayer.this);
                plate.setInteractorHere(true);
            }
        }

        @Override
        public void interactWith(Fire fire, boolean isCellInteraction) {
            if (isCellInteraction) {
                getOwnerArea().unregisterActor(fire);
            }
        }
    }

}
