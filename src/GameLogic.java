import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class GameLogic
{

    private static Vec2 gravity;
    private static World world;

    public GameLogic(float gravity) {
        this.gravity = new Vec2(0.f, gravity);
        world = new World(this.gravity);

    }
}
