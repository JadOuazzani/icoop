package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * A GUI that shows information about the player on the screen.
 */
public class ICoopPlayerStatusGUI implements Graphics {

    private final static int DEPTH = 2000;
    private final ICoopPlayer player;
    private final boolean flipped;
    private String sprite_name;

    public ICoopPlayerStatusGUI(ICoopPlayer player, boolean flipped) {
        this.player = player;
        this.flipped = flipped;
        this.sprite_name = player.getCurrentItem().getSpriteName();
    }

    @Override
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
    }
}
