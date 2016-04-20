import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Parent Class containing shared code between equipped objects
 */
public class InventoryItem extends SolidObject implements DrawAndUpdateObject, CollisionListener {

    protected final int ID;
    protected Player player;
    protected Vec2 relativePos;
    protected final Vec2 size;
    protected final int damage;

    public InventoryItem(int ID, Player player, int damage, Image image) {
        super(player.getPosition(), 0f, image);
        this.damage = damage;
        this.ID = ID;
        size = new Vec2(GameComponent.pixToMeters((float) image.getWidth()), GameComponent.pixToMeters((float) image.getHeight()));
        relativePos = new Vec2(player.getPosition().x + player.getSize().x, player.getPosition().y - size.y/2);
    }

    public InventoryItem(int ID, Vec2 position, int damage, Image image) {
        super(position, 0f, image);
        this.damage = damage;
        this.ID = ID;
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


    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureB().getBody().getUserData() instanceof Player){
            boolean added = ((Player) contact.getFixtureB().getBody().getUserData()).getInventory().addItem((InventoryItem) contact.getFixtureA().getBody().getUserData());
            if (added){
                body.setType(BodyType.KINEMATIC);
            }
        }
    }


    public void endContact(Contact contact){

    }



    public void equip(Player player){
        
    }

    public void drop(){
        player = null;
        body.setType(BodyType.DYNAMIC);
    }

    private void createBody(Player player){

    }

    public void unEquip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(body);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
    }
}
