package ch.epfl.cs107.icoop.actor;

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
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;


public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity {

    private final static int MOVE_DURATION = 8;
    private final TextGraphics message;
    private float hp;
    private String element;
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};
    private OrientedAnimation dessinRouge;
    private OrientedAnimation dessinBleu;
    private static final int ANIMATION_DURATION = 4;
    private static boolean isblue;
    private static String prefixe;




    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates,boolean blue, String element) {
        super(owner, orientation, coordinates);
        this.hp = 10;
        this.element = element;
        message = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        isblue = blue;
        if (blue) {
            prefixe = "icoop/player2";
            dessinBleu = new OrientedAnimation(prefixe, ANIMATION_DURATION , this ,
                    anchor , orders , 4, 1, 2, 16, 32,
                    true);
        }
        else {
            prefixe = "icoop/player";
            dessinRouge = new OrientedAnimation(prefixe , ANIMATION_DURATION , this ,
                    anchor , orders , 4, 1, 2, 16, 32,
                    true);
        }
        resetMotion();

    }


    @Override
    public void update(float deltaTime) {
        if (hp > 0) {
            hp -= deltaTime;
            message.setText(Integer.toString((int) hp));
        }
        if (hp < 0) hp = 0.f;
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(UP, keyboard.get(Keyboard.UP));
        moveIfPressed(RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        super.update(deltaTime);
        if (isDisplacementOccurs()) {
            dessinBleu.update(deltaTime);
            dessinRouge.update(deltaTime);
        }
        else {
            dessinRouge.reset();
            dessinBleu.reset();
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

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
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }


    public boolean isWeak() {
        return (hp <= 0.f);
    }


    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }


    public void strengthen() {
        hp = 10;
    }

    @Override
    public String element() {
        return element;
    }




}

interface ElementalEntity {
    public String element();
}
