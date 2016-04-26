import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Parent Class containing shared code between equipped objects
 */
public class SquareInventoryItem extends DynamicSquare implements InventoryItem, CollisionListener
{
    protected World world;
    protected final int ID;
    protected Player player;
    protected Player currentCollidingPlayer;
    protected Vec2 relativePos;
    protected final Vec2 size;
    protected boolean equipped;
    protected boolean hasDamaged;

    public SquareInventoryItem(int ID, World world, Player player, float friction, Image image) {
        super(ID, world, player.body.getPosition(), friction, image);
        super.setDensity(1f);
        this.world = world;
        this.ID = ID;
        currentCollidingPlayer = null;
        hasDamaged = false;
        equipped = true;
        setGroupIndex(-player.getID());
        body.setType(BodyType.DYNAMIC);
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
        calcRelativePos(player);
    }

    public SquareInventoryItem(int ID, World world, Vec2 position, float friction, Image image) {
        super(ID, world, position, friction, image);
        super.setDensity(1f);
        this.ID = ID;
        this.world = world;
        hasDamaged = false;
        currentCollidingPlayer = null;
        player = null;
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
    }

    private void setGroupIndex(int index){
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()){
            Filter filter = fixture.getFilterData();
            filter.groupIndex = index;
            fixture.setFilterData(filter);
        }
    }

    private void calcRelativePos(Player player){
        relativePos = new Vec2(player.getSize().x, -size.y/2);
    }

    public void update(){
        if (currentCollidingPlayer != null && currentCollidingPlayer.isPickUpItem() && player == null){
            System.out.println("in update");
            currentCollidingPlayer.getInventory().addItem(this);
        }
        else if (player != null) {
            Vec2 newPos = new Vec2(0,0);
            float newAngle = body.getAngle();
            if (player.getDirection() == Direction.LEFT){
                newPos = new Vec2(player.getPosition().x - relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = Math.abs(body.getAngle());
            }
            else if (player.getDirection() == Direction.RIGHT){
                newPos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
                newAngle = -Math.abs(body.getAngle());
            }
            body.setTransform(newPos, newAngle);
        }
    }

    public void draw(GraphicsContext gc){
        super.drawSquare(gc, body.getPosition(), (double) size.x, (double) size.y);
    }

    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) &&
                contact.getFixtureB().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureB().getBody().getUserData();
            if (player != null && !hasDamaged && !((Player) contact.getFixtureB().getBody().getUserData()).equals(player)) {
                currentCollidingPlayer.damage(15);
                hasDamaged = true;
            }
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) &&
                contact.getFixtureA().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureA().getBody().getUserData();
            if (player != null && !hasDamaged && !((Player) contact.getFixtureA().getBody().getUserData()).equals(player)) {
                currentCollidingPlayer.damage(15);
                hasDamaged = true;
            }
        }
    }

    /**
     * Checks if the player wants to pick this item up. If so then adds this to the players inventory
     */
    private void pickUpCheck(Player player) {
        //Checks if the player wants to pick upp item ('E' is pressed). If so then this item is added to the players inventory
        if (player.isPickUpItem())
            if (player.getInventory().addItem(this)){
                pickUp(player);
                System.out.println("In pickupCheck");
        }
    }

    public void endContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            hasDamaged = false;
            currentCollidingPlayer = null;
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            hasDamaged = false;
            currentCollidingPlayer = null;
        }

    }

    /**
     * Equips the item to the current player.
     */
    public void equip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(this);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(this);
        super.createBody(world);
        System.out.println("in equip");
        equipped = true;
    }

    /**
     * Unequips the item fron the player, destroying the items body and removing it from the 'DrawAndUpdate' listener aswell as
     * from the 'CollisionListener' listener.
     */
    public void unEquip(){
        System.out.println("in unequip");
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(body);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
        body = null;
        equipped = false;
    }

    public void pickUp(Player player){
        System.out.println("in pickup");
        body.setType(BodyType.DYNAMIC);
        body.getFixtureList().setSensor(false);
        body.setTransform(body.getPosition(), (float) -(Math.PI / 2 + Math.PI / 4));
        body.setFixedRotation(true);
        setGroupIndex(-player.getID());
        calcRelativePos(player);
        this.player = player;
    }

    public void drop(){
        System.out.println("in drop");
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(false);
        player = null;
    }

    public int getID() {return ID;}

    @Override
    public boolean equals(Object object){
        if (object instanceof SquareInventoryItem){
            if (this.getID() == ((SquareInventoryItem) object).getID()){
                return true;
            }
        }
        return false;
    }
}
