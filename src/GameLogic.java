import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic implements DrawAndUpdateObject
{
    private World world;

    PowerUpFactory powerUpFactory;

    //PowerUpFactory powerUpFactory = new PowerUpFactory(world);

    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();

    public GameLogic(World world) {
        this.world = world;
        powerUpFactory = new PowerUpFactory(world, LoadMap.getInstance().loadImage("/textures/squareTextures/FirstAid.jpg", new Vec2(0.4f, 0.4f))
                ,LoadMap.getInstance().loadImage("/textures/circleTextures/Coin.png",new Vec2(0.4f, 0.4f)));
    }

    /**
     * The function that updates the object every frame
     */
    int count = 0;
    public void update(){

        count ++;
        if (count % 60 == 0) {
            PowerUps powerUps = powerUpFactory.powerUp(world);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
        }
    }

    /**
     * Nothing needs to be drawn in the 'GameLogic'
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){}


    //TODO: move getid from drawandupdate interface to solidobject when making solidobject an abstract class
    public int getID(){
        return -1;
    }
}

