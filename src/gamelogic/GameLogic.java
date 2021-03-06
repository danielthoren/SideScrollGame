package gamelogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import characterspesific.PowerUps;

/**
 * Class does all the logic in the game that depends on
 */
public class GameLogic implements Update
{
    private PowerUpFactory powerUpFactory;
    private long timeSinceLastPowerUp;
    private final long id;

    //Default values, can be changed with setters
    //Todo Move default imSize and time values to a more appropriate position
    private static final Vec2 HEAL_BOX_SIZE = new Vec2(0.4f, 0.4f);
    private static final Vec2 COIN_SIZE = new Vec2(0.2f, 0.2f);
    private static final long TIME_BETWEEN_POWERUPS = 5000;
    private static final float FRICTION = 0.5f;

    public GameLogic(long objectId) {
        this.id = objectId;
        timeSinceLastPowerUp = System.currentTimeMillis();

        Image boxTexture = LoadMap.getInstance().loadImage("/textures/squareTextures/FirstAid.jpg", HEAL_BOX_SIZE);
        Image coinTexture = LoadMap.getInstance().loadImage("/textures/circleTextures/Coin.png", COIN_SIZE);

        powerUpFactory = new PowerUpFactory(FRICTION, boxTexture, coinTexture);
        //powerUpFactory = new PowerUpFactory(0.2f, new Vec2(0.2f, 0.2f), Color.WHITE);
    }

    public void update(){

        if (System.currentTimeMillis() - timeSinceLastPowerUp > TIME_BETWEEN_POWERUPS) {
            PowerUps powerUps = powerUpFactory.powerUp(LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getWorld());
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawObject(powerUps);
            LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUps);
            timeSinceLastPowerUp = System.currentTimeMillis();
        }
    }

    public long getId(){
        return id;
    }
}

