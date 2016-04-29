package gameobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import characterspesific.JumpHandler;
import characterspesific.PowerUps;
import gamelogic.GameComponent;
import gamelogic.LoadMap;
import gamelogic.Map;

/**
 *This class creates a coin that contains a jump powerup.
 */
public class PowerUpCoin extends DynamicCircle implements PowerUps
{
    private JumpHandler jumpHandler;

    /**
     * Creates the coin.
     * @param world The world where it will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param image     The image of the coin
     * @param ID    The id of the coin
     * @param jumpHandler   The jumphandler coinatined and given to the player when collided with
     */
    public PowerUpCoin(int ID, World world, Vec2 pos, float friction,
                       Image image, JumpHandler jumpHandler) {
        super(ID, world, pos, friction, image);
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
    }

    /**
     * Creates the coin.
     * @param world The world where it will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param ID    The id of the coin
     * @param jumpHandler   The jumphandler coinatined and given to the player when collided with
     */
    public PowerUpCoin(int ID,World world, Vec2 pos, float friction,
                       Color color, double radious, JumpHandler jumpHandler) {
        super(ID, world, pos, friction, color, radious);
        body.getFixtureList().setSensor(false); //Makes the coin a sensor
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
        setRestitution(1f);
    }
    /**
     * This method checks if the player have collided whit the coin, if it has the player is given the jumphandler held
     * by the coin and the coin is then removed from the world.
     *
     * OBS!
     * The chain of 'instanceof' is used to check if the object collided with is an instance of player. The 'getUserData' method in
     * body is a container of type 'Object'. This container always contains the class owning the body (in this project) thus
     * this parameter can be used to check wich type of gameobject is collided with and then run methods on said object to get
     * an effect. For example heal the player if we are sure that the object collided with is of the 'Player' class
     * (thus we can safely cast the object contained inside the 'UserData' to 'Player'.
     */
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).setCurrentJumpHandler(jumpHandler);
            ((Player) contact.getFixtureB().getBody().getUserData()).setStartTime(true);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureA().getBody().getUserData()).setCurrentJumpHandler(jumpHandler);
            ((Player) contact.getFixtureA().getBody().getUserData()).setStartTime(true);
            Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
            map.removeBody(body);
            map.removeCollisionListener(this);
            map.removeDrawAndUpdateObject(this);
        }
    }

    /**
     * Not needed in this class.
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     */
    public void endContact(Contact contact) {
    }

}
