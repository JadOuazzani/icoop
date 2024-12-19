package ch.epfl.cs107.icoop.actor.foe;

import ch.epfl.cs107.icoop.utilities.ICoopBehavior;


/**
 * The Unstoppable interface represents an entity that can move through any cell without restrictions.
 */
public interface Unstoppable {

    /**
     * Determines if the entity can move to a specified cell.
     *
     * @param cell (ICoopBehavior.ICoopCell): The cell to check.
     * @return (boolean): Always true, as Unstoppable entities can move anywhere.
     */
    default boolean canMoveToCell(ICoopBehavior.ICoopCell cell) {
        return true;
    }
}
