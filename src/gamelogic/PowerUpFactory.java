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
 * Created by kristiansikiric on 2016-04-21.
 */

/**
 * This class returns either a firsaidbox or a jump powerup given a random number.
 */
public class PowerUpFactory {
    List<JumpHandler> jumpHandlers = new ArrayList<JumpHandler>();//Contains all the jumphandlers
    Image firstAidBoxTexture;
    Image powerUpCoinTexture;

    /**
     * Creates an instance of this class.
     * @param world The world where the powerups is returned to.
     */
    public PowerUpFactory(World world) {
        jumpHandlers.add(new WallJumpHandler());
    }
    /**
     * Creates an instance of this class.
     * @param world The world where the powerups is returned to.
     */
    public PowerUpFactory(World world, Image firstAidBoxTexture, Image powerUpCoinTexture) {
        jumpHandlers.add(new WallJumpHandler());
        this.firstAidBoxTexture = firstAidBoxTexture;
        this.powerUpCoinTexture = powerUpCoinTexture;
    }

    /**
     * This method return a random powerup.
     * @param world The world where the powerup is returned to.
     * @return Returns a powerup.
     */
    public PowerUps powerUp(World world){
        Random rand = new Random();
        int randomPowerUP = rand.nextInt(2); //Creates a random number from 0 to 1.


        int lenList = rand.nextInt(jumpHandlers.size()); //Returns a random jumphandler. (More can be added)
        FirstAidBox firstAidBox;
        PowerUpCoin powerUpCoin;
        switch (randomPowerUP) {
            case (0):
                if (firstAidBoxTexture == null) {
                    int currentId = LoadMap.getObjectID();  //Gets the current ID from class load map.
                    firstAidBox = new FirstAidBox(currentId, world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                            1f, Color.WHITE, 0.4d, 0.4d); //Creates the box.
                }
                else{
                    int currentId = LoadMap.getObjectID();  //Gets the current ID from class load map.
                    firstAidBox = new FirstAidBox(currentId, world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                                                  1f, firstAidBoxTexture); //Creates the box.
                }
                return firstAidBox;


            case (1):
                //This case i similar to the previous. The diffrence being that we create a coin and not a box.
                if(powerUpCoinTexture == null){
                int currentId = LoadMap.getObjectID();
                powerUpCoin = new PowerUpCoin(currentId, world, new Vec2(2f, 4f),
                                              1f, 1f, 1f, Color.WHITE, 0.2d, jumpHandlers.get(lenList));
                }
                else {
                    int currentId = LoadMap.getObjectID();
                    powerUpCoin = new PowerUpCoin(currentId, world, new Vec2(2f, 4f),
                            1f, 1f, 1f, powerUpCoinTexture, jumpHandlers.get(lenList));
                }
                return powerUpCoin;
            default:
                return null; // Needed or else the method would complain. Returns null if neither case is met.

        }

    }
}
