package gamelogic;

import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import characterspesific.JumpHandler;
import characterspesific.PowerUps;

import java.util.ArrayList;
import java.util.List;

public class GameLogic implements DrawAndUpdateObject
{
    private World world;
    private int count = 0;
    private int fiveSeconds = 300;

    PowerUpFactory powerUpFactory;

    //gamelogic.PowerUpFactory powerUpFactory = new gamelogic.PowerUpFactory(world);

    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();

    public GameLogic(World world) {
        this.world = world;
        powerUpFactory = new PowerUpFactory(world, LoadMap
                .getInstance().loadImage("/textures/squareTextures/FirstAid.jpg", new Vec2(0.4f, 0.4f))
                , LoadMap.getInstance().loadImage("/textures/circleTextures/Coin.png",new Vec2(0.4f, 0.4f)));
    }

    /**
     * The function that updates the object every frame
     */

    public void update(){

        count ++;
        if (count % fiveSeconds == 0) {
            PowerUps powerUps = powerUpFactory.powerUp(world);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
        }
    }

    /**
     * Nothing needs to be drawn in the 'gamelogic.GameLogic'
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){}


    //TODO: move getid from drawandupdate interface to solidobject when making solidobject an abstract class
    public int getID(){
        return -1;
    }
}

