package gamelogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import characterspesific.JumpHandler;
import characterspesific.PowerUps;

import java.util.ArrayList;
import java.util.List;

public class GameLogic implements DrawAndUpdateObject
{
    private World world;
    private PowerUpFactory powerUpFactory;
    private long timeSinceLastPowerup;
    private final int iD;

    //Default values, can be changed with setters
    //Todo Move default size and time values to a more appropriate position
    private static final Vec2 HEAL_BOX_SIZE = new Vec2(0.4f, 0.4f);
    private static final Vec2 COIN_SIZE = new Vec2(0.2f, 0.2f);
    private static final long timeBetweenPowerups = 100;

    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();

    public GameLogic(int iD, World world) {
        this.world = world;
        this.iD = iD;
        timeSinceLastPowerup = System.currentTimeMillis();

        Image boxTexture = LoadMap.getInstance().loadImage("/textures/squareTextures/FirstAid.jpg", HEAL_BOX_SIZE);
        Image coinTexture = LoadMap.getInstance().loadImage("/textures/circleTextures/Coin.png", COIN_SIZE);

        powerUpFactory = new PowerUpFactory(boxTexture, coinTexture);
        //powerUpFactory = new PowerUpFactory(0.2f, new Vec2(0.2f, 0.2f), Color.WHITE);
    }

    public void update(){

        if (System.currentTimeMillis() - timeSinceLastPowerup > timeBetweenPowerups) {
            PowerUps powerUps = powerUpFactory.powerUp(LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld());
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
            timeSinceLastPowerup = System.currentTimeMillis();
        }
    }

    /**
     * Nothing needs to be drawn in the 'gamelogic.GameLogic'
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){}

    public int getID(){
        return iD;
    }
}

