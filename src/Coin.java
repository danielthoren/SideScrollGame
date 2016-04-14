import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by kristiansikiric on 2016-04-09.
 */

/**
 * This class creates a coin that can be picked up by the player. The coin contains points
 * given to the player when picked up.
 */
public class Coin extends Circle implements CollisionListener {
    private int points;
    private int ID;
    private World world;

    /**
     * Creates a circle that will symbolize the coin.
     * @param world The world where the coin will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param image     The iamge of the coin
     * @param ID    The ID of the coin
     */
    public Coin(World world, Vec2 pos, float friction, Image image, int ID) {
        super(world, pos, friction, image);
        points = 100;
        body.getFixtureList().setSensor(true); //Makes the coin a sensor
        body.setUserData(this);
    }

    /**
     * Creates a circle that will symbolize the coin
     * @param world The world where the coin will be created
     * @param pos   The posoistion of the coin
     * @param friction  The friction of the coin
     * @param color     The color of the coin
     * @param radious   The radious of the coin
     * @param ID    The ID of the coin
     */
    public Coin(World world, Vec2 pos, float friction, Color color, double radious, int ID) {
        super(world, pos, friction, color, radious);
        points = 100;
        body.getFixtureList().setSensor(true); //Makes the coin a sensor
        body.setUserData(this);
    }

    /**
     * This method checks if the player have collided whit the coin, if it has the player is given the points held
     * by the coin and the coin is then removed from the world.
     */
    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).addScore(points);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureA().getBody().getUserData()).addScore(points);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
    }

    /**
     * Not needed in this case.
     * @param contact
     */
    public void endContact(Contact contact){}

    /**
     * @return the points held by the coin
     */
    public int getPoints() {return points;}

    /**
     * @return the ID of the coin
     */
    public int getID() {return ID;}

    /**
     * Overrides the method 'equals.()'
     * @param obj The object that should be evaluated
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coin && ((Coin) obj).getID() == this.getID()){return true;}
        else {return false;}
    }



}
