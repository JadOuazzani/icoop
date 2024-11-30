package ch.epfl.cs107.icoop.area;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */

import ch.epfl.cs107.icoop.ICoopBehavior;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.reflect.Array.getLength;

public abstract class ICoopArea extends Area {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;

    /**
     * Area specific callback to initialise the instance
     */
    protected abstract void createArea();

    /**
     * @return the player's spawn position in the area
     */

    /**
     * Callback to initialise the instance of the area
     *
     * @param window     (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if the area is instantiated correctly, false otherwise
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            setBehavior(new ICoopBehavior(window, getTitle()));
            createArea();
            return true;
        }
        return false;
    }

    private float distance(Vector position_playerA, Vector position_playerB) {
        return position_playerA.sub(position_playerB).getLength();
    }

    /**
     * Getter for Tuto2's scale factor
     *
     * @return Scale factor in both the x-direction and the y-direction
     */


    public void setCameraScaleFactor(Vector position_playerA, Vector position_playerB) {
        this.cameraScaleFactor = (float) max(DEFAULT_SCALE_FACTOR, DEFAULT_SCALE_FACTOR * 0.75 + distance(position_playerA, position_playerB) / 2);
    }

    @Override
    public final float getCameraScaleFactor() {
        return cameraScaleFactor;
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    public abstract List<DiscreteCoordinates> getStartPositions();

}
