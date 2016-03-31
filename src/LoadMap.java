import javafx.scene.paint.Color;
import jdk.internal.util.xml.impl.Input;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

/**
 * The singgelton that loads and holds the map.
 */
public final class LoadMap {

    private final static LoadMap instance = new LoadMap();
    private AbstractMap<Integer, Map> maps;

    private static float pixPerMeter;
    private LoadMap() {
        maps = new HashMap<Integer, Map>();
        pixPerMeter = 100;                                      //should be read from file in the future
    }

    public static LoadMap getInstance(){
        return instance;
    }

    public void loadMap(World world, Integer mapNumber){
        List<DrawAndUpdateObject> gameObjects;
        List<InputListener> gameObjectsListen;
        if (!maps.containsKey(mapNumber)){
            gameObjects = new ArrayList<DrawAndUpdateObject>(10);
            gameObjectsListen = new ArrayList<InputListener>(10);

            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 2f), 0.3f, 1f, 0f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 2f), 0.3f, 1f, 0f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 0.5f), 0.3f, 1f, 0f, Color.AZURE, 0.4d, 0.4d));
            gameObjects.add( new Square(world, new Vec2(0f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add( new Square(world, new Vec2(7f, 3f), 1f, Color.BEIGE, 0.4d, 6d));

            Square bottomSquare = new Square(world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, 8d, 0.4d);
            //bottomSquare.body.setTransform(bottomSquare.center, -0.2f);
            gameObjects.add(bottomSquare);

            gameObjects.add(new Circle(world, new Vec2(4f, 4f), 0.3f, Color.RED, 0.2d));
            gameObjects.add(new DynamicCircle(world, new Vec2(5f, 4f), 0.3f, 0.3f, 0.3f, Color.RED, 0.2d));

            DynamicSquare playerSquare = new DynamicSquare(world, new Vec2(2.2f, 1f), 0.3f, 10f, 0f, Color.DEEPPINK, 0.4d, 0.4d);
            //playerSquare.body.setTransform(playerSquare.body.getPosition(), -0.2f);
            gameObjectsListen.add(new Player(playerSquare, new Vec2(10,0)));

            Map map = new Map(gameObjects, gameObjectsListen);
            maps.put(mapNumber, map);
        }
    }

    public float getPixPerMeter(){return pixPerMeter;}

    public Map getMap(Integer mapNumber){
        return maps.get(mapNumber);
    }
}
