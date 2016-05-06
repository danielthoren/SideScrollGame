package gameobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
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
     * @param objectID    The iD of the coin
     */
    public FirstAidBox(long objectID, World world, Vec2 pos, float friction, int heal, Image image) {
        super(objectID, world, pos, friction, image);
        this.heal = heal;
        body.setUserData(this);
    }

    /**
     * Creates a box that will symbolize a first aid kit
     * @param world The world where the box will be created
     * @param pos   The posistion of the box
     * @param friction  The friction of the box
     * @param color The color of the box
     * @param size The size of the box
     * @param objectID    The id of the box
     */
    public FirstAidBox(long objectID, World world, Vec2 pos, float friction, int heal, Color color, Vec2 size) {
        super(objectID, world, pos, friction, color, size);
        this.heal = heal;
        body.setUserData(this);
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
        beginContactCheck(contact.getFixtureA(), contact.getFixtureB());
        beginContactCheck(contact.getFixtureB(), contact.getFixtureA());
    }

    /**
     * Helps the 'beginContact' function to check collisions and take action appropriatly. For more detailed comments read the
     * comments on said function.
     * @param fixtureA One of the fixtures in the check.
     * @param fixtureB One of the fixtures in the check.
     */
    private void beginContactCheck(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getBody().getUserData().equals(this) && fixtureB.getBody().getUserData() instanceof Player){
            ((Player) fixtureB.getBody().getUserData()).heal(heal);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawObject(this);
        }
    }

    /**
     * Not needed in this case.
     * @param contact Data container containing information about the contact.
     */
    public void endContact(Contact contact){}
}
