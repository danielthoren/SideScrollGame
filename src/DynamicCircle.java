import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.Body;

/**
 * Created by daniel on 2016-03-31.
 */
public class DynamicCircle extends Circle {
    private float density;
    private float restitution;

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of the body
     * @param density The density of the body
     * @param restitution The restitution of the body (the bouncines of the body)
     * @param image The image representing the body in the visual realm
     */
    public DynamicCircle(World world, Vec2 pos, float friction, float density, float restitution, Image image) {
        super(world, pos, friction, image);
        this.density = density;
        this.restitution = restitution;
        makeDynamic();
    }

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of its body
     * @param density The density of its body
     * @param restitution The restitution of its body (the bounciness of the body)
     * @param color The color representing the body in the visual realm
     * @param width The width of the body in meters
     * @param height The height of the body in meters
     */
    public DynamicCircle(World world, Vec2 pos, float friction, float density, float restitution, Color color, double radious) {
        super(world, pos, friction, color, radious);
        this.restitution = restitution;
        this.density = density;
        makeDynamic();
    }

    /**
     * Makes the body the 'super' classes constructor created dynamic and adds the restitution and density to the body.
     */
    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
        this.body.getFixtureList().setRestitution(restitution);
        this.body.getFixtureList().setDensity(density);
        this.body.setFixedRotation(false);
    }
}
