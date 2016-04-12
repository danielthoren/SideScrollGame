import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by kristiansikiric on 2016-04-09.
 */
public class Coin extends Circle implements CollisionListener {
    private int points;
    private int ID;
    private World world;

    public Coin(World world, Vec2 pos, float friction, Image image, int ID) {
        super(world, pos, friction, image);
        points = 100;
        body.getFixtureList().setSensor(true);
    }

    public Coin(World world, Vec2 pos, float friction, Color color, double radious, int ID) {
        super(world, pos, friction, color, radious);
        points = 100;
        body.getFixtureList().setSensor(true);
        body.setUserData(this);
    }

    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            System.out.println("Ja");
            ((Player) contact.getFixtureB().getBody().getUserData()).addScore(points);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            System.out.println("Ja");
            ((Player) contact.getFixtureA().getBody().getUserData()).addScore(points);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
    }
    public void endContact(Contact contact){}

    public int getPoints() {return points;}

    public int getID() {return ID;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coin && ((Coin) obj).getID() == this.getID()){return true;}
        else {return false;}
    }



}
