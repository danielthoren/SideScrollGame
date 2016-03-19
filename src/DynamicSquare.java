import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel on 2016-03-19.
 */
public class DynamicSquare extends Square {
    public DynamicSquare(World world, Vec2 pos, int pixPerMeters, Image image) {
        super(world, pos, pixPerMeters, image);
        makeDynamic();
    }

    public DynamicSquare(World world, Vec2 pos, int pixPerMeter, Color color, double width, double height) {
        super(world, pos, pixPerMeter, color, width, height);
        makeDynamic();
    }

    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
    }
}
