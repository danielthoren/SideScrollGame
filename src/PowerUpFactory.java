import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.jbox2d.common.Vec2;

import java.util.Random;

/**
 * Created by kristiansikiric on 2016-04-21.
 */
public class PowerUpFactory {
    public int randomPowerUP;
    public JumpHandler jumpHandler;

    public Shape powerUp(){
        switch (randomPowerUP){
            case (0):
                int currentId = LoadMap.getObjectID();
                LoadMap.setObjectID(currentId++);
                return new FirstAidBox(world, new Vec2(2f, 4f), 1f, Color.WHITE, 0.4d, 0.4, currentId++);

            case (1):
                currentId = LoadMap.getObjectID();
                LoadMap.setObjectID(currentId++);
                return new PowerUpCoin(world, new Vec2(2f, 4f), 1f, 1f,1f, Color.WHITE, 0.4d, currentId++,jumpHandler);

        }
    }
}
