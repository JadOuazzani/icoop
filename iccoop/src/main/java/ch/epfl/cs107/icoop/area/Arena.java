package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.object.Coin;
import ch.epfl.cs107.icoop.actor.object.Door;
import ch.epfl.cs107.icoop.actor.object.Key;
import ch.epfl.cs107.icoop.actor.object.Teleportation;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class Arena extends ICoopArea {
    private Key redKey;
    private Key blueKey;
    private boolean isRegistered;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.isOn()&& !isRegistered){
            Coin coin = new Coin(this,Orientation.DOWN,new DiscreteCoordinates(7, 9));
            registerActor(coin);
            coin.update(deltaTime);
            isRegistered=true;

        }
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        redKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 16), "fire");
        registerActor(redKey);
        blueKey = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 4), "water");
        registerActor(blueKey);

        Door teleporeur = new Teleportation("Spawn", new Logic() {
            @Override
            public boolean isOn() {
                return (redKey.isOn() && blueKey.isOn());
            }

            @Override
            public boolean isOff() {
                return (redKey.isOff() || blueKey.isOff());
            }
        }, List.of(new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6)), this, new DiscreteCoordinates(10, 10), Orientation.DOWN);
        registerActor(teleporeur);
    }

    @Override
    public boolean isOn() {
        if (redKey != null && blueKey != null) {
            return (redKey.isOn() && blueKey.isOn());
        } else {
            return false;
        }
    }

    @Override
    public boolean isOff() {
        if (redKey != null && blueKey != null) {
            return (redKey.isOff() || blueKey.isOff());
        } else {
            return true;
        }
    }


    @Override
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14, 15));
    }

    @Override
    public String getTitle() {
        return "Arena";
    }

}
