import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel59088 on 4/19/16.
 */
public class Sword extends SquareInventoryItem implements InputListener {

    private KeyCode attack;
    private KeyCode defend;

    public Sword(int ID, World world, Player player, float friction,  int damage, Image image) {
        super(ID, world, player, friction, image);
    }

    public Sword(int ID, World world, Vec2 pos, float friction, int damage, Image image) {
        super(ID, world, pos, friction, image);
    }

    public void inputAction(KeyEvent keyEvent){
        if (equipped) {
            if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                if (keyEvent.getCode() == attack) {
                    attack();
                } else if (keyEvent.getCode() == defend) {
                    defend();
                }
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
