package gameobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import characterspesific.PowerUps;
import gamelogic.GameComponent;
import gamelogic.LoadMap;
import gamelogic.Map;

/**
 * This class creates a box that can be picked up by the player. The box contains heal
 * given to the player when picked up.
 */
public class FirstAidBox extends DynamicSquare implements PowerUps
{
    private int heal;

    /**
     * Creates a box that will symbolize a first aid kit
     * @param world The world where the coin will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param image     The iamge of the coin
     * @param objectID    The objectID of the coin
     */
    public FirstAidBox(long objectID, World world, Vec2 pos, float friction, int heal, Image image) {
        super(objectID, world, pos, friction, image);
        this.heal = heal;
        body.setUserData(this);
        setRestitution(1f);
    }

    /**
     * Creates a box that will symbolize a first aid kit
     * @param world The world where the box will be created
     * @param pos   The posistion of the box
     * @param friction  The friction of the box
     * @param color The color of the box
     * @param width The width of the box
     * @param height    The height if the box
     * @param objectID    The id of the box
     */
    public FirstAidBox(long objectID, World world, Vec2 pos, float friction, int heal, Color color, double width, double height) {
        super(objectID, world, pos, friction, color, width, height);
        this.heal = heal;
        body.setUserData(this);
        setRestitution(1f);
    }

    /**
     * This method checks if the player have collided whit the box, if it has, the player is given the heal held
     * by the box and the box is then removed from the world.
     *
     *OBS!
     * The chain of 'instanceof' is used to check if the object collided with is an instance of player. The 'getUserData' method in
     * body is a container of type 'Object'. This container always contains the class owning the body (in this project) thus
     * this parameter can be used to check wich type of gameobject is collided with and then run methods on said object to get
     * an effect. For example heal the player if we are sure that the object collided with is of the 'Player' class
     * (thus we can safely cast the object contained inside the 'UserData' to 'Player'.
     */
    public void beginContact(Contact contact){
        //noinspection ChainOfInstanceofChecks,ChainOfInstanceofChecks
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).heal(heal);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureA().getBody().getUserData()).heal(heal);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
    }

    /**
     * Not needed in this case.
<<<<<<< HEAD
     * @param contact Data container containing information about the contact.
=======
     * @param contact Datacontainer containing information about the contact.
>>>>>>> 0d785578a64f27efe99df6e6fb03d1014469cbfc
     */
    public void endContact(Contact contact){}

    /**
     * @return the healh held by the box
     */
    public int getHeal() {return heal;}
}
