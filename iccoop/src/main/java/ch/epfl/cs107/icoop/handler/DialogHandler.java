package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Dialog;

/**
 * DialogHandler is an interface for managing and displaying dialogues in the game.
 *
 * <p>Classes implementing this interface are responsible for handling the display of
 * `Dialog` objects, allowing game elements to communicate messages to the player.</p>
 */

 public interface DialogHandler {
    /**
     * Publishes a dialogue to be displayed in the game.
     *
     * @param dialogue (Dialog): The dialogue object containing the message to be displayed.
     */
    public void publish(Dialog dialogue);
}
