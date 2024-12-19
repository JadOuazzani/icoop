package ch.epfl.cs107.icoop.utilities;


/*
 *	Author:      Jad Ouazzani
 *	Date:
 */


import ch.epfl.cs107.icoop.actor.foe.BombFoe;
import ch.epfl.cs107.icoop.actor.foe.DarkLord;
import ch.epfl.cs107.icoop.actor.foe.Unstoppable;
import ch.epfl.cs107.icoop.actor.object.ElementalWall;
import ch.epfl.cs107.icoop.actor.object.Explosive;
import ch.epfl.cs107.icoop.actor.object.Obstacle;
import ch.epfl.cs107.icoop.actor.object.Rock;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;

/**
 * ICoopBehavior defines the behavior of an area in the ICoop game.
 *
 * <p>This class extends `AreaBehavior` and uses a grid-based system to define the properties
 * and behaviors of cells within the area. It also supports the creation of actors (e.g., rocks, obstacles)
 * based on cell types.</p>
 */

 public class ICoopBehavior extends AreaBehavior {

    /**
     * Constructor for the ICoopBehavior.
     *
     * @param window (Window): The window used to load the area image.
     * @param name   (String): The name of the area behavior to be loaded.
     */

    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }

    /**
     * Enumeration of cell types in the ICoop game.
     *
     * <p>Each cell type defines properties such as whether it can be walked on or flown over.</p>
     */

    public enum ICoopCellType {
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true, true);

        final int type;
        final boolean canWalk;
        final boolean canFly;

        /**
         * Constructor for ICoopCellType.
         *
         * @param type    (int): The RGB value associated with the cell type.
         * @param canWalk (boolean): True if the cell can be walked on, false otherwise.
         * @param canFly  (boolean): True if the cell can be flown over, false otherwise.
         */
        ICoopCellType(int type, boolean canWalk, boolean canFly) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }
        /**
         * Converts an RGB value to the corresponding cell type.
         *
         * @param type (int): The RGB value of the cell.
         * @return (ICoopCellType): The corresponding cell type.
         */
        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            //System.out.println(type);
            return NULL;
        }


    }
    /**
     * Creates actors in the specified area based on cell types.
     *
     * @param area (ICoopArea): The area in which to create actors.
     */
    public void createYourActors(ICoopArea area) {
        if (area.getTitle().equals("Arena")) {
            for (int i = 0; i < area.getHeight(); i++) {
                for (int j = 0; j < area.getWidth(); j++) {
                    ICoopCell cell = (ICoopCell) (getCell(i, j));
                    if (cell.type == ICoopCellType.ROCK) {
                        Rock rock = new Rock(area, Orientation.DOWN , new DiscreteCoordinates(i, j));
                        area.registerActor(rock);
                    }

                    if (cell.type == ICoopCellType.OBSTACLE) {
                        Obstacle obstacle = new Obstacle(area, Orientation.DOWN, new DiscreteCoordinates(i, j));
                        area.registerActor(obstacle);
                    }

                }
            }
        }

    }

    /**
     * ICoopCell represents an individual cell in the ICoop game's grid.
     *
     * <p>Each cell has properties defined by its type and determines the behavior of entities entering or leaving it.</p>
     */

    public class ICoopCell extends AreaBehavior.Cell {
        /// Type of the cell following the enum
        private final ICoopCellType type;


        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        /**
         * Determines whether an entity can leave this cell.
         *
         * @param entity (Interactable): The entity attempting to leave.
         * @return (boolean): Always returns true in this implementation.
         */
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        /**
         * Determines whether an entity can enter this cell.
         *
         * @param entity (Interactable): The entity attempting to enter.
         * @return (boolean): True if the entity is allowed to enter, false otherwise.
         */
        protected boolean canEnter(Interactable entity) {

            if (entity instanceof Unstoppable) {
                return true;
            }
            if (!type.canWalk) {
                return false;
            }

            for (Interactable other : entities) {
                if (entity instanceof Explosive || entity instanceof BombFoe || entity instanceof DarkLord) {
                    return false;
                }
                if (other.takeCellSpace()) {
                    return false;
                }

                if (other instanceof ElementalWall && entity instanceof ICoopPlayer) {
                    String itemPlayer = ((ICoopPlayer) entity).getItem();
                    String itemWall = ((ElementalWall) other).item();
                    return itemWall.equals(itemPlayer) || ((ElementalWall) other).getActif().isOff();
                }
            }

            return true;
        }


        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public boolean takeCellSpace() {
            return super.takeCellSpace();
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }

    }


}
