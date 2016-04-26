import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
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

    /**
     * Creates an instance of this class.
     * @param world The world where the powerups is returned to.
     */
    public PowerUpFactory(World world) {
        jumpHandlers.add(new WallJumpHandler());
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
        switch (randomPowerUP) {
            case (0):
                int currentId = LoadMap.getObjectID();  //Gets the current ID from class load map.
                LoadMap.setObjectID(currentId + 1); //Updates the ID because we are creating a new object that needs a uniqe ID
                FirstAidBox firstAidBox = new FirstAidBox(world, new Vec2(new Random().nextFloat() * 2, new Random().nextFloat() * 4),
                        1f, Color.WHITE, 0.4d, 0.4, currentId++); //Creates the box.
                firstAidBox.setID(currentId); //Sets the ID of the box so it matches with loadmap.
                return firstAidBox;


            case (1):
                //This case i similar to the previous. The diffrence being that we create a coin and not a box.
                currentId = LoadMap.getObjectID();
                LoadMap.setObjectID(currentId + 1);
                PowerUpCoin powerUpCoin = new PowerUpCoin(world, new Vec2(2f, 4f),
                        1f, 1f, 1f, Color.WHITE, 0.2d, currentId++, jumpHandlers.get(lenList));
                powerUpCoin.setID(currentId);
                return powerUpCoin;
            default:
                return null; // Needed or else the method would complain. Returns null if neither case is met.

        }

    }
}
