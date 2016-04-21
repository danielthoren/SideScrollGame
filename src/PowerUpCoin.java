import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by kristiansikiric on 2016-04-21.
 */
public class PowerUpCoin extends DynamicCircle implements CollisionListener{
    private JumpHandler jumpHandler;
    private int ID;

    public PowerUpCoin(World world, Vec2 pos, float friction, float density,
                       float restitution, Image image, int ID, JumpHandler jumpHandler) {
        super(world, pos, friction, density, restitution, image);
        body.getFixtureList().setSensor(false); //Makes the coin a sensor
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
    }

    public PowerUpCoin(World world, Vec2 pos, float friction, float density,
                       float restitution, Color color, double radious, int ID, JumpHandler jumpHandler) {
        super(world, pos, friction, density, restitution, color, radious);
        body.getFixtureList().setSensor(false); //Makes the coin a sensor
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
    }

    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).setCurrentJumpHandler(jumpHandler);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureA().getBody().getUserData()).setCurrentJumpHandler(jumpHandler);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
    }

    public void endContact(Contact contact) {

    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
