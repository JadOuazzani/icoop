package ch.epfl.cs107.icoop.actor.object;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Staff extends ElementalItem {


    private final Sprite[] sprites;
    private int imageActuelle;
    private int compteurImage;
    private final static int SEUIL_IMAGE = 4;
    private final static int NOMBRE_IMAGE = 8;

    // Enumération des types de bâtons
    public enum StaffType {
        EAU("water", "staff_water"),
        FEU("fire", "staff_fire");

        private final String spriteName;
        private final String item;

        StaffType(String item, String spriteName) {
            this.spriteName = spriteName;
            this.item = item;
        }

        public String getSpriteName() {
            return spriteName;
        }

        public String getItem() {
            return item;
        }
    }

    public Staff(Area area, Orientation orientation, DiscreteCoordinates position, StaffType staffType) {
        super(area, orientation, position, staffType.getItem());

        sprites = new Sprite[NOMBRE_IMAGE];

        for (int i = 0; i < NOMBRE_IMAGE; i++) {
            sprites[i] = new RPGSprite(
                    staffType.getSpriteName(),
                    2, 2,
                    this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0)
            );
        }

        imageActuelle = 0;
        compteurImage = 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        compteurImage++;
        if (compteurImage >= SEUIL_IMAGE) {
            compteurImage = 0;
            imageActuelle++;
            if (imageActuelle >= NOMBRE_IMAGE) {
                imageActuelle = 0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprites[imageActuelle].draw(canvas);
    }

    @Override
    public String item() {
        return super.getItem();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);

    }
}