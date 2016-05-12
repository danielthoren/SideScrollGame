package gameobjects;

import characterspesific.InventoryItem;
import gamelogic.Map;
import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import gamelogic.Direction;
import gamelogic.GameComponent;
import gamelogic.LoadMap;

/**
 * Parent Class containing shared code between equipped objects
 */
public class InventoryItemParent implements InventoryItem
{
    protected Player player;
    protected Player currentCollidingPlayer;
    protected Circle circle;
    protected Square square;
    protected Vec2 relativePos;
    protected float density;
    protected float friction;
    protected float restitution;
    protected float relativeAngle;

    /**
     * Initializes a 'gameobjects.InventoryItemParent and puts it at the specified position in the specified world.
     * @param objectID The id of the object.
     * @param circle The circlebody of the object.
     */
    public InventoryItemParent(long objectID, Circle circle) {
        this.circle = circle;
        density = circle.getBody().getFixtureList().getDensity();
        friction = circle.getBody().getFixtureList().getFriction();
        restitution = circle.getBody().getFixtureList().getRestitution();
        square = null;
        currentCollidingPlayer = null;
        relativePos = null;
        player = null;
        //Setting the DENSITY of the object to 1. This is the default value of an equipped item and can be changed by setter in superclass.
        circle.setDensity(1f);
        relativeAngle = 0;
    }

    /**
     * Initializes a 'gameobjects.InventoryItemParent and puts it at the specified position in the specified world.
     * @param objectID The id of the object.
     * @param square The squarebody of the object.
     */
    public InventoryItemParent(long objectID, Square square) {
        this.square = square;
        circle = null;
        density = square.getBody().getFixtureList().getDensity();
        friction = square.getBody().getFixtureList().getFriction();
        restitution = square.getBody().getFixtureList().getRestitution();
        currentCollidingPlayer = null;
        relativePos = null;
        player = null;
        relativeAngle = 0;
    }

    /**
     * Initializes a 'gameobjects.InventoryItemParent and puts it at the specified position in the specified world.
     * !OBS This constructor does not set the body of the InventoryObject, thus its childclass must create a body and either
     * set the circle parameter or the square parameter so that it points to that object.
     *
     * @param objectID The id of the object.
     */
    public InventoryItemParent(long objectID) {
        square = null;
        circle = null;
        currentCollidingPlayer = null;
        relativePos = null;
        player = null;
        relativeAngle = 0;
    }


    /**
     * Setting the group index of all of the bodies fixtures. This is used to control collision between objects.
     * The primary usage in this specific class is to prevent the item:s body from colliding with the player whom currently
     * equipps said item.
     * @param index The index to set the group to. If two fixtures has the same negative value they will collide with everything
     *              except for each other. If the value is posetive they will collide with nothing bot each other.
     */
    private void setGroupIndex(int index){
        for (Fixture fixture = getBody().getFixtureList(); fixture != null; fixture = fixture.getNext()){
            Filter filter = fixture.getFilterData();
            filter.groupIndex = index;
            fixture.setFilterData(filter);
        }
    }

    /**
     * Calculates the default relative position of the object. This function should be overridden if the default position
     * does not suit the child-class.
     * @param player The player on wich to calculate the relative position.
     */
    private void calcRelativePos(Player player){
        relativePos = new Vec2(player.getSize().x, -getSize().y / 2);
    }

