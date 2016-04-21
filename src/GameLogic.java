import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class GameLogic implements CollisionListener, DrawAndUpdateObject
{
    private static GameLogic ourInstance = new GameLogic();

    public static GameLogic getInstance() {
	return ourInstance;
    }

    private World world;

    private GameLogic() {
        world = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld();
    }


    /**
     * The function that updates the object every frame
     */
    public void update(){

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
        //is an 'InventoryItem' then said 'InventoryItem's 'equip' function is called.
        if (((Player) playerFixture.getBody().getUserData()).isPickUpItem() && otherFixture.getBody().getUserData() instanceof InventoryItem){
            ((InventoryItem) otherFixture.getBody().getUserData()).equip((Player) playerFixture.getBody().getUserData());
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

    public void setWorld(final World world) {
        this.world = world;
    }
}

