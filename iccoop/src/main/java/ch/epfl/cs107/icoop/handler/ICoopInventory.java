package ch.epfl.cs107.icoop.handler;


/**
 * ICoopInventory represents a specialized inventory for the ICoop game.
 *
 * <p>This class extends the `Inventory` class, providing a unique identifier for the inventory
 * system in the ICoop game. It allows entities to manage their items in a structured way.</p>
 */

 import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;

public class ICoopInventory extends Inventory{
    /**
     * Constructor for an ICoopInventory.
     *
     * @param uniquePocket (String): A unique identifier for this inventory pocket.
     */
    public ICoopInventory(String uniquePocket){
        super(uniquePocket);
    }

}
