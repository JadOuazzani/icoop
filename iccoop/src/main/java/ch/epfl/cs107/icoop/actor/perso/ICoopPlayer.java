package ch.epfl.cs107.icoop.actor.perso;

import ch.epfl.cs107.icoop.actor.foe.FireBall;
import ch.epfl.cs107.icoop.actor.foe.Foe;
import ch.epfl.cs107.icoop.actor.foe.WaterBall;
import ch.epfl.cs107.icoop.actor.object.*;


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

/**
 * The ICoopPlayer class represents a player in the ICoop game. It manages the player's
 * attributes, animations, inventory, and interactions with other game elements.
 */

public class ICoopPlayer extends MovableAreaEntity implements Interactor, ElementalEntity, Inventory.Holder {

    private static int MOVE_DURATION;
    private static final int ANIMATION_DURATION = 4; // Animation duration for player movements
    private static final int MAX_LIFE = 5; // Maximum health points of the player
    private final static int SWORD_ANIMATION_DURATION = 2; // Duration for sword animation
    private final static int STAFF_ANIMATION_DURATION = 2; // Duration for staff animation

    private final String item; // The elemental type of the player (e.g., "water", "fire")
    private final Vector anchorIdle = new Vector(0, 0); // Anchor for idle animations
    private final Vector anchorStaff = new Vector(-.5f, -.20f); // Anchor for staff animations
    private final Orientation[] ordersIdle = {DOWN, RIGHT, UP, LEFT}; // Orientation order for idle animations
    private final Orientation[] ordersFight = {DOWN, UP, RIGHT, LEFT}; // Orientation order for fighting animations
    private OrientedAnimation animation;
    private OrientedAnimation swordAnimation; // Animation for sword usage
    private OrientedAnimation waterStaffAnimation; // Animation for water staff usage
    private OrientedAnimation fireStaffAnimation; // Animation for fire staff usage
    private OrientedAnimation idleAnimation; // Animation for idle state// The current animation being played

    private final KeyBindings.PlayerKeyBindings keys; // Keybindings for player controls
    private final Keyboard keyboard; // The keyboard instance for user input

    private List<DiscreteCoordinates> playerPosition; // Stores the player's position
    private DiscreteCoordinates manoirPortailCoord; // Coordinates for a specific area portal
    private String areaKey; // Key representing the area the player is in

    private final ICoopPlayerInteractionHandler handler; // Handles interactions with other entities

    private final Health health; // Represents the player's health
    private boolean invulnerable; // Indicates if the player is immune to all damage
    private boolean immunity; // Temporary immunity after taking damage
    private int periodImunity = 24; // Immunity duration period

    private boolean isAttacking; // Indicates if the player is currently attacking
    private boolean firsTime = true; // Tracks the first interaction with certain elements
    private boolean isDoorInContact; // Tracks if the player is in contact with a door

    private ICoopInventory inventory; // The player's inventory
    private ICoopItem currentItem; // The item currently being used by the player
    private static final List<ICoopItem> NAVIGATION_LIST = List.of(ICoopItem.SWORD, ICoopItem.FIRE_KEY, ICoopItem.WATER_KEY, ICoopItem.FIRE_STAFF, ICoopItem.WATER_STAFF, ICoopItem.EXPLOSIVE);

    private static int globalCoinCounter = 0; // Tracks the total number of coins collected by all players


