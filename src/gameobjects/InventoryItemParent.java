package gameobjects;

import characterspesific.InventoryItem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import gamelogic.Direction;
import gamelogic.DrawAndUpdateObject;
import gamelogic.GameComponent;
import gamelogic.LoadMap;

/**
 * Parent Class containing shared code between equipped objects
 */
public class InventoryItemParent implements InventoryItem
{
    protected World world;
    protected Player player;
    protected Player currentCollidingPlayer;
    protected DynamicCircle dynamicCircle;
    protected DynamicSquare dynamicSquare;
    protected SolidObject solidObject;
    protected DrawAndUpdateObject drawAndUpdateObject;
    protected Vec2 relativePos;
    protected float relativeAngle;
    protected final Vec2 size;
    protected boolean equipped;

    /**
     * Initializes a 'gameobjects.InventoryItemParent and puts it at the specified position in the specified world.
     * @param ID The id of the object.
     * @param world The game world to add the object to.
     * @param position The startposition of the object.
     * @param friction The friction of the object.
     * @param image The image of the object.
     */
    public InventoryItemParent(int ID, World world, Vec2 position, float friction, Image image, boolean isSquare) {
        this.world = world;
        if (isSquare){
            dynamicSquare = new DynamicSquare(ID, world, position, friction, image);
            solidObject = dynamicSquare;
            drawAndUpdateObject = dynamicSquare;
            dynamicCircle = null;
        }
        else{
            dynamicCircle = new DynamicCircle(ID, world, position, friction, image);
            solidObject = dynamicCircle;
            drawAndUpdateObject = dynamicCircle;
            dynamicSquare = null;
        }

        currentCollidingPlayer = null;
        relativePos = null;
        player = null;
        //Setting the density of the object to 1. This is the default value of an equipped item and can be changed by setter in superclass.
        solidObject.setDensity(1f);
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
    }

    /**
     * Setting the group index of all of the bodies fixtures. This is used to control collision between objects.
     * The primary usage in this specific class is to prevent the item:s body from colliding with the player whom currently
     * equipps said item.
     * @param index The index to set the group to. If two fixtures has the same negative value they will collide with everything
     *              except for each other. If the value is posetive they will collide with nothing bot each other.
     */
    private void setGroupIndex(int index){
        for (Fixture fixture = solidObject.getBody().getFixtureList(); fixture != null; fixture = fixture.getNext()){
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
        relativePos = new Vec2(player.getSize().x, -size.y/2);
        relativeAngle = (float) -(Math.PI / 2 + Math.PI / 4);
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
        drawAndUpdateObject.update();
        if (currentCollidingPlayer != null && currentCollidingPlayer.isPickUpItem() && player == null){
            currentCollidingPlayer.getInventory().addItem(this);
        }
        else if (player != null) {
            Vec2 newPos = new Vec2(0,0);
            float newAngle = solidObject.getBody().getAngle();
            if (player.getDirection() == Direction.LEFT){
                newPos = new Vec2(player.getPosition().x - relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = Math.abs(solidObject.getBody().getAngle());
            }
            else if (player.getDirection() == Direction.RIGHT){
                newPos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = -Math.abs(solidObject.getBody().getAngle());
            }
            solidObject.getBody().setTransform(newPos, newAngle);
            solidObject.getBody().setFixedRotation(true);
        }
    }

    /**
     * Draws the item using the parentclass drawfunction
     * @param gc The GraphicsContext with wich to draw
     */
    @Override
    public void draw(GraphicsContext gc){
        solidObject.drawSquare(gc, solidObject.body.getPosition(), (double) size.x, (double) size.y);
    }

    /**
     * Equips the item to the player owning the inventory that this is stored in.
     */
    public void equip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(this);
        if (dynamicSquare == null){
            dynamicCircle.createBody(world);
        }
        equipped = true;
    }

    /**
     * Unequips the item from the player, destroying the items body and removing it from the 'DrawAndUpdate' listener aswell as
     * from the 'gamelogic.CollisionListener' listener.
     * OBSERVE!: If a child of this class implements other listeners, that child must override this
     * function and remove itselfe from the other listenerlists aswell.
     */
    public void unEquip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(solidObject.body);
        solidObject.body = null;
        equipped = false;
    }

    /**
     * Adds this to a players inventory and changes the position and rotation of this to current relative position/angle.
     * @param player The player to add this to.
     */
    public void pickUp(Player player){
        solidObject.body.getFixtureList().setSensor(false);
        solidObject.body.setFixedRotation(true);
        solidObject.body.setTransform(solidObject.body.getPosition(), relativeAngle);
        setGroupIndex(-player.getId());
        calcRelativePos(player);
        this.player = player;
    }

    /**
     * Drops this item from the player.
     */
    public void drop(){
        solidObject.body.setFixedRotation(false);
        setGroupIndex(0);
        player = null;
    }


    /**
     * Returns the id of the 'gameobjects.SolidObject' of the items body
     * @return int id
     */
    public int getId(){
        return solidObject.getId();
    }

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