package gamelogic;

import characterspesific.JumpHandler;
import characterspesific.PowerUps;
import characterspesific.WallJumpHandler;
import gameobjects.FirstAidBox;
import gameobjects.PowerUpCoin;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class returns either a firsaidbox or a jump powerup given a random number.
 */
public class PowerUpFactory {
    private List<JumpHandler> jumpHandlers;
    private Image firstAidTexture;
    private Image coinTexture;
    private Color color;
    private Random random;
    private float coinRadious;
    private Vec2 boxSize;

    //Todo move constant to appropriate position
    private final static int HEALTH_BOUND = 30;

    public PowerUpFactory(float coinRadious, Vec2 boxSize, Color color) {
        this.color = color;
        this.coinRadious = coinRadious;
        this.boxSize = boxSize;
        jumpHandlers = new ArrayList<>();
        jumpHandlers.add(new WallJumpHandler());
        random = new Random();
    }


    public PowerUpFactory(Image firstAidBoxTexture, Image powerUpCoinTexture) {
        this.firstAidTexture = firstAidBoxTexture;
        this.coinTexture = powerUpCoinTexture;
        jumpHandlers = new ArrayList<>();
        jumpHandlers.add(new WallJumpHandler());
        random = new Random();
    }

    /**
     * This method return a random powerup.
     * @param world The world where the powerup is returned to.
     * @return Returns a powerup.
     */
    public PowerUps powerUp(World world){
        int randomPowerUP = random.nextInt(2); //Creates a random number from 0 to 1.
        int lenList = random.nextInt(jumpHandlers.size()); //Returns a index used to retrive a random jumpHandler. (More can be added)

        FirstAidBox firstAidBox;
        PowerUpCoin powerUpCoin;
        switch (randomPowerUP) {
            case (0):
                int heal = random.nextInt(HEALTH_BOUND);
                if (firstAidTexture == null) {
                    firstAidBox = new FirstAidBox(LoadMap.getObjectID(), world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                            1f, heal, this.color, boxSize.x, boxSize.y); //Creates the box.
                }
                else{
                    firstAidBox = new FirstAidBox(LoadMap.getObjectID(), world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                                                  1f, heal, firstAidTexture); //Creates the box.
                }
                return firstAidBox;


            case (1):
                //This case i similar to the previous. The diffrence being that we create a coin and not a box.
                if(coinTexture == null){
                powerUpCoin = new PowerUpCoin(LoadMap.getObjectID(), world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                                              1f, this.color, coinRadious, jumpHandlers.get(lenList));
                }
                else {
                    powerUpCoin = new PowerUpCoin(LoadMap.getObjectID(), world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                                                  1f, coinTexture, jumpHandlers.get(lenList));
                }
                return powerUpCoin;
            default:
                return null; // Needed or else the method would complain. Returns null if neither case is met.

        }

    }
}
