package ch.epfl.cs107.icoop.utilities;


import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.utilities.KeyBindings.*;


public class ICoop extends AreaGame implements DialogHandler, Logic {
    private final String[] areas = {"Spawn", "OrbWay", "Maze", "Area"};
    private int areaIndex;
    private ICoopPlayer bluePlayer;
    private ICoopPlayer redPlayer;
    private Dialog dialogue;
    private static boolean ITS_INITIALIZED = false;
    ICoopPlayerStatusGUI redStatusGUI;
    ICoopPlayerStatusGUI blueStatusGUI;
    private ICoopArea spawn;
    private ICoopArea maze;
    private ICoopArea orbWay;
    private ICoopArea arena;


    public void resetGame() {
        ITS_INITIALIZED = false;
        begin(getWindow(), getFileSystem());

    }

    public void resetArea() {
        initArea(getCurrentArea().getTitle());
    }


    @Override
    public String getTitle() {
        return "ICoop";
    }

    private void createAreas() {
        spawn = new Spawn(this, this);
        maze = new Maze(this);
        orbWay = new OrbWay(this);
        arena = new Arena();
        addArea(spawn);
        addArea(orbWay);
        addArea(maze);
        addArea(arena);
    }

    @Override
    public boolean isOn() {
        return arena.isOn() && orbWay.isOn() && maze.isOn();
    }

    @Override
    public boolean isOff() {
        return arena.isOff() || orbWay.isOff() || maze.isOff();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    @Override

    public void update(float deltaTime) {
        if (!ITS_INITIALIZED) {
            super.update(deltaTime);
            ITS_INITIALIZED = true;
            bluePlayer.resetMotion();
            redPlayer.resetMotion();
        }
        if (!dialogActif(dialogue) || dialogue.isCompleted()) {
            switchArea();
            bluePlayer.centerCamera(redPlayer, bluePlayer);
            super.update(deltaTime);
            ((ICoopArea) getCurrentArea()).setCameraScaleFactor(redPlayer.getPosition(), bluePlayer.getPosition());

            if (getWindow().getKeyboard().get(RESET_GAME).isPressed()) {
                resetGame();
            }

            if (getWindow().getKeyboard().get(RESET_AREA).isPressed()) {
                resetArea();
            }
            if (bluePlayer.getHealth().isOff() || redPlayer.getHealth().isOff()) {
                resetArea();
            }
        } else {
            dialogue.draw(getWindow());
            if ((getWindow().getKeyboard().get(KeyBindings.NEXT_DIALOG).isPressed())) {
                dialogue.update(deltaTime);
            }
        }
        blueStatusGUI.draw(getWindow());
        redStatusGUI.draw(getWindow());
    }


    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords1 = area.getStartPositions().getFirst();
        DiscreteCoordinates coords2 = area.getStartPositions().getLast();
        redPlayer = new ICoopPlayer(getCurrentArea(), Orientation.DOWN, coords1, "Icoop/player", "fire", RED_PLAYER_KEY_BINDINGS);
        redStatusGUI = new ICoopPlayerStatusGUI(redPlayer, true);
        redPlayer.enterArea(getCurrentArea(), coords1);
        bluePlayer = new ICoopPlayer(getCurrentArea(), Orientation.DOWN, coords2, "Icoop/player2", "water", BLUE_PLAYER_KEY_BINDINGS);
        blueStatusGUI = new ICoopPlayerStatusGUI(bluePlayer, false);
        bluePlayer.enterArea(getCurrentArea(), coords2);
        bluePlayer.centerCamera(redPlayer, bluePlayer);

    }


    private void switchArea() {
        ICoopPlayer contactPlayer = redPlayer.isContactPorte() ? redPlayer : bluePlayer;
        ICoopPlayer nonContactPlayer = !redPlayer.isContactPorte() ? redPlayer : bluePlayer;
        if (contactPlayer.isContactPorte()) {
            contactPlayer.leaveArea();
            nonContactPlayer.leaveArea();
            setCurrentArea(contactPlayer.getAreaKey(), false);
            redPlayer.enterArea(getCurrentArea(), contactPlayer.getJoueurPosition().getFirst());
            bluePlayer.enterArea(getCurrentArea(), contactPlayer.getJoueurPosition().get(1));
        }
    }

    private boolean dialogActif(Dialog dialogue) {
        return dialogue != null;
    }


    @Override
    public void publish(Dialog dialogue) {
        this.dialogue = dialogue;
    }

}
