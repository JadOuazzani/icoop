package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.actor.foe.DarkLord;
import ch.epfl.cs107.icoop.actor.object.Coin;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

public class Sanctum extends ICoopArea{
    private boolean isRegistered = false;
    private DarkLord darkLord;




    @Override
    protected void createArea() {
        registerActor(new Background(this));
        DarkLord darkLord = new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(7, 9), List.of("physique"), 10);
        registerActor(darkLord);
    }

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
    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(7, 0), new DiscreteCoordinates(8, 0));
    }

    @Override
    public String getTitle() {
        return "Sanctum";
    }

    @Override
    public boolean isOn() {
        return darkLord.isDead() ;
    }

    @Override
    public boolean isOff() {
        return false;
    }
}
