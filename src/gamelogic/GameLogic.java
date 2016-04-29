package gamelogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import characterspesific.JumpHandler;
import characterspesific.PowerUps;

import java.util.ArrayList;
import java.util.List;

/**
 * Class does all the logic in the game that depends on
 */
public class GameLogic implements DrawAndUpdateObject
{
    private PowerUpFactory powerUpFactory;
    private long timeSinceLastPowerUp;
    private final long iD;

    //Default values, can be changed with setters
    //Todo Move default size and time values to a more appropriate position
    private static final Vec2 HEAL_BOX_SIZE = new Vec2(0.4f, 0.4f);
    private static final Vec2 COIN_SIZE = new Vec2(0.2f, 0.2f);
    private static final long timeBetweenPowerups = 100;

    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();

    public GameLogic(long iD, World world) {
        this.iD = iD;
        timeSinceLastPowerUp = System.currentTimeMillis();

        Image boxTexture = LoadMap.getInstance().loadImage("/textures/squareTextures/FirstAid.jpg", HEAL_BOX_SIZE);
        Image coinTexture = LoadMap.getInstance().loadImage("/textures/circleTextures/Coin.png", COIN_SIZE);

        powerUpFactory = new PowerUpFactory(boxTexture, coinTexture);
        //powerUpFactory = new PowerUpFactory(0.2f, new Vec2(0.2f, 0.2f), Color.WHITE);
    }

    public void update(){

        if (System.currentTimeMillis() - timeSinceLastPowerUp > timeBetweenPowerups) {
            PowerUps powerUps = powerUpFactory.powerUp(LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld());
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
            timeSinceLastPowerUp = System.currentTimeMillis();
        }
    }

    /**
     * Nothing needs to be drawn in the 'gamelogic.GameLogic'
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){}

    public long getId(){
        return iD;
    }
}

