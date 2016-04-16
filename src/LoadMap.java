import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * The singelton that loads and holds the map.
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

    public void loadMap(Integer mapNumber){

        World world = new World(getMapGravity(mapNumber));

        if (!maps.containsKey(mapNumber)){

            List<DrawAndUpdateObject> gameObjects;
            List<InputListener> gameObjectsListen;
            List<CollisionListener> gameObjectsCollision;

            gameObjects = new ArrayList<DrawAndUpdateObject>(10);
            gameObjectsListen = new ArrayList<InputListener>(2);
            gameObjectsCollision = new ArrayList<CollisionListener>(2);


            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 2f), 0.3f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 2f), 0.3f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 0.5f), 0.3f, Color.AZURE, 0.4d, 0.4d));
            /*
            gameObjects.add(new DynamicSquare(world, new Vec2(2f, 2f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.5f, 2f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.3f, 0.5f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 3f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 3f), 0.3f, loadImage("textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 1.5f), 0.3f, loadImage("textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f))));
            */
            gameObjects.add(new Square(world, new Vec2(0f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add(new Square(world, new Vec2(7f, 3f), 1f, Color.BEIGE, 0.4f, 6f));
            Square bottomSquare = new Square(world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, 14.35d, 0.4d);
            gameObjects.add(bottomSquare);

            //gameObjects.add(new MovingPlatform(world, new Vec2(0f, 3f), 1f, Color.WHEAT, 1d, 0.1d, new Vec2(7f, 4f)));
            //gameObjects.add(new MovingPlatform(world, new Vec2(1f, 3f), 1f, Color.BISQUE, 1d, 0.1d, new Vec2(1f, 5f)));

            Coin coin = (new Coin(world, new Vec2(2f, 4f), 1f, Color.GOLD, 0.2d, objectID++));
            gameObjectsCollision.add(coin);
            gameObjects.add(coin);

            Vec2 position = new Vec2(3f, 0f);
            Vec2 acceleration = new Vec2(50f, 800f);
            Vec2 deceleration = new Vec2(100f, 0f);
            Vec2 size = new Vec2(0.3f, 0.8f); //Must have a ration bigger than 1:2
            float friction = 1f;
            float density = 1f;

            gameObjects.add(new Square(world, new Vec2(3,0), friction, Color.RED, 0.4d, 0.4d));
            Sprite sprite = new Sprite(loadImage("/textures/sprites/ExplosionSprite.png", new Vec2(0,0)), 5, 5, 25, 5, new Vec2(3,1), 0);
            Sprite sprite2 = new Sprite(loadImage("/textures/sprites/AgentSprite.png", new Vec2(0,0)), 10, 1, 10, 3, new Vec2(3,2), 0);
            gameObjects.add(sprite);
            gameObjects.add(sprite2);

            //Player player = new Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLUE);
            Player player = new Player(objectID++, world, position, friction, density, acceleration, deceleration, sprite2);
            gameObjectsListen.add(player);
            gameObjects.add(player);
            gameObjectsCollision.add(player);

            Map map = new Map(world, gameObjects, gameObjectsListen, gameObjectsCollision, getMapGravity(mapNumber));
            maps.put(mapNumber, map);
        }
    }

    private Image loadImage(String path, Vec2 size){
        Image image;
        try{
            if (size.x == 0 || size.y == 0){
                image = new Image(path);
            }
            else {
                image = new Image(path, GameComponent.metersToPix(size.x), GameComponent.metersToPix(size.y), false, false);
            }
        }
        catch (NullPointerException | IllegalArgumentException e ){
            image = new Image("textures/squareTextures/ErrorSquare.png", GameComponent.metersToPix(size.x), GameComponent.metersToPix(size.y), false, false);
        }
        return image;
    }

    public Vec2 getMapGravity(int mapNumber){
        return new Vec2(0, 20f);                                                                        ///////////////////////////////////Needs to be dealt with
    }

    public float getPixPerMeter(){return pixPerMeter;}

    public Map getMap(int mapNumber){
        return maps.get(mapNumber);
    }
}
