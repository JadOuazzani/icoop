package ch.epfl.cs107.icoop.utilities;

import ch.epfl.cs107.icoop.actor.object.Coin;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.icoop.handler.MusicPlayer;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Mouse;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.utilities.KeyBindings.*;

public class ICoop extends AreaGame implements DialogHandler, Logic {
    private final String[] areas = {"Spawn", "OrbWay", "Maze", "Arena", "SanctumEntrance", "Sanctum", "Accueil", "Ending"};
    private int areaIndex;
    private ICoopPlayer bluePlayer;
    private ICoopPlayer redPlayer;
    private Dialog dialogue;
    private static boolean ITS_INITIALIZED = false;
    private ICoopPlayerStatusGUI redStatusGUI;
    private ICoopPlayerStatusGUI blueStatusGUI;
    private Spawn spawn;
    private Maze maze;
    private OrbWay orbWay;
    private Arena arena;
    private SanctumEntrance sanctumEntrance;
    private Sanctum sanctum;

    public void resetGame() {
        ITS_INITIALIZED = false;
        Coin.setGlobalCoinCounter(0); // Réinitialise le compteur de pièces
        dialogue = null; // Remet à zéro les dialogues
        begin(getWindow(), getFileSystem());
    }

    public void resetArea() {
        dialogue = null; // Remet à zéro les dialogues pour l'aire
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
        sanctumEntrance = new SanctumEntrance(this);
        sanctum = new Sanctum();
        Accueil accueil = new Accueil();
        Ending ending = new Ending();
        addArea(spawn);
        addArea(orbWay);
        addArea(maze);
        addArea(arena);
        addArea(sanctumEntrance);
        addArea(sanctum);
        addArea(accueil);
        addArea(ending);
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
            areaIndex = 5; // Index pour "Accueil"
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

        Keyboard keyboard = getWindow().getKeyboard();
        String currentAreaTitle = getCurrentArea().getTitle();
        Mouse mouse = getWindow().getMouse();

        // Récupération de la position de la souris dans l'aire courante
        Vector mousePos = getCurrentArea().getRelativeMousePosition();
        // mousePos.x et mousePos.y correspondent aux coordonnées dans l'aire, assurez-vous que c'est cohérent
        // avec l'emplacement où vous dessinez votre bouton.

        boolean isMouseOverStartButton = (mousePos.x >= 8.0f && mousePos.x <= 8.0f + 3.0f) && (mousePos.y >= 2.0f && mousePos.y <= 2.0f + 1.0f);
        boolean isMouseOverRestartButton = (mousePos.x >= 7.0f && mousePos.x <= 7.0f + 4.0f) && (mousePos.y >= 1.0f && mousePos.y <= 1.0f + 1.0f);

        if (currentAreaTitle.equals("Accueil")) {
            if (isMouseOverStartButton && mouse.getLeftButton().isPressed()) {
                initArea(areas[0]);
                super.update(deltaTime);
            }
        } else if (currentAreaTitle.equals("Ending")) {
            if (isMouseOverRestartButton && mouse.getLeftButton().isPressed()) {
                Coin.setGlobalCoinCounter(0);
                dialogue = null; // Remettre le dialogue à zéro
                initArea("Accueil");
                super.update(deltaTime);
            }
        } else if (!dialogActif(dialogue) || dialogue.isCompleted()) {
            // Vérifier le compteur global de pièces
            if (Coin.getGlobalCoinCount() >= 3 && !currentAreaTitle.equals("Ending")) {
                initArea("Ending");
                super.update(deltaTime);
            } else {
                switchArea();
                bluePlayer.centerCamera(redPlayer, bluePlayer);
                super.update(deltaTime);
                ((ICoopArea) getCurrentArea()).setCameraScaleFactor(redPlayer.getPosition(), bluePlayer.getPosition());

                if (keyboard.get(RESET_GAME).isPressed()) {
                    resetGame();
                }

                if (keyboard.get(RESET_AREA).isPressed()) {
                    resetArea();
                }

                if (bluePlayer.getHealth().isOff() || redPlayer.getHealth().isOff()) {
                    resetArea();
                }
            }
        } else {
            dialogue.draw(getWindow());
            if (keyboard.get(NEXT_DIALOG).isPressed()) {
                dialogue.update(deltaTime);
            }
        }

        // N'affiche l'inventaire que si on n'est ni dans Accueil, ni dans Ending
        if (!currentAreaTitle.equals("Accueil") && !currentAreaTitle.equals("Ending")) {
            blueStatusGUI.draw(getWindow());
            redStatusGUI.draw(getWindow());
        }


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