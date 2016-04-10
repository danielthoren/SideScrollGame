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

    public int getPoints() {
        return points;
    }

    public int getID() {
        return ID;
    }
    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            System.out.println("Ja");
            ((Player) contact.getFixtureB().getBody().getUserData()).addScore(points);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeSolidObject(this);
        }
        if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            System.out.println("Ja");
            ((Player) contact.getFixtureA().getBody().getUserData()).addScore(points);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeSolidObject(this);
        }
    }
    public void endContact(Contact contact){

    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coin && ((Coin) obj).getID() == this.getID()){return true;}
        else {return false;}
    }

}
