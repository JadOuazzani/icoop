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
import ch.epfl.cs107.icoop.actor.object.Rochers;
import ch.epfl.cs107.icoop.actor.perso.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;

public class ICoopBehavior extends AreaBehavior {

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


        ICoopCellType(int type, boolean canWalk, boolean canFly) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

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

    public void createYourActors(ICoopArea area) {
        if (area.getTitle().equals("Arena")) {
            for (int i = 0; i < area.getHeight(); i++) {
                for (int j = 0; j < area.getWidth(); j++) {
                    ICoopCell cell = (ICoopCell) (getCell(i, j));
                    if (cell.type == ICoopCellType.ROCK) {
                        Rochers rock = new Rochers(area, Orientation.DOWN , new DiscreteCoordinates(i, j));
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
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
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
