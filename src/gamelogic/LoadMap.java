package gamelogic;

import gameobjects.DynamicCircle;
import gameobjects.DynamicSquare;
import gameobjects.Player;
import gameobjects.Sprite;
import gameobjects.Square;
import gameobjects.Sword;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import userinterface.*;
import sun.applet.Main;

import java.util.*;

/**
 * The singelton that loads and holds the map.
 */
public final class LoadMap {


    private final static LoadMap instance = new LoadMap();
    private AbstractMap<Integer, Map> maps;

    private static float pixPerMeter = 100;
    private static int objectID = 2;

    private LoadMap() {
        maps = new HashMap<Integer, Map>();}

    public static LoadMap getInstance(){
        return instance;
    }

    /**
     * Function loading specified map. At the moment it only instantiates hardcoded objects.
     * Todo make loadmap use serializable to load objects. Make saveMap function that saves a map using serializable.
     * @param mapNumber
     */
    public void loadMap(Integer mapNumber){

        World world = new World(getMapGravity(mapNumber));

        if (!maps.containsKey(mapNumber)){

            List<DrawAndUpdateObject> gameObjects;
            List<InputListener> gameObjectsListen;
            List<CollisionListener> gameObjectsCollision;

            gameObjects = new ArrayList<DrawAndUpdateObject>(10);
            gameObjectsListen = new ArrayList<InputListener>(2);
            gameObjectsCollision = new ArrayList<CollisionListener>(2);


            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1f, 2f), 0.3f, Color.BLUE, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.5f, 2f), 0.3f, Color.AQUA, 0.4d, 0.4d));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.3f, 0.5f), 0.3f, Color.AZURE, 0.4d, 0.4d));

            Image stoneBrickWall = loadImage("/textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f));
            Image dessertSquare = loadImage("/textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f));
            Image grayHexagons = loadImage("/textures/squareTextures/GrayHexagons.png", new Vec2(0.4f, 0.4f));


            Image swordIm = loadImage("/textures/Textures/Sword.png", new Vec2(0.2f, 0.4f));
            Sword sword = new Sword(getObjectID(), world, new Vec2(5f, 0f), 0.4f, swordIm, true, 50);
            gameObjects.add(sword);
            gameObjectsCollision.add(sword);

            gameObjects.add(new DynamicCircle(getObjectID(), world, new Vec2(2f,0f), 0.2f, Color.AQUA, 0.2));

            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2f, 2f), 0.3f, stoneBrickWall));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2.5f, 2f), 0.3f, stoneBrickWall));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2.3f, 0.5f), 0.3f, grayHexagons));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1f, 3f), 0.3f, grayHexagons));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.5f, 3f), 0.3f, dessertSquare));
            gameObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.3f, 1.5f), 0.3f, dessertSquare));
            
            gameObjects.add(new Square(getObjectID(), world, new Vec2(0f, 3f), 1f, Color.BEIGE, 0.4d, 6d));
            gameObjects.add(new Square(getObjectID(), world, new Vec2(7f, 3f), 1f, Color.BEIGE, 0.4f, 6f));
            Square bottomSquare = new Square(getObjectID(), world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, 14.35d, 0.4d);
            gameObjects.add(bottomSquare);

            //gameObjects.add(new gameobjects.MovingPlatform(world, new Vec2(0f, 3f), 1f, Color.WHEAT, 1d, 0.1d, new Vec2(7f, 4f)));
            //gameObjects.add(new gameobjects.MovingPlatform(world, new Vec2(1f, 3f), 1f, Color.BISQUE, 1d, 0.1d, new Vec2(1f, 5f)));




            Vec2 position = new Vec2(6f, 5f);

            Vec2 acceleration = new Vec2(50f, 800f);
            Vec2 deceleration = new Vec2(100f, 0f);
            Vec2 size = new Vec2(0.3f, 0.8f); //Must have a ration bigger than 1:2
            float friction = 1f;
            float density = 1f;

            Image agentSprite = loadImage("/textures/sprites/AgentSprite.png", new Vec2(0,0));
            Sprite sprite = new Sprite(getObjectID(), agentSprite, 10, 1, 10, 3, new Vec2(3, 2), 0);
            Sprite sprite2 = new Sprite(getObjectID(), agentSprite, 10, 1, 10, 3, new Vec2(3,2), 0);
            sprite.setActualSizeOfSprite(new Vec2(0.48f, 0.90f));
            sprite2.setActualSizeOfSprite(new Vec2(0.48f, 0.90f));
            gameObjects.add(sprite2);
            gameObjects.add(sprite);

            //gameobjects.Player player = new gameobjects.Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLUE);
            Player player = new Player(getObjectID(), world, position, friction, density, acceleration, deceleration, sprite);

            ScoreBoard.getInstance().addPlayers(player);
            player.setJumpCode(KeyCode.W);
            player.setRunLeftCode(KeyCode.A);
            player.setRunRightCode(KeyCode.D);
            gameObjectsListen.add(player);
            gameObjects.add(player);
            gameObjectsCollision.add(player);


            //gameobjects.Player player2 = new gameobjects.Player(objectID++, world, position, friction, density, acceleration, deceleration, size, Color.BLANCHEDALMOND);
            Player player2 = new Player(getObjectID(), world, position, friction, density, acceleration, deceleration, sprite2);

            ScoreBoard.getInstance().addPlayers(player2);
            player2.setJumpCode(KeyCode.UP);
            player2.setRunRightCode(KeyCode.RIGHT);
            player2.setRunLeftCode(KeyCode.LEFT);
            gameObjectsListen.add(player2);
            gameObjects.add(player2);
            gameObjectsCollision.add(player2);

            gameObjects.add(ScoreBoard.getInstance());
            gameObjects.add(new GameLogic(LoadMap.getObjectID(), world));

            Map map = new Map(world, gameObjects, gameObjectsListen, gameObjectsCollision, getMapGravity(mapNumber));
            maps.put(mapNumber, map);
        }
    }

    public Image loadImage(String path, Vec2 size){
        Image image;
        try{
            if (size.x == 0 || size.y == 0){
                image = new Image(Main.class.getResourceAsStream(path));
            }
            else {
                image = new Image(Main.class.getResourceAsStream(path), GameComponent.metersToPix(size.x), GameComponent.metersToPix(size.y), false, false);
            }
        }
        catch (NullPointerException e ){
            image = new Image(Main.class.getResourceAsStream("/textures/squareTextures/ErrorSquare.png"), GameComponent.metersToPix(size.x), GameComponent.metersToPix(size.y), false, false);
        }
        return image;
    }

    public Vec2 getMapGravity(int mapNumber){
        //Todo gravity constant needs to be moved to appropriate location
        return new Vec2(0, 20f);
    }

    public float getPixPerMeter(){return pixPerMeter;}

    public Map getMap(int mapNumber){
        return maps.get(mapNumber);
    }

    public static int getObjectID() {
        objectID++;
        return objectID;
    }


}
