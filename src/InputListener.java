import javafx.scene.input.KeyEvent;

public interface InputListener extends DrawAndUpdateObject
{
    public void inputAction(KeyEvent event);
}
