import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jdk.internal.util.xml.impl.Input;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Parent Class containing shared code between equipped objects
 */
public class InventoryItem extends SolidObject implements DrawAndUpdateObject
{

    protected World world;
    protected final int ID;
    protected Player player;
    protected Vec2 relativePos;
    protected final Vec2 size;
    protected final int damage;
    protected boolean equipped;

    public InventoryItem(World world, int ID, Player player, int damage, Image image) {
        super(player.getPosition(), 0f, image);
        this.damage = damage;
        this.world = world;
        this.ID = ID;
        equipped = true;
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
        relativePos = new Vec2(player.getPosition().x + player.getSize().x, player.getPosition().y - size.y/2);
    }

    public InventoryItem(int ID, Vec2 position, int damage, Image image) {
        super(position, 0f, image);
        this.damage = damage;
        this.ID = ID;
        this.world = world;
        player = null;
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
    }

    public void update(){
        if (player != null) {
            pos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
        }
    }

    public void draw(GraphicsContext gc){
        super.drawSquare(gc, pos, (double) size.x, (double) size.y);
    }

    /**
     * Equips the item to the current player.
     * @param player
     */
    public void equip(Player player){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(this);
        createBody();
    }

    /**
     * Unequips the item fron the player, destroying the items body and removing it from the 'DrawAndUpdate' listener aswell as
     * from the 'CollisionListener' listener.
     */
    public void unEquip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(body);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
    }

    public void drop(){
        player = null;
        body.setType(BodyType.DYNAMIC);
    }

    /**
     * Should create the body of the object. The childClass should override this function
     */
    protected void createBody(){}
}