    /**
     * Constructor for ICoopPlayer.
     *
     * @param owner       (Area): The area where the player is located.
     * @param orientation (Orientation): The initial orientation of the player.
     * @param coordinates (DiscreteCoordinates): The initial coordinates of the player.
     * @param prefixe     (String): Prefix for animation assets.
     * @param item        (String): The player's elemental type.
     * @param keys        (KeyBindings.PlayerKeyBindings): Keybindings for player controls.
     */


    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String prefixe, String item, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.item = item;
        idleAnimation = new OrientedAnimation(prefixe, ANIMATION_DURATION, this, anchorIdle, ordersIdle, 4, 1, 2, 16, 32, true);
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
        swordAnimation = new OrientedAnimation(prefixe + ".sword", SWORD_ANIMATION_DURATION, this, anchorStaff, ordersFight,
                4, 2, 2, 32, 32);
        waterStaffAnimation = new OrientedAnimation("icoop/player2.staff_water", STAFF_ANIMATION_DURATION, this,
                anchorStaff, ordersFight, 4, 2, 2, 32, 32);
        fireStaffAnimation = new OrientedAnimation("player.staff_fire", STAFF_ANIMATION_DURATION, this,
                anchorStaff, ordersFight, 4, 2, 2, 32, 32);
        animation = idleAnimation;
        MOVE_DURATION = 6;
    }

    /**
     * Gets the global coin count.
     *
     * @return (int): The total number of coins collected.
     */

    public static int getGlobalCoinCount() {
        return globalCoinCounter;
    }

    /**
     * Sets the global coin count.
     *
     * @param globalCoinCounter (int): The new global coin count.
     */

    public static void setGlobalCoinCounter(int globalCoinCounter) {
        ICoopPlayer.globalCoinCounter = globalCoinCounter;
    }

    /**
     * Updates the player's state.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        moveIfPressed(LEFT, keyboard.get(keys.left()));
        moveIfPressed(UP, keyboard.get(keys.up()));
        moveIfPressed(RIGHT, keyboard.get(keys.right()));
        moveIfPressed(DOWN, keyboard.get(keys.down()));

        if (immunity) {
            periodImunity -= deltaTime / 2;
            if (periodImunity <= 0) {
                immunity = false;
                periodImunity = 24;
            }
        }

        if (keyboard.get(keys.switchItem()).isPressed()) {
            switchItem();
        }

        if (!isAttacking) {
            invokeItem(currentItem);
        }

        if (isAttacking) {
            animation.update(deltaTime);
            if (animation.isCompleted()) {
                resetToIdle(); // Retour à l'état neutre après l'attaque
            }
        } else if (isDisplacementOccurs()) {
            animation.update(deltaTime);
        } else {
            resetToIdle();
        }
        if (!firsTime) {
            firsTime = !getCurrentMainCellCoordinates().equals(manoirPortailCoord);
        }
    }


    /**
     * Resets the player's state and animation to idle mode.
     * This method ensures that the player exits any active animations (e.g., attacks)
     * and returns to a neutral state, ready for new interactions or actions.
     */
    private void resetToIdle() {
        animation = idleAnimation;
        swordAnimation.reset();
        fireStaffAnimation.reset();
        waterStaffAnimation.reset();
        isAttacking = false;
    }

    /**
     * Switches to the next item in the navigation list that the player possesses.
     */
    public void switchItem() {

        int index = NAVIGATION_LIST.indexOf(currentItem);

        do {
            index = (index + 1) % NAVIGATION_LIST.size();
        } while (!possess(NAVIGATION_LIST.get(index)));
        currentItem = NAVIGATION_LIST.get(index);

    }

    /**
     * Invokes the player's current item.
     *
     * @param item (ICoopItem): The item to be invoked.
     */

    public void invokeItem(ICoopItem item) {
        if (keyboard.get(keys.useItem()).isPressed() && !isAttacking) {
            switch (item) {
                case SWORD:
                    animation = swordAnimation;
                    isAttacking = true;
                    break;
                case FIRE_STAFF:
                    animation = fireStaffAnimation;
                    FireBall fireball = new FireBall(
                            getOwnerArea(), getOrientation(),
                            getCurrentMainCellCoordinates().jump(getOrientation().toVector()),
                            3, 40
                    );
                    if (getOwnerArea().canEnterAreaCells(fireball, fireball.getCurrentCells())) {
                        getOwnerArea().registerActor(fireball);
                        isAttacking = true;
                    }
                    break;
                case WATER_STAFF:
                    animation = waterStaffAnimation;
                    WaterBall waterBall = new WaterBall(
                            getOwnerArea(), getOrientation(),
                            getCurrentMainCellCoordinates().jump(getOrientation().toVector()),
                            3, 40
                    );
                    if (getOwnerArea().canEnterAreaCells(waterBall, waterBall.getCurrentCells())) {
                        getOwnerArea().registerActor(waterBall);
                        isAttacking = true;
                    }
                    break;
                case EXPLOSIVE:
                    Explosive bombe = new Explosive(getOwnerArea(), DOWN, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
                    if (getOwnerArea().canEnterAreaCells(bombe, bombe.getCurrentCells())) {
                        getOwnerArea().registerActor(bombe);
                        inventory.removePocketItem(ICoopItem.EXPLOSIVE, 1);
                        if (!possess(item)) {
                            switchItem();
                        }
                    } else {
                        bombe.setActiver(true);
                    }
                    break;

                default:
                    isAttacking = false;
            }
        }
    }

    public ICoopItem getCurrentItem() {
        return currentItem;
    }

    public Health getHealth() {
        return health;
    }

    /**
     * Determines if the player occupies a cell.
     *
     * @return (boolean): True if the player occupies a cell.
     */

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * Checks if the player can interact with a cell.
     *
     * @return (boolean): True if the player can interact with a cell.
     */

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Checks if the player can interact with the view.
     *
     * @return (boolean): True if the player can interact with the view.
     */

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Retrieves the current cells occupied by the player.
     *
     * @return (List < DiscreteCoordinates >): The cells occupied by the player.
     */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Retrieves the field of view cells for the player.
     *
     * @return (List < DiscreteCoordinates >): The cells within the player's field of view.
     */

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }


    /**
     * Determines if the player wants to interact with a cell.
     *
     * @return (boolean): True if the player wants to interact with a cell.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Determines if the player wants to interact with the view.
     *
     * @return (boolean): True if the player wants to interact with the view.
     */
    @Override
    public boolean wantsViewInteraction() {
        return keyboard.get(keys.useItem()).isPressed();
    }


    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Accepts interactions from an interaction visitor.
     *
     * @param v                 (AreaInteractionVisitor): The interaction visitor.
     * @param isCellInteraction (boolean): True if it is a cell interaction.
     */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Moves the player in the specified orientation if the button is pressed.
     *
     * @param orientation (Orientation): The direction in which to move the player.
     * @param b           (Button): The button corresponding to the move action.
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown() && !isAttacking && !isDisplacementOccurs()) {

            orientate(orientation);
            move(MOVE_DURATION);

        }
    }

    /**
     * Removes the player from their current area.
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Places the player in a new area at the specified position.
     *
     * @param area     (Area): The new area where the player will be placed.
     * @param position (DiscreteCoordinates): The coordinates in the new area.
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
        isDoorInContact = false;
    }


    /**
     * Centers the camera based on the positions of two players.
     *
     * @param redPlayer  (ICoopPlayer): The first player.
     * @param bluePlayer (ICoopPlayer): The second player.
     */
    public void centerCamera(ICoopPlayer redPlayer, ICoopPlayer bluePlayer) {
        CenterOfMass centerOfMass = new CenterOfMass(redPlayer, bluePlayer);
        getOwnerArea().setViewCandidate(centerOfMass);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!immunity) {
            animation.draw(canvas);
            health.draw(canvas);
        }
        if (immunity && periodImunity % 7 == 0) {
            animation.draw(canvas);
            health.draw(canvas);
        }

    }

    /**
     * Decreases the player's health points based on the type of damage received.
     *
     * @param damage     (int): The amount of damage.
     * @param damageType (String): The type of damage (e.g., "physical", "fire").
     */
    public void looseHp(int damage, String damageType) {
        if (damageType.equals("physical")) {
            if (!immunity) {
                health.decrease(damage);
                immunity = true;
            }
        } else if (damageType.equals(getItem())) {
            if (!invulnerable && !immunity) {
                health.decrease(damage);
                immunity = true;
            }
        } else {
            if (!immunity) {
                health.decrease(damage);
                immunity = true;
            }
        }

    }

    @Override
    public String item() {
        return item;
    }

    /**
     * Checks if the player possesses a specific inventory item.
     *
     * @param item (InventoryItem): The item to check for possession.
     * @return (boolean): True if the player possesses the item, false otherwise.
     */

    @Override
    public boolean possess(InventoryItem item) {
        return inventory.contains(item);
    }

    /**
     * Retrieves the key of the current area.
     *
     * @return (String): The key of the current area.
     */

    public String getAreaKey() {
        return areaKey;
    }

    /**
     * Retrieves the player's elemental type.
     *
     * @return (String): The player's elemental type (e.g., "water", "fire").
     */
    public String getItem() {
        return item;
    }

    /**
     * Checks if the player is in contact with a door.
     *
     * @return (boolean): True if the player is in contact with a door, false otherwise.
     */

    public boolean isDoorInContact() {
        return isDoorInContact;
    }

    /**
     * Retrieves the player's current position.
     *
     * @return (List<DiscreteCoordinates>): The list of coordinates representing the player's position.
     */

    public List<DiscreteCoordinates> getPlayerPosition() {
        return playerPosition;
    }

    /**
     * Sets the player's invulnerability state.
     *
     * @param invulnerable (boolean): True to make the player invulnerable, false otherwise.
     */

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    /**
     * Resets the player's motion.
     * This method is overridden from the parent class to ensure proper reset functionality.
     */

    @Override
    public void resetMotion() {
        super.resetMotion();
    }

    /**
     * Sets the player's movement duration.
     *
     * @param moveDuration (int): The duration for a movement action.
     */

    public static void setMoveDuration(int moveDuration) {
        MOVE_DURATION = moveDuration;
    }

    /**
     * ICoopPlayerInteractionHandler is a private inner class of ICoopPlayer.
     * It handles all interactions between the player and other interactable objects in the game.
     */

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Handles interaction with a Door object.
         *
         * @param door              (Door): The door to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        public void interactWith(Door door, boolean isCellInteraction) {
            if (door.getSignal().isOn()) {
                isDoorInContact = true;
                playerPosition = door.getArrivalCoords();
                areaKey = door.getTransitoryArea();
            }
        }

        /**
         * Handles interaction with a Foe object:
         * foe loses life if receives a sword blow
         *
         * @param foe               (Foe): The foe to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction) {
            if (animation == swordAnimation && wantsViewInteraction()) {
                foe.looseHp(1, "physical");
            }
        }

        /**
         * Handles interaction with an Explosive object.
         * depends on the currentItem, can either activate the explosive or add it to his inventory
         *
         * @param explosive         (Explosive): The explosive to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                explosive.setActiver(true);
            }

            if (wantsCellInteraction() && isCellInteraction) {
                explosive.disappears();
                inventory.addPocketItem(ICoopItem.EXPLOSIVE, 1);
            }

            if (wantsCellInteraction() && (currentItem == ICoopItem.EXPLOSIVE || currentItem == ICoopItem.SWORD)) {
                explosive.setActiver(true);
            }

        }

        /**
         * Handles interaction with a Staff object.
         * collect the staff if they share the same element
         *
         * @param staff             (Staff): The staff to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        public void interactWith(Staff staff, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {
                staff.collectBy(ICoopPlayer.this::getItem);
                if (staff.getItem().equals("water")) {
                    inventory.addPocketItem(ICoopItem.WATER_STAFF, 1);
                } else if (staff.getItem().equals("fire")) {
                    inventory.addPocketItem(ICoopItem.FIRE_STAFF, 1);
                }
            }
        }

        /**
         * Handles interaction with an Orb object.
         * collect the orb if they share the same element and publish the dialog
         *
         * @param orb               (Orb): The orb to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (isCellInteraction) {
                if (orb.item().equals("water") || orb.item().equals("fire")) {
                    orb.getGame().publish(orb.getDialog());
                    orb.collectBy(ICoopPlayer.this::getItem);
                    setInvulnerable(true);
                } else if (orb.item().equals("speed")) {
                    orb.getGame().publish(orb.getDialog());
                    orb.disappears();
                    setMoveDuration(3);
                }
            }
        }

        /**
         * Handles interaction with a PressurePlate object.
         * sends an information to the plate to inform of the player's presence
         *
         * @param plate             (PressurePlate): The pressure plate to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(PressurePlate plate, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {
                plate.setOccupier(ICoopPlayer.this);
                plate.setInteractorHere(true);
            }
        }

        /**
         * Handles interaction with a Key object.
         * collect the key of a same element and add it to its inventory
         *
         * @param key               (Key): The key to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            if (wantsCellInteraction() && isCellInteraction) {

                if (key.getItem().equals("water") && ICoopPlayer.this.getItem().equals("water")) {
                    key.collectBy(ICoopPlayer.this::getItem);
                    inventory.addPocketItem(ICoopItem.WATER_KEY, 1);
                } else if (key.getItem().equals("fire") && ICoopPlayer.this.getItem().equals("fire")) {
                    key.collectBy(ICoopPlayer.this::getItem);
                    inventory.addPocketItem(ICoopItem.FIRE_KEY, 1);
                }
            }
        }

        /**
         * Handles interaction with a Portail object.
         *
         * @param portal           (Portail): The portal to interact with.
         * @param isCellInteraction (boolean): True if it is a cell interaction.
         */
        @Override
        public void interactWith(Portal portal, boolean isCellInteraction) {
            if (portal.getSignal().isOn() && isCellInteraction && firsTime) {
                portal.getGame().publish(portal.getVictoryDialog());
                isDoorInContact = true;
                playerPosition = portal.getArrivalCoords();
                areaKey = portal.getTransitoryArea();
                firsTime = false;


            } else if (portal.getSignal().isOff() && isCellInteraction && firsTime) {
                portal.getGame().publish(portal.getRequiredDialog());
                firsTime = false;
            }
            manoirPortailCoord = portal.getCurrentMainCellCoordinates();
        }
    }

}


