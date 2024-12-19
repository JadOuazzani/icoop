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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Mouse;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.utilities.KeyBindings.*;

/**
 * ICoop is the main game class that manages areas, players, and game logic for the ICoop game.
 *
 * <p>This class extends `AreaGame` and implements the `DialogHandler` and `Logic` interfaces.
 * It initializes the game, handles transitions between areas, manages player interactions,
 * and updates the game state during each frame.</p>
 */

 public class ICoop extends AreaGame implements DialogHandler, Logic {
    /** Array containing the names of the areas in the game. */

    private final String[] areas = {"Spawn", "OrbWay", "Maze", "Arena", "SanctumEntrance", "Sanctum", "Starting", "Ending", "Menu"};
    /** Index of the currently active area. */

    private int areaIndex;
    /** Blue player controlled by the user. */

    private ICoopPlayer bluePlayer;
    /** Red player controlled by the user. */

    private ICoopPlayer redPlayer;
    /** The currently active dialogue, if any. */

    private Dialog dialogue;
    /** A flag to ensure the game is initialized only once. */

    private static boolean ITS_INITIALIZED = false;
    /** GUI for displaying the red player's status. */

    private ICoopPlayerStatusGUI redStatusGUI;
    /** GUI for displaying the blue player's status. */

    private ICoopPlayerStatusGUI blueStatusGUI;
    // References to specific areas in the game

    private Spawn spawn;
    private Maze maze;
    private OrbWay orbWay;
    private Arena arena;
    private SanctumEntrance sanctumEntrance;
    private Sanctum sanctum;

    /**
     * Resets the entire game by reinitializing players, dialogues, and areas.
     */
    public void resetGame() {
        ITS_INITIALIZED = false;
        ICoopPlayer.setGlobalCoinCounter(0); // Réinitialise le compteur de pièces
        dialogue = null; // Remet à zéro les dialogues
        begin(getWindow(), getFileSystem());
    }
    /**
     * Resets the current area by clearing dialogues and reinitializing it.
     */
    public void resetArea() {
        dialogue = null; // Remet à zéro les dialogues pour l'aire
        initArea(getCurrentArea().getTitle());
    }

    @Override
    /**
     * Gets the title of the game.
     *
     * <p>This method overrides the `getTitle` method in `AreaGame` to provide a specific title for the ICoop game.</p>
     *
     * @return (String): The title of the game, "ICoop".
     */
    public String getTitle() {
        return "ICoop";
    }

    /**
     * Creates and registers all areas used in the game.
     */

    private void createAreas() {
        spawn = new Spawn(this, this);
        maze = new Maze(this);
        orbWay = new OrbWay(this);
        arena = new Arena();
        sanctumEntrance = new SanctumEntrance(this);
        sanctum = new Sanctum(this);
        Starting starting = new Starting();
        Ending ending = new Ending();
        Menu menu = new Menu();
        addArea(spawn);
        addArea(orbWay);
        addArea(maze);
        addArea(arena);
        addArea(sanctumEntrance);
        addArea(sanctum);
        addArea(starting);
        addArea(ending);
        addArea(menu);
    }

    @Override
    /**
     * Determines if the game logic is "on".
     *
     * <p>This method overrides `isOn` from the `Logic` interface and determines the state
     * based on whether key areas (Arena, OrbWay, Maze) are "on".</p>
     *
     * @return (boolean): True if the game logic is "on", false otherwise.
     */
    public boolean isOn() {
        return arena.isOn() && orbWay.isOn() && maze.isOn();
    }

    @Override
    /**
     * Determines if the game logic is "off".
     *
     * <p>This method overrides `isOff` from the `Logic` interface and determines the state
     * based on whether any key area (Arena, OrbWay, Maze) is "off".</p>
     *
     * @return (boolean): True if the game logic is "off", false otherwise.
     */
    public boolean isOff() {
        return arena.isOff() || orbWay.isOff() || maze.isOff();
    }

    @Override
    /**
     * Initializes the game, creating areas and setting the starting area.
     *
     * <p>This method overrides `begin` in `AreaGame` to initialize the game's areas,
     * set the starting area ("Starting"), and prepare the game state.</p>
     *
     * @param window     (Window): The game window used for rendering.
     * @param fileSystem (FileSystem): The file system used to load resources.
     * @return (boolean): True if the game was successfully initialized, false otherwise.
     */
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
    /**
     * Updates the game state for each frame.
     *
     * <p>This method overrides `update` in `AreaGame` to manage player interactions,
     * dialogues, area transitions, and game-specific logic.</p>
     *
     * @param deltaTime (float): Time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        // Initialize the game only once

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

        boolean isMouseOverStartButton = (mousePos.x >= 8.0f && mousePos.x <= 8.0f + 4.0f) && (mousePos.y >= 1.5f && mousePos.y <= 1.5f + 2.0f);
        boolean isMouseOverRestartButton = (mousePos.x >= 8.0f && mousePos.x <= 8.0f + 4.0f) && (mousePos.y >= 1.0f && mousePos.y <= 1.0f + 1.0f);
        // Area-specific logic
        if (currentAreaTitle.equals("Starting")) {
            if (isMouseOverStartButton && mouse.getLeftButton().isPressed()) {
                initArea(areas[0]);
                super.update(deltaTime);
            }
        } else if (currentAreaTitle.equals("Ending")) {
            if (isMouseOverRestartButton && mouse.getLeftButton().isPressed()) {
                redPlayer.setGlobalCoinCounter(0);
                dialogue = null; // Remettre le dialogue à zéro
                initArea("Starting");
                super.update(deltaTime);
            }
        } else if (!dialogActive(dialogue) || dialogue.isCompleted()) {
            // Vérifier le compteur global de pièces
            if (redPlayer.getGlobalCoinCount() >= 3 && !currentAreaTitle.equals("Ending")) {
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

        // Render GUI if not in Starting or Ending areas
        if (!currentAreaTitle.equals("Starting") && !currentAreaTitle.equals("Ending")) {
            blueStatusGUI.draw(getWindow());
            redStatusGUI.draw(getWindow());
        }




    }

    /**
     * Initializes the specified area and places players at their start positions.
     *
     * @param areaKey (String): The key of the area to initialize.
     */

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

    /**
     * Handles area transitions when players interact with doors.
     */
    private void switchArea() {
        ICoopPlayer contactPlayer = redPlayer.isDoorInContact() ? redPlayer : bluePlayer;
        ICoopPlayer nonContactPlayer = !redPlayer.isDoorInContact() ? redPlayer : bluePlayer;
        if (contactPlayer.isDoorInContact()) {
            contactPlayer.leaveArea();
            nonContactPlayer.leaveArea();
            setCurrentArea(contactPlayer.getAreaKey(), false);
            redPlayer.enterArea(getCurrentArea(), contactPlayer.getPlayerPosition().getFirst());
            bluePlayer.enterArea(getCurrentArea(), contactPlayer.getPlayerPosition().get(1));
        }
    }
    /**
     * Checks if a dialogue is currently active.
     *
     * @param dialogue (Dialog): The dialogue to check.
     * @return (boolean): True if the dialogue is active, false otherwise.
     */
    private boolean dialogActive(Dialog dialogue) {
        return dialogue != null;
    }

    @Override
    /**
     * Publishes a dialogue to be displayed.
     *
     * <p>This method overrides `publish` from `DialogHandler` to update the current active dialogue in the game.</p>
     *
     * @param dialogue (Dialog): The dialogue to be displayed.
     */
    public void publish(Dialog dialogue) {
        this.dialogue = dialogue;
    }}