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
public class SquareInventoryItem extends DynamicSquare implements InventoryItem
{

    protected World world;
    protected final int ID;
    protected Player player;
    protected Vec2 relativePos;
    protected final Vec2 size;
    protected boolean equipped;

    public SquareInventoryItem(int ID, World world, Player player, float friction, Image image) {
        super(ID, world, player.body.getPosition(), friction, image);
        this.world = world;
        this.ID = ID;
        equipped = true;
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
        relativePos = new Vec2(player.getPosition().x + player.getSize().x, player.getPosition().y - size.y/2);
    }

    public SquareInventoryItem(int ID,World world, Vec2 position, float friction, Image image) {
        super(ID, world, position, friction, image);
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
        super.drawSquare(gc, body.getPosition(), (double) size.x, (double) size.y);
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
        equipped = false;
    }

    public void pickUp(Player player){
        this.player = player;
    }

    public void drop(){
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
