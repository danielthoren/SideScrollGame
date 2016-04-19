import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
            gameObjects.add(new DynamicSquare(world, new Vec2(2f, 2f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.5f, 2f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(2.3f, 0.5f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1f, 3f), 0.3f, loadImage("textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.5f, 3f), 0.3f, loadImage("textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new DynamicSquare(world, new Vec2(1.3f, 1.5f), 0.3f, loadImage("textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f))));
            gameObjects.add(new Square(world, new Vec2(0f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add(new Square(world, new Vec2(7f, 3f), 1f, Color.BEIGE, 0.4f, 6f));
            Square bottomSquare = new Square(world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, 14.35d, 0.4d);
            gameObjects.add(bottomSquare);

            gameObjects.add(new MovingPlatform(world, new Vec2(0f, 3f), 1f, Color.WHEAT, 1d, 0.1d, new Vec2(7f, 4f)));
            gameObjects.add(new MovingPlatform(world, new Vec2(1f, 3f), 1f, Color.BISQUE, 1d, 0.1d, new Vec2(1f, 5f)));

            Coin coin = (new Coin(world, new Vec2(2f, 4f), 1f, Color.GOLD, 0.2d, objectID++));
            gameObjectsCollision.add(coin);
            gameObjects.add(coin);

            Vec2 position = new Vec2(6f, 5f);
            Vec2 acceleration = new Vec2(50f, 800f);
            Vec2 deceleration = new Vec2(100f, 0f);
            Vec2 size = new Vec2(0.3f, 0.8f); //Must have a ration bigger than 1:2
            float friction = 1f;
            float density = 1f;

            gameObjects.add(new DynamicSquare(world, new Vec2(position.x - 0.03f, position.y - 0.5f), 0.3f, Color.BROWN, 0.4d, 0.4d));

            Player player = new Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLUE);
            ScoreBoard.getInstance().addPlayers(player);
            player.setJump(KeyCode.W);
            player.setLeft(KeyCode.A);
            player.setRight(KeyCode.D);
            gameObjectsListen.add(player);
            gameObjects.add(player);
            gameObjectsCollision.add(player);

            /*Player player2 = new Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLANCHEDALMOND);
            ScoreBoard.getInstance().addPlayers(player2);
            player2.setJump(KeyCode.UP);
            player2.setRight(KeyCode.RIGHT);
            player2.setLeft(KeyCode.LEFT);
            gameObjectsListen.add(player2);
            gameObjects.add(player2);
            gameObjectsCollision.add(player2);*/

            gameObjects.add(ScoreBoard.getInstance());

            Map map = new Map(world, gameObjects, gameObjectsListen, gameObjectsCollision, getMapGravity(mapNumber));
            maps.put(mapNumber, map);
        }
    }

    private Image loadImage(String path, Vec2 size){
        Image image;
        try{
            image = new Image(path, GameComponent.metersToPix(size.x), GameComponent.metersToPix(size.y), false, false);
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
