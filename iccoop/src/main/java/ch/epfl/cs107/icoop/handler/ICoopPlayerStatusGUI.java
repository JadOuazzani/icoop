package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

/**
 * A GUI that shows information about the player on the screen.
 */
public class ICoopPlayerStatusGUI implements Graphics {
    /** Rendering depth for the GUI elements. */

    private final static int DEPTH = 2000;
    /** The player whose status is being displayed. */

    private final ICoopPlayer player;
    /** Indicates whether the GUI should be flipped for screen layout. */

    private final boolean flipped;
    /** The name of the sprite representing the player's currently selected item. */

    private String sprite_name;

    /**
     * Constructor for the ICoopPlayerStatusGUI.
     *
     * @param player  (ICoopPlayer): The player whose status will be displayed.
     * @param flipped (boolean): True if the GUI should be flipped, false otherwise.
     */
    public ICoopPlayerStatusGUI(ICoopPlayer player, boolean flipped) {
        this.player = player;
        this.flipped = flipped;
        this.sprite_name = player.getCurrentItem().getSpriteName();
    }

    @Override
    /**
     * Draws the player's status on the canvas, including selected gear and total coins.
     *
     * @param canvas (Canvas): The canvas on which to draw the GUI.
     */
    public void draw(Canvas canvas) {
        // Compute width, height and anchor
        float width = canvas.getTransform().getX().getX();
        float height = canvas.getTransform().getY().getY();

        float ratio = canvas.getWidth() / (float) canvas.getHeight();
        if (ratio > 1)
            height = width / ratio;
        else
            width = height * ratio;

        sprite_name = player.getCurrentItem().getSpriteName();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(flipped ? (-width / 2 + 2) : width / 2, height / 2));

        //Draw selected gear
        ImageGraphics gearDisplay = new ImageGraphics(ResourcePath.getSprite("icoop/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, DEPTH);
        gearDisplay.draw(canvas);
        ImageGraphics currentItem = new ImageGraphics(ResourcePath.getSprite(sprite_name), 0.5f, 0.5f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(0.5f, height - 1.25f)), 1, DEPTH);
        currentItem.draw(canvas);
        // Draw total coin count

        TextGraphics coinText = new TextGraphics("    "+Integer.toString(player.getGlobalCoinCount()), 0.5f, Color.YELLOW);
        coinText.setFontName("Dragonfly");
        coinText.setFontSize(1.1f);
        coinText.setAnchor(anchor.add(new Vector(1.5f, height - 2.0f)));
        coinText.draw(canvas);



    }
}
