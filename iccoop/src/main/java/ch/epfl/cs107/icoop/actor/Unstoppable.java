package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.utilities.ICoopBehavior;

public interface Unstoppable {
    default boolean canMoveToCell(ICoopBehavior.ICoopCell cell) {
        return true;
    }
}
