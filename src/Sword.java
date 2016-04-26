import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel59088 on 4/19/16.
 */
public class Sword extends SquareInventoryItem {

    public Sword(int ID, World world, Vec2 pos, float friction, int damage, Image image) {
        super(ID, world, pos, friction, image, true);
    }

    private void attack(){
        System.out.println("attack");
    }

    private void defend(){
        System.out.println("defend");
    }
}
