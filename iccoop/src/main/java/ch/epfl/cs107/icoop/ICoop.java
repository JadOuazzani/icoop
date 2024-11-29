package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.OrbWay;
import ch.epfl.cs107.icoop.area.Spawn;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import static ch.epfl.cs107.icoop.KeyBindings.RED_PLAYER_KEY_BINDINGS;


public class ICoop extends AreaGame {
    private final String[] areas = {"Spawn", "OrbWay"};
    private int areaIndex;
    private ICoopPlayer bluePlayer;
    private ICoopPlayer redPlayer;



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
        super.update(deltaTime);
    }

    @Override
    public void end() {

    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getStartPositions().getFirst();
        redPlayer = new ICoopPlayer(getCurrentArea(), Orientation.DOWN, coords, "Icoop/player", "fire", RED_PLAYER_KEY_BINDINGS);
        redPlayer.enterArea(getCurrentArea(), coords);
    }


}
