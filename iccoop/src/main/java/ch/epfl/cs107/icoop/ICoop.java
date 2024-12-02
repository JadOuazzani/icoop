package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.PauseMenu;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.KeyBindings.*;


public class ICoop extends AreaGame {
    int test;
    private final String[] areas = {"Spawn", "OrbWay"};
    private int areaIndex;
    private ICoopPlayer bluePlayer;
    private ICoopPlayer redPlayer;
    private Dialog dialogue;
    private boolean paused, requestPause;
    private PauseMenu menu;



    public void resetGame() {
        begin(getWindow(), getFileSystem());
    }

    public void resetArea() {
        if (getCurrentArea().getTitle().equals(areas[0])) {
            initArea(areas[0]);
        } else if (getCurrentArea().getTitle().equals(areas[1])) {
            initArea(areas[1]);
        }
    }


    @Override
    public String getTitle() {
        return "ICoop";
    }

    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
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
        setPauseMenu(menu);
        requestPause();
        super.update(deltaTime);
            if (getWindow().getKeyboard().get(KeyBindings.NEXT_DIALOG).isPressed()) {
                dialogue.update(deltaTime);
            }
        }
    }

    @Override
    public void end() {
    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords1 = area.getStartPositions().getFirst();
        DiscreteCoordinates coords2 = area.getStartPositions().getLast();
        redPlayer = new ICoopPlayer(getCurrentArea(), Orientation.DOWN, coords1, "Icoop/player", "fire", RED_PLAYER_KEY_BINDINGS);
        redPlayer.enterArea(getCurrentArea(), coords1);
        bluePlayer = new ICoopPlayer(getCurrentArea(), Orientation.DOWN, coords2, "Icoop/player2", "water", BLUE_PLAYER_KEY_BINDINGS);
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

    private void setActiveDialog(String dialogue) {
        this.dialogue = new Dialog(dialogue);
    }


}
