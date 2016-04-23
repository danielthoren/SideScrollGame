import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
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

    public SquareInventoryItem(int ID, World world, Player player, float friction, Image image) {
        super(ID, world, player.body.getPosition(), friction, image);
        this.world = world;
        this.ID = ID;
        currentCollidingPlayer = null;
        equipped = true;
        setGroupIndex(-player.getID());
        setSensor(false);
        body.setType(BodyType.KINEMATIC);
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
        calcRelativePos(player);
    }

    public SquareInventoryItem(int ID,World world, Vec2 position, float friction, Image image) {
        super(ID, world, position, friction, image);
        this.ID = ID;
        this.world = world;
        currentCollidingPlayer = null;
        player = null;
        setSensor(true);
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
    }

    private void setSensor(boolean isSensor){
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()){
            fixture.setSensor(isSensor);
        }
    }

    private void setGroupIndex(int index){
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()){
            fixture.getFilterData().groupIndex = index;
        }
    }

    private void calcRelativePos(Player player){
        relativePos = new Vec2(player.getSize().x, -size.y/2);
    }

    public void update(){
        if (currentCollidingPlayer != null && currentCollidingPlayer.isPickUpItem()){
            if (currentCollidingPlayer.getInventory().addItem(this)){
                pickUp(currentCollidingPlayer);
            }
        }
        if (player != null) {
            Vec2 newPos = new Vec2(0,0);
            if (player.getDirection() == Direction.LEFT){
                newPos = new Vec2(player.getPosition().x - relativePos.x, player.getPosition().y + relativePos.y);
            }
            else if (player.getDirection() == Direction.RIGHT){
                newPos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
            }
            body.getPosition().x = newPos.x;
            body.getPosition().y = newPos.y;

            /*
            System.out.print(body.getPosition());
            System.out.print("    playerPosition:    ");
            System.out.println(player.body.getPosition());
            */
        }
    }

    public void draw(GraphicsContext gc){
        super.drawSquare(gc, body.getPosition(), (double) size.x, (double) size.y);
    }

    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureB().getBody().getUserData();
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = (Player) contact.getFixtureA().getBody().getUserData();
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
        }
    }

    public void endContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = null;
        }
        else if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureA().getBody().getUserData() instanceof Player){
            currentCollidingPlayer = null;
        }

    }

    /**
     * Equips the item to the current player.
     */
    public void equip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(this);
        super.createBody(world);
        equipped = true;
    }

    /**
     * Unequips the item fron the player, destroying the items body and removing it from the 'DrawAndUpdate' listener aswell as
     * from the 'CollisionListener' listener.
     */
    public void unEquip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(body);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
        body = null;
        equipped = false;
    }

    public void pickUp(Player player){
        setGroupIndex(-player.getID());
        setSensor(false);
        body.setType(BodyType.KINEMATIC);
        calcRelativePos(player);
        this.player = player;
    }

    public void drop(){
        setSensor(true);
        setGroupIndex(0);
        body.setType(BodyType.DYNAMIC);
        player = null;
        body.setType(BodyType.DYNAMIC);
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
