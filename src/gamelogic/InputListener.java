package gamelogic;

import javafx.scene.input.KeyEvent;

/**
 * This interface listens to key events.
 */
public interface InputListener extends GameObject
{
    void inputAction(KeyEvent event);
}
