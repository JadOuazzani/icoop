package ch.epfl.cs107.icoop.actor.object;


/**
 * ElementalEntity is an interface that represents an entity with a specific elemental item.
 *
 * <p>This interface defines a single method that retrieves the elemental item's name or type
 * associated with the implementing entity.</p>
 */

public interface ElementalEntity {
    /**
     * Retrieves the name or type of the elemental item associated with this entity.
     *
     * @return (String): The elemental item's name or type.
     */
    public String item();
}
