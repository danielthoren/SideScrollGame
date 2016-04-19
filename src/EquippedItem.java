import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import javax.swing.*;

/**
 * Parent Class containing shared code between equipped objects
 */
public class EquippedItem extends SolidObject implements EquipItem, DrawAndUpdateObject {

    protected final int damage;
    protected Vec2 relativePos;
    protected Player player;
    protected final float height;
    protected final float width;

    public EquippedItem(Player player, int damage, Vec2 relativePos, Image image) {
        super(player.getPosition(), 0f, image);
        this.damage = damage;
        this.player = player;
        this.relativePos = relativePos;
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
