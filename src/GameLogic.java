import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic implements CollisionListener, DrawAndUpdateObject
{
    private World world;

    PowerUpFactory powerUpFactory = new PowerUpFactory(world);

    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();

    public GameLogic(World world) {
        this.world = world;
    }


    /**
     * The function that updates the object every frame
     */
    int count = 0;
    public void update(){

        count ++;
        if (count % 60 == 0) {
            PowerUps powerUps = powerUpFactory.powerUp(world);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
        }
    }

    /**
     * Nothing needs to be drawn in the 'GameLogic'
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){}

    /**
     * Handles event that needs to happen when contact between objects begins. Do note that this function
     * is called every time any contact occurs, thus each object implementing this interface must check if they
     * are part of the contact or not.
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     *                such as point of contact and so on.
     */
    public void beginContact(Contact contact){
        if (contact.getFixtureB().getBody().getUserData() instanceof Player){
            playerCollisionCheck(contact.getFixtureB(), contact.getFixtureA());
        }
        else if (contact.getFixtureA().getBody().getUserData() instanceof Player){
            playerCollisionCheck(contact.getFixtureA(), contact.getFixtureB());
        }
    }

    /**
     * Checks all of the different senarios that can appear when the player collides with something then takes action
     * accordingly.
     * @param playerFixture The Fixture of the player
     * @param otherFixture The Fixture of the other body colliding with the player
     */
    private void playerCollisionCheck(Fixture playerFixture, Fixture otherFixture){
        //Checks if the player wants to pick upp item ('E' is pressed). If so and other item colliding with the player
        //is an 'SquareInventoryItem' then said 'SquareInventoryItem's 'equip' function is called.
        if (((Player) playerFixture.getBody().getUserData()).isPickUpItem() && otherFixture.getBody().getUserData() instanceof SquareInventoryItem){
            ((SquareInventoryItem) otherFixture.getBody().getUserData()).pickUp((Player) playerFixture.getBody().getUserData());
        }

}

    /**
     * Handles event that needs to happen when contact between objects ends. Do note that this function
     * is called every time any contact occurs, thus each object implementing this interface must check if they
     * are part of the contact or not.
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     *                such as point of contact and so on.
     */
    public void endContact(Contact contact){

    }
}

