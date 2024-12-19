package ch.epfl.cs107.icoop.area;


import ch.epfl.cs107.icoop.actor.object.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

/**
 * The OrbWay class represents a game area with elemental puzzles involving orbs and walls.
 */
public class OrbWay extends ICoopArea {
    private DialogHandler game;// Handler for managing in-game dialogs
    private Orb fireOrb1;// Fire orb in the area
    private Orb waterOrb1;// Water orb in the area
    private boolean isRegistered;// Tracks if coins are already registered

    /**
     * Constructor for the OrbWay class.
     *
     * @param game (DialogHandler): Handler for in-game dialogs.
     */
    public OrbWay(DialogHandler game) {
        this.game = game;
        isRegistered = false;
    }

    /**
     * Updates the area, adding a coin once both orbs are active.
     *
     * @param deltaTime (float): Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.isOn() && !isRegistered) {
            Coin coin = new Coin(this, Orientation.DOWN, new DiscreteCoordinates(6, 8));
            registerActor(coin);
            coin.update(deltaTime);
            isRegistered = true;

        }
    }

    /**
     * Creates the area by registering its elements, including orbs, walls, and pressure plates.
     */
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        Door door1 = new Door("Spawn", Logic.TRUE, List.of(
                new DiscreteCoordinates(18, 16),
                new DiscreteCoordinates(18, 15)),
                this,
                new DiscreteCoordinates(0, 14),
                Orientation.DOWN,
                new DiscreteCoordinates(0, 13), new DiscreteCoordinates(0, 12), new DiscreteCoordinates(0, 11), new DiscreteCoordinates(0, 10));
        registerActor(door1);
        Door door2 = new Door("Spawn", Logic.TRUE, List.of(
                new DiscreteCoordinates(18, 16),
                new DiscreteCoordinates(18, 15)),
                this,
                new DiscreteCoordinates(0, 8),
                Orientation.DOWN,
                new DiscreteCoordinates(0, 7), new DiscreteCoordinates(0, 6), new DiscreteCoordinates(0, 5), new DiscreteCoordinates(0, 4));
        registerActor(door2);
        fireOrb1 = new Orb(this, Orientation.UP, new DiscreteCoordinates(17, 12), Orb.OrbType.FIRE, game);
        registerActor(fireOrb1);
        waterOrb1 = new Orb(this, Orientation.UP, new DiscreteCoordinates(17, 6), Orb.OrbType.WATER, game);
        registerActor(waterOrb1);

        Heart heart1 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 4));
        registerActor(heart1);
        Heart heart2 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10, 6));
        registerActor(heart2);
        Heart heart3 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5, 13));
        registerActor(heart3);
        Heart heart4 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10, 11));
        registerActor(heart4);

        PressurePlate firePressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 7), Orientation.DOWN);
        registerActor(firePressurePlate);
        PressurePlate waterPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 10), Orientation.DOWN);
        registerActor(waterPressurePlate);

        for (int i = 0; i <= 4; i++) {
            ElementalWall fireWall = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10 + i), new Logic() {
                @Override
                public boolean isOn() {
                    return firePressurePlate.isOff();
                }

                @Override
                public boolean isOff() {
                    return firePressurePlate.isOn();
                }
            }, ElementalWall.WallType.FEU);
            registerActor(fireWall);

            ElementalWall waterWall = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4 + i), new Logic() {
                @Override
                public boolean isOn() {
                    return waterPressurePlate.isOff();
                }

                @Override
                public boolean isOff() {
                    return waterPressurePlate.isOn();
                }
            }, ElementalWall.WallType.EAU);
            registerActor(waterWall);

        }


    }

    /**
     * Retrieves the title of the area.
     *
     * @return (String): The title of the area.
     */
    @Override
    public String getTitle() {
        return "OrbWay";
    }

    /**
     * Retrieves the starting positions in the area.
     *
     * @return (List < DiscreteCoordinates >): A list of starting coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5));
    }


    /**
     * Checks if the area is "on," based on the state of the orbs.
     *
     * @return (boolean): True if both orbs are active.
     */
    @Override
    public boolean isOn() {
        return (waterOrb1.isOn() && fireOrb1.isOn());
    }

    /**
     * Checks if the area is "off." This area is never "off."
     *
     * @return (boolean): Always false.
     */
    @Override
    public boolean isOff() {
        return false;
    }
}
