import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by kristiansikiric on 2016-04-09.
 */
public class Coin extends Circle {
    private int points;

    public Coin(World world, Vec2 pos, float friction, Image image) {
        super(world, pos, friction, image);
        points = 100;
    }

    public Coin(World world, Vec2 pos, float friction, Color color, double radious) {
        super(world, pos, friction, color, radious);
        points = 100;
    }

    public int getPoints() {
        return points;
    }
}
