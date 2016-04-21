import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel59088 on 4/19/16.
 */
public class Sword extends InventoryItem implements InputListener {

    private KeyCode attack;
    private KeyCode defend;

    public Sword(World world, int ID, Player player, int damage, Image image) {
        super(world, ID, player, damage, image);
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

    @Override
    protected void createBody(){

    }

    private void attack(){
        System.out.println("attack");
    }

    private void defend(){
        System.out.println("defend");
    }
}