    /**
     * Updates the item, performing the following checks:
     * - if: The item is not in an inventory and collides with a player and said player wants to pick up this item then call that
     *   players inventory and try to add this item to it.
     * - else if: The item is in a players inventory, then update the position and angle of the item depending on where the
     *   player is and what side the player is facing.
     */
    @Override
    public void update(){
        if (currentCollidingPlayer != null && currentCollidingPlayer.isPickUpItem() && player == null){
            currentCollidingPlayer.getInventory().addItem(this);
        }
        else if (player != null) {
            Vec2 newPos = new Vec2(0,0);
            float newAngle = getBody().getAngle();
            if (player.getDirection() == Direction.LEFT){
                newPos = new Vec2(player.getPosition().x - relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = Math.abs(getBody().getAngle());
            }
            else if (player.getDirection() == Direction.RIGHT){
                newPos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = -Math.abs(getBody().getAngle());
            }
            getBody().setTransform(newPos, newAngle);
            getBody().setFixedRotation(true);
        }
    }

    /**
     * Draws the item using the parentclass drawfunction
     * @param gc The GraphicsContext with wich to draw
     */
    @Override
    public void draw(GraphicsContext gc){
        if (circle == null){
            square.draw(gc);
        }
        else{
            circle.draw(gc);
        }
    }

    /**
     * Equips the item to the player owning the inventory that this is stored in.
     */
    public void equip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawObject(this);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addUpdateObject(this);
        if (circle == null) {
            if (square.getBody() == null) {
                square.createBody(LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld(), density, friction, restitution);
                square.setDensity(density);
            }
        }
        else{
            if (circle.getBody() == null){
                circle.createBody(LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld());
                circle.setDensity(density);
            }
        }
        Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
        map.addDrawObject((square == null) ? circle : square);
        map.addUpdateObject(this);
    }

    /**
     * Unequips the item from the player, destroying the items body and removing it from the 'DrawAndUpdate' listener aswell as
     * from the 'gamelogic.CollisionListener' listener.
     * OBSERVE!: If a child of this class implements other listeners, that child must override this
     * function and remove itselfe from the other listenerlists aswell.
     */
    public void unEquip(){
        Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
        map.removeBody(getBody());
        map.removeDrawObject((square == null) ? circle : square);
        map.removeUpdateObject(this);
    }

    /**
     * Adds this to a players inventory and changes the position and rotation of this to current relative position/angle.
     * @param player The player to add this to.
     */
    public void pickUp(Player player){
        setGroupIndex(-(int)player.getId());
        this.player = player;
        if (circle == null){
            square.setDensity(1f);
        }
        else{
            circle.setDensity(1f);
        }
    }

    /**
     * Drops this item from the player.
     */
    public void drop(){
        getBody().setFixedRotation(false);
        setGroupIndex(0);
        Vec2 gravity = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld().getGravity();
        //Sets the velocity to specific value. If this is not doen the object will move verry fast in the direction of the gravity.
        //The reason behind this is that box2d freezes the velocity on the object when making it KINEMATIC, than that velocity is
        //applied again when the body is set to DYNAMIC again.
        if (player.getDirection() == Direction.LEFT){
            getBody().setLinearVelocity(new Vec2(-1, -1));
        }
        else {
            getBody().setLinearVelocity(new Vec2(1, -1));
        }
        player = null;
    }


    /**
     * This function adds an abstractionlayer to the object since it can either have a
     * Circle or a Square as a bodyobject. If it has a circle then the Vec2 coordinates are the same (the radius).
     * @return the size.
     */
    protected Vec2 getSize(){
        if (circle == null){
            return square.getSize();
        }
        else {
            return new Vec2(circle.getRadius(), circle.getRadius());
        }
    }

    /**
     * Gets the body of thoe object.
     * This function adds an abstractionlayer to the object since it can either have a
     * Circle or a Square as a bodyobject.
     * @return The body of the object.
     */
    protected Body getBody(){
        if (circle == null){
            return square.getBody();
        }
        else {
            return circle.getBody();
        }
    }

    public void setDensity(float density){
        if (circle == null){
            square.setDensity(density);
        }
        else{
            circle.setDensity(density);
        }
        this.density = density;
    }

    /**
     * Returns the id of the 'gameobjects.SolidObject' of the items body
     * @return int id
     */
    public long getId(){
        if (circle == null) {
            return square.getId();
        }
        else{
            return circle.getId();
        }
    }

    public void setRelativeAngle(final float relativeAngle) {this.relativeAngle = relativeAngle;}

    /**
     * equals function looking at the objects id to check if this is the exact same object as the one compared with.
     * @param object The object to compare with.
     * @return true if both objects are identical (has the same id).
     */
    @Override
    public boolean equals(Object object){
        if (object instanceof InventoryItemParent){
            if (this.getId() == ((InventoryItemParent) object).getId()){
                return true;
            }
        }
        return false;
    }
}
