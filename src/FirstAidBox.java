import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created by kristiansikiric on 2016-04-09.
 */

/**
 * This class creates a box that can be picked up by the player. The box contains health
 * given to the player when picked up.
 */
public class FirstAidBox extends Square implements CollisionListener {
    private int health;
    private int ID;
    private World world;

    /**
     * Creates a box that will symbolize a first aid kit.
     * @param world The world where the coin will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param image     The iamge of the coin
     * @param ID    The ID of the coin
     */
    public FirstAidBox(World world, Vec2 pos, float friction, Image image, int ID) {
        super(world, pos, friction, image);
        health = 100;
        body.getFixtureList().setSensor(true); //Makes the coin a sensor
        body.setUserData(this);
    }

    /**
     * Creates a box that will symbolize a first aid kit
     * @param world The world where the box will be created
     * @param pos   The posistion of the box
     * @param friction  The friction of the box
     * @param color The color of the box
     * @param width The width of the box
     * @param height    The height if the box
     * @param ID    The id of the box
     */
    public FirstAidBox(World world, Vec2 pos, float friction, Color color, double width, double height, int ID) {
        super(world, pos, friction, color, width, height);
        health = 100;
        body.getFixtureList().setSensor(true); //Makes the coin a sensor
        body.setUserData(this);
    }

    /**
     * This method checks if the player have collided whit the coin, if it has the player is given the points held
     * by the coin and the coin is then removed from the world.
     */
    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).heal(health);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureA().getBody().getUserData()).heal(health);
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
    public int getHealth() {return health;}

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
        if (obj instanceof FirstAidBox && ((FirstAidBox) obj).getID() == this.getID()){return true;}
        else {return false;}
    }



}
