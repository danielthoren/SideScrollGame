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
    private static int objectID;
    private LoadMap() {
        maps = new HashMap<Integer, Map>();
        objectID = 0;
        pixPerMeter = 100;                                 //should be read from file in the future
    }

    public static LoadMap getInstance(){
        return instance;
    }

    public void loadMap(World world, Integer mapNumber){
        List<DrawAndUpdateObject> gameObjects;
        List<InputListener> gameObjectsListen;
        List<CollisionListener> gameObjectsCollision;
        if (!maps.containsKey(mapNumber)){
            gameObjects = new ArrayList<DrawAndUpdateObject>(10);
            gameObjectsListen = new ArrayList<InputListener>(2);
            gameObjectsCollision = new ArrayList<CollisionListener>(2);


            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 2f), 0.3f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 2f), 0.3f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 0.5f), 0.3f, Color.AZURE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(2f, 2f), 0.3f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.5f, 2f), 0.3f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.3f, 0.5f), 0.3f, Color.AZURE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 3f), 0.3f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 3f), 0.3f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 1.5f), 0.3f, Color.AZURE, 0.4d, 0.4d));
            gameObjects.add(new Square(world, new Vec2(0f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add(new Square(world, new Vec2(7f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add(new MovingPlatform(world, new Vec2(0f, 3f), 0.8f, Color.WHEAT, 1d, 0.1d, new Vec2(7f, 4f)));
            gameObjects.add(new MovingPlatform(world, new Vec2(1f, 3f), 0.8f, Color.BISQUE, 1d, 0.1d, new Vec2(1f, 5f)));
            gameObjects.add(new Coin(world, new Vec2(2f, 4f), 1f, Color.GOLD, 0.2d));

            Square bottomSquare = new Square(world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, 14.35d, 0.4d);
            //bottomSquare.body.setTransform(bottomSquare.center, -0.2f);
            gameObjects.add(bottomSquare);

            //gameObjects.add(new DynamicCircle(world, new Vec2(2f, 1f), 0.3f, 0.3f, 0.3f, Color.RED, 0.2d));

            Vec2 position = new Vec2(6f, 0f);
            Vec2 acceleration = new Vec2(50f, 800f);
            Vec2 deceleration = new Vec2(100f, 0f);
            Vec2 size = new Vec2(0.3f, 0.8f); //Must have a ration bigger than 1:2
            float friction = 1f;
            float density = 1f;

            gameObjects.add(new DynamicSquare(world, new Vec2(position.x - 0.03f, position.y - 0.5f), 0.3f, Color.BROWN, 0.4d, 0.4d));

            Player player = new Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLUE);
            gameObjectsListen.add(player);
            gameObjects.add(player);
            gameObjectsCollision.add(player);

            Map map = new Map(gameObjects, gameObjectsListen, gameObjectsCollision, getMapGravity(mapNumber));
            maps.put(mapNumber, map);
        }
    }

    public Vec2 getMapGravity(int mapNumber){
        return new Vec2(0, 20f);                                                                        ///////////////////////////////////Needs to be dealt with
    }

    public float getPixPerMeter(){return pixPerMeter;}

    public Map getMap(Integer mapNumber){
        return maps.get(mapNumber);
    }
}
