import javafx.scene.input.KeyEvent;


public interface InputListener
{
    public void inputAction(KeyEvent event);

    public int getID();
}
