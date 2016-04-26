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
     * @param density   The density of the coin
     * @param restitution   The restitution of the coin
     * @param image     The image of the coin
     * @param ID    The id of the coin
     * @param jumpHandler   The jumphandler coinatined and given to the player when collided with
     */
    public PowerUpCoin(int ID, World world, Vec2 pos, float friction, float density,
                       float restitution, Image image, JumpHandler jumpHandler) {
        super(ID, world, pos, friction, image);
        body.getFixtureList().setSensor(false); //Makes the coin a sensor
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
    }

    /**
     * Creates the coin.
     * @param world The world where it will be created
     * @param pos   The position of the coin
     * @param friction  The friction of the coin
     * @param density   The density of the coin
     * @param restitution   The restitution of the coin
     * @param ID    The id of the coin
     * @param jumpHandler   The jumphandler coinatined and given to the player when collided with
     */
    public PowerUpCoin(int ID,World world, Vec2 pos, float friction, float density,
                       float restitution, Color color, double radious, JumpHandler jumpHandler) {
        super(ID, world, pos, friction, color, radious);
        body.getFixtureList().setSensor(false); //Makes the coin a sensor
        body.setUserData(this);
        this.jumpHandler = jumpHandler;
    }
    /**
     * This method checks if the player have collided whit the coin, if it has the player is given the jumphandler held
     * by the coin and the coin is then removed from the world.
     */
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            ((Player) contact.getFixtureB().getBody().getUserData()).setCurrentJumpHandler(jumpHandler);
            ((Player) contact.getFixtureA().getBody().getUserData()).setStartTime(true);
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
