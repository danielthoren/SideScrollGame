import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

/**
 * Created by daniel on 2016-03-19.
 */
public final class LoadMap {

    private final static LoadMap instance = new LoadMap();
    private ArrayList<DrawAndUpdateObject> gameObjects;

    private static int loadedMap;
    private static float pixPerMeter;
    private LoadMap() {
        gameObjects = new ArrayList<DrawAndUpdateObject>(10);
        loadedMap = 0;
        pixPerMeter = 100;                                      //should be read from file in the future
    }

    public static LoadMap getInstance(){
        return instance;
    }

    public void loadMap(World world, int mapNumber){
        ArrayList<DrawAndUpdateObject> result;
        if (loadedMap != mapNumber){
            gameObjects = new ArrayList<DrawAndUpdateObject>(10);

            gameObjects.add(new DynamicSquare(world, new Vec2(0.5f, 0.5f), 0.3f, 0.3f, 0.3f, Color.BLUE, 0.2d, 0.2d));
            gameObjects.add(new Square(world, new Vec2(0f, 5f), 0.3f, Color.RED, 5d, 0.3d));
        }
    }

    public float getPixPerMeter(){return pixPerMeter;}

    public ArrayList<DrawAndUpdateObject> getMap(){
        return gameObjects;
    }
}
