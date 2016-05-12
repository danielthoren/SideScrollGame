package gameobjects;

import gamelogic.CollisionListener;
import gamelogic.GameComponent;
import gamelogic.LoadMap;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * This class creates a sword which contains damage given to the player struck by the sword.
 */
public class Sword extends InventoryItemParent implements CollisionListener
{

    private int damage; //The damage of the sword
    private boolean hasDamaged;
    private long animationTimer;
    private boolean swingAnimation;
    private boolean stabAnimation;
    private boolean defendAnimation;
    private final Vec2 origRelativePos;

    /**
     * Creates a sword.
     * @param objectID The id of the sword
     * @param world The world where the sword is created
     * @param position  The position of the sword
     * @param friction  The friction of the sword
     * @param image The image of the sword
     * @param damage    The damage of the sword.
     */
    public Sword(final long objectID, final World world, final Vec2 position, final float friction, final Image image, int damage)
    {
	super(objectID, new DynamicSquare(objectID, world, position, friction, image));
	this.damage = damage;
	hasDamaged = false;
        relativeAngle = 0;
        relativePos = new Vec2(getSize().x, -getSize().y/2);
        origRelativePos = relativePos;
        animationTimer = 0;
        swingAnimation = false;
        stabAnimation = false;
        defendAnimation = false;
    }

    /**
     * Picks up the sword.
     */
    @Override
    public void equip() {
	super.equip();
        getBody().setFixedRotation(true);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(this);
    }

    /**
     * Drops the sword.
     */
    @Override
    public void unEquip() {
	super.unEquip();
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeCollisionListener(this);
    }

    @Override
    public void pickUp(Player player){
        super.pickUp(player);
        getBody().getFixtureList().setSensor(false);
        getBody().setTransform(getBody().getPosition(), relativeAngle);
        getBody().setFixedRotation(true);
    }

    @Override
    public void update(){
        super.update();
        if (swingAnimation){swingAnimation();}
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
        beginContactCheck(contact.getFixtureA(), contact.getFixtureB());
        beginContactCheck(contact.getFixtureB(), contact.getFixtureA());
    }

    /**
     * Helps the 'beginContact' function to check for certain collisions. for more detailed comments on what theese two functions
     * do read the comments on said function.
     * @param fixtureA One of the fixtures used in the check.
     * @param fixtureB One of the fixtures used in the check.
     */
    private void beginContactCheck(Fixture fixtureA, Fixture fixtureB){
        if (stabAnimation || swingAnimation) {
            if (fixtureA.getBody().getUserData().equals((circle == null) ? square : circle) &&
                fixtureB.getBody().getUserData() instanceof Player) {
                currentCollidingPlayer = (Player) fixtureB.getBody().getUserData();
                if (player != null && !hasDamaged && !fixtureB.getBody().getUserData().equals(player)) {
                    currentCollidingPlayer.damage(damage);
                    hasDamaged = true;
                }
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
        endContactCheck(contact.getFixtureA(), contact.getFixtureB());
        endContactCheck(contact.getFixtureB(), contact.getFixtureA());
    }

    /**
     * Checks if this stops colliding with a player, then set the field 'HasDamaged' to 'false'.
     * For more detailed comments se the function 'endContact' that this function is called from.
     * @param fixtureA The first fixture to be used in check.
     * @param fixtureB The second fixture to be used in check.
     */
    private void endContactCheck(Fixture fixtureA, Fixture fixtureB) {
        if (swingAnimation || stabAnimation) {
            if (fixtureA.getBody().getUserData().equals((circle == null) ? square : circle) &&
                fixtureB.getBody().getUserData() instanceof Player) {
                hasDamaged = false;
                currentCollidingPlayer = null;
            }
        }
    }

    private void swingAnimation(){
        Vec2 relativeDistance = new Vec2(player.getBody().getPosition().x - getBody().getPosition().x, player.getBody().getPosition().y - getBody().getPosition().y);
        double radius = Math.sqrt((double) (Math.pow(relativeDistance.x, 2) + Math.pow(relativeDistance.y, 2)));

        double newAngle = getBody().getAngle() + getBody().getAngle() * 1.1;
        Vec2 newRelativePos = new Vec2((float) (radius * Math.sin(newAngle)), (float) (radius * Math.cos(newAngle)));
        relativePos = newRelativePos;

        animationTimer++;
    }

    public void setDefendAnimation(final boolean defendAnimation) {this.defendAnimation = defendAnimation;}

    public void setStabAnimation(final boolean stabAnimation) {this.stabAnimation = stabAnimation;}

    public void setSwingAnimation(final boolean swingAnimation) {this.swingAnimation = swingAnimation;}
}
