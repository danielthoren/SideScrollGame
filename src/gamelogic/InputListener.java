package gamelogic;

import javafx.scene.input.KeyEvent;

public interface InputListener extends GameObject
{
    void inputAction(KeyEvent event);
}
