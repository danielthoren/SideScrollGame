import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;

/**
 * Created by daniel59088 on 4/19/16.
 */
public class Sword extends InventoryItem implements InputListener {

    private KeyCode attack;
    private KeyCode defend;

    public Sword(Player player, int damage, Vec2 relativePos, Image image) {
        super(player, damage, relativePos, image);
    }

    public void inputAction(KeyEvent keyEvent){
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if (keyEvent.getCode() == attack){
                attack();
            }
            else if (keyEvent.getCode() == defend){
                defend();
            }
        }
    }

    private void attack(){
        System.out.println("attack");
    }

    private void defend(){
        System.out.println("defend");
    }
}
