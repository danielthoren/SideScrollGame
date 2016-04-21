import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.Random;

/**
 * Created by kristiansikiric on 2016-04-21.
 */
public class PowerUpFactory {
    public int randomPowerUP;
    public JumpHandler jumpHandler;

    public PowerUpFactory(World world, int randomPowerUP, JumpHandler jumpHandler) {
        this.randomPowerUP = randomPowerUP;
        this.jumpHandler = jumpHandler;
        powerUp(world);
        System.out.println(randomPowerUP);
    }

    public void powerUp(World world){
        System.out.println(randomPowerUP);
        switch (randomPowerUP){
            case (0):
                int currentId = LoadMap.getObjectID();
                LoadMap.setObjectID(currentId++);
                System.out.println(1);
                FirstAidBox firstAidBox = new FirstAidBox(world, new Vec2(new Random().nextFloat()*2, new Random().nextFloat()*4),
                        1f, Color.WHITE, 0.4d, 0.4, currentId++);
                LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(firstAidBox);
                LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(firstAidBox);
                break;

            case (1):
                currentId = LoadMap.getObjectID();
                LoadMap.setObjectID(currentId++);
                PowerUpCoin powerUpCoin = new PowerUpCoin(world, new Vec2(2f, 4f),
                        1f, 1f,1f, Color.WHITE, 0.4d, currentId++,jumpHandler);
                LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(powerUpCoin);
                LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(powerUpCoin);
                break;

        }
    }
}
