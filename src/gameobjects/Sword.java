package gameobjects;

import gamelogic.CollisionListener;
import gamelogic.GameComponent;
import gamelogic.LoadMap;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class Sword extends InventoryItemParent implements CollisionListener
{

    protected int damage;
    protected boolean hasDamaged;

    public Sword(final long objectID, final World world, final Vec2 position, final float friction, final Image image,
		 final boolean isSquare, int damage)
    {
	super(objectID, world, position, friction, image, isSquare);
	this.damage = damage;
	hasDamaged = false;
    }


    private void attack(){
        System.out.println("attack");
    }

    private void defend(){
        System.out.println("defend");
    }

    @Override
    public void equip() {
	super.equip();
	LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(this);
    }

    @Override
    public void unEquip() {
	super.unEquip();
	LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
    }

    /**
     * Takes care of collisionencounters with players:
     * - if: This collides with a player, set the 'currentCollidingPlayer' field to said player.
     *       - if: This item is in an inventory and has not damaged a player since this started to collide with a player
     *             (thus preventing the item from draining all of the health of the other player with just one collision)
     *             then damage the player with whom this is colliding and set field 'hasDamaged' to true.
     *
     *OBS!
     * The chain of 'instanceof' is used to check if the object collided with is an instance of player. The 'getUserData' method in
     * body is a container of type 'Object'. This container always contains the class owning the body (in this project) thus
     * this parameter can be used to check wich type of gameobject is collided with and then run methods on said object to get
     * an effect. For example heal the player if we are sure that the object collided with is of the 'Player' class
     * (thus we can safely cast the object contained inside the 'UserData' to 'Player'.
     *
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     */
    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(solidObject) &&
                contact.getFixtureB().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureB().getBody().getUserData();
            if (player != null && !hasDamaged && !((Player) contact.getFixtureB().getBody().getUserData()).equals(player)) {
                currentCollidingPlayer.damage(damage);
                hasDamaged = true;
            }
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(solidObject) &&
                contact.getFixtureA().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureA().getBody().getUserData();
            if (player != null && !hasDamaged && !((Player) contact.getFixtureA().getBody().getUserData()).equals(player)) {
                currentCollidingPlayer.damage(damage);
                hasDamaged = true;
            }
        }
    }

    /**
     * Takes car of collisionencounters with players:
     * - if: This stops to collide with a player then set the field 'hasDamaged' to 'false' and 'currentCollidingPlayer' to 'null'.
     *
     * OBS!
     * The chain of 'instanceof' is used to check if the object collided with is an instance of player. The 'getUserData' method in
     * body is a container of type 'Object'. This container always contains the class owning the body (in this project) thus
     * this parameter can be used to check wich type of gameobject is collided with and then run methods on said object to get
     * an effect. For example heal the player if we are sure that the object collided with is of the 'Player' class
     * (thus we can safely cast the object contained inside the 'UserData' to 'Player'.
     * Also the branches are not identical because they evaluate different fixtures.
     *
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     */
    public void endContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(solidObject) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            hasDamaged = false;
            currentCollidingPlayer = null;
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(solidObject) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            hasDamaged = false;
            currentCollidingPlayer = null;
        }
    }
}
