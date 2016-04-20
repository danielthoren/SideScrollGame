import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;

/**
 * Parent Class containing shared code between equipped objects
 */
public class InventoryItem extends SolidObject implements DrawAndUpdateObject {

    protected Player player;
    protected Vec2 relativePos;
    protected final int damage;
    protected final float height;
    protected final float width;

    public InventoryItem(Player player, int damage, Vec2 relativePos, Image image) {
        super(player.getPosition(), 0f, image);
        this.damage = damage;
        this.relativePos = relativePos;
        height = GameComponent.pixToMeters((float) image.getHeight());
        width = GameComponent.pixToMeters((float) image.getWidth());
    }

    public InventoryItem(Vec2 position, int damage, Vec2 relativePos, Image image) {
        super(position, 0f, image);
        this.damage = damage;
        this.relativePos = relativePos;
        player = null;
        height = GameComponent.pixToMeters((float) image.getHeight());
        width = GameComponent.pixToMeters((float) image.getWidth());
    }

    public void update(){
        if (player != null) {
            pos = new Vec2(player.getPosition().x + relativePos.x, player.getPosition().y + relativePos.y);
        }
    }

    public void draw(GraphicsContext gc){
        super.drawSquare(gc, pos, (double) width, (double) height);
    }


    public void equip(Player player){
        
    }

    public void unEquip(){
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeBody(body);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).removeDrawAndUpdateObject(this);
    }
}
