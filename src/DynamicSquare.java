import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel on 2016-03-19.
 */
public class DynamicSquare extends Square {

    private float density;
    private float restitution;
    public DynamicSquare(World world, Vec2 pos, float friction, float density, float restitution, Image image) {
        super(world, pos, friction, image);
        this.density = density;
        this.restitution = restitution;
        makeDynamic();
    }

    public DynamicSquare(World world, Vec2 pos, float friction, float density, float restitution, Color color, double width, double height) {
        super(world, pos, friction, color, width, height);
        this.restitution = restitution;
        this.density = density;
        makeDynamic();
    }

    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
        this.body.getFixtureList().setRestitution(restitution);
        this.body.getFixtureList().setDensity(density);
    }
}
