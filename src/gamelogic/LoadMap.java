package gamelogic;

import gameobjects.DynamicCircle;
import gameobjects.DynamicSquare;
import gameobjects.MovingPlatform;
import gameobjects.Player;
import gameobjects.Sprite;
import gameobjects.Square;
import gameobjects.Sword;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import sun.applet.Main;

import java.util.*;

/**
 * The singelton that loads and holds the map. This is a singelton since there is no need for more than one maploader.
 * All maps and objects can be loaded from this one class. This saves system resources since there is no need for more than one
 * class that loads maps.
 */
@SuppressWarnings("ALL")
public final class LoadMap {


    private final static LoadMap INSTANCE = new LoadMap();
    private AbstractMap<Integer, Map> maps;

    //Todo Move the pixtometer constant to mapfile
    private final static float PIX_PER_METER = 100;
    private static long objectID = 2;

    private LoadMap() {
        maps = new HashMap<Integer, Map>();}

    public static LoadMap getInstance(){
        return INSTANCE;
    }
    //Todo Read value from file
    private static final Vec2 GRAVITY = new Vec2(0, 20);

    /**
     * Function loading specified map. At the moment it only instantiates hardcoded objects.
     * It was planed to keep this code in a file that would be loaded and thats why it holds so many magic numbers.
     * Todo make loadmap use serializable to load objects. Make saveMap function that saves a map using serializable.
     * @param mapNumber The number of the map to be loaded
     */
    public void loadMap(Integer mapNumber){

        World world = new World(GRAVITY);

        if (!maps.containsKey(mapNumber)){

            List<Draw> drawObjects;
            List<Update> updateObjects;
            List<InputListener> gameObjectsListen;
            List<CollisionListener> gameObjectsCollision;

            drawObjects = new ArrayList<>(10);
            updateObjects = new ArrayList<>(10);
            gameObjectsListen = new ArrayList<InputListener>(2);
            gameObjectsCollision = new ArrayList<CollisionListener>(2);


            DynamicSquare dyn1 = new DynamicSquare(getObjectID(), world, new Vec2(1f, 2f), 0.3f, Color.BLUE, new Vec2(0.4f, 0.4f));
            DynamicSquare dyn2 = new DynamicSquare(getObjectID(), world, new Vec2(1.5f, 2f), 0.3f, Color.AQUA, new Vec2(0.4f, 0.4f));
            DynamicSquare dyn3 = new DynamicSquare(getObjectID(), world, new Vec2(1.3f, 0.5f), 0.3f, Color.AZURE, new Vec2(0.4f, 0.4f));
            drawObjects.add(dyn1);
            drawObjects.add(dyn2);
            drawObjects.add(dyn3);

            Image stoneBrickWall = loadImage("/textures/squareTextures/StoneBrickWall.jpg", new Vec2(0.4f, 0.4f));
            Image dessertSquare = loadImage("/textures/squareTextures/DessertSquare.jpg", new Vec2(0.4f, 0.4f));
            Image grayHexagons = loadImage("/textures/squareTextures/GrayHexagons.png", new Vec2(0.4f, 0.4f));

            Image swordIm = loadImage("/textures/Textures/Sword.png", new Vec2(0.2f, 0.4f));
            Sword sword = new Sword(getObjectID(), world, new Vec2(5f, 0f), 0.4f, swordIm, 15);
            sword.setDensity(0.1f);
            drawObjects.add(sword);
            updateObjects.add(sword);
            gameObjectsCollision.add(sword);

            drawObjects.add(new DynamicCircle(getObjectID(), world, new Vec2(2f,0f), 0.2f, Color.AQUA, 0.2f));

            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2f, 2f), 0.3f, stoneBrickWall));
            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2.5f, 2f), 0.3f, stoneBrickWall));
            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(2.3f, 0.5f), 0.3f, grayHexagons));
            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1f, 3f), 0.3f, grayHexagons));
            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.5f, 3f), 0.3f, dessertSquare));
            drawObjects.add(new DynamicSquare(getObjectID(), world, new Vec2(1.3f, 1.5f), 0.3f, dessertSquare));
            
            drawObjects.add(new Square(getObjectID(), world, new Vec2(0f, 3f), 1f, Color.BEIGE, new Vec2(0.4f, 6f)));
            drawObjects.add(new Square(getObjectID(), world, new Vec2(7f, 3f), 1f, Color.BEIGE, new Vec2(0.4f, 6f)));
            Draw bottomSquare = new Square(getObjectID(), world, new Vec2(0f, 5.5f), 0.8f, Color.AZURE, new Vec2(14.35f, 0.4f));
            drawObjects.add(bottomSquare);

            MovingPlatform platform1 = new MovingPlatform(getObjectID(), world, new Vec2(0f, 3f), 1f, Color.WHEAT, new Vec2(1f, 0.1f), new Vec2(7f, 4f));
            MovingPlatform platform2 = new MovingPlatform(getObjectID(), world, new Vec2(1f, 3f), 1f, Color.BISQUE, new Vec2(1f, 0.1f), new Vec2(1f, 5f));
            drawObjects.add(platform1);
            drawObjects.add(platform2);
            updateObjects.add(platform1);
            updateObjects.add(platform2);


            Vec2 position = new Vec2(6f, 5f);

            Vec2 acceleration = new Vec2(50f, 800f);
            Vec2 deceleration = new Vec2(100f, 0f);
            float friction = 1f;
            float density = 1f;

            Image agentSprite = loadImage("/textures/sprites/AgentSprite.png", new Vec2(0,0));
            Sprite sprite = new Sprite(getObjectID(), agentSprite, 10, 1, 10, 3, new Vec2(3, 2), 0);
            Sprite sprite2 = new Sprite(getObjectID(), agentSprite, 10, 1, 10, 3, new Vec2(3,2), 0);
            sprite.setActualSizeOfSprite(new Vec2(0.48f, 0.90f));
            sprite2.setActualSizeOfSprite(new Vec2(0.48f, 0.90f));
            drawObjects.add(sprite2);
            updateObjects.add(sprite2);
            drawObjects.add(sprite);
            updateObjects.add(sprite);

            //gameobjects.Player player = new gameobjects.Player(iD++, world, position, friction, DENSITY, acceleration, deceleration, imSize, Color.BLUE);
            Player player = new Player(getObjectID(), world, position, friction, density, acceleration, deceleration, sprite);

            ScoreBoard.getOurInstance().addPlayers(player);
            player.setSpriteIdleFrame(8,1);
            player.setJumpCode(KeyCode.W);
            player.setRunLeftCode(KeyCode.A);
            player.setRunRightCode(KeyCode.D);
            gameObjectsListen.add(player);
            drawObjects.add(player);
            updateObjects.add(player);
            gameObjectsCollision.add(player);


            //gameobjects.Player player2 = new gameobjects.Player(iD++, world, position, friction, DENSITY, acceleration, deceleration, imSize, Color.BLANCHEDALMOND);
            Player player2 = new Player(getObjectID(), world, position, friction, density, acceleration, deceleration, sprite2);

            ScoreBoard.getOurInstance().addPlayers(player2);
            player2.setSpriteIdleFrame(8,1);
            player2.setJumpCode(KeyCode.UP);
            player2.setRunRightCode(KeyCode.RIGHT);
            player2.setRunLeftCode(KeyCode.LEFT);
            gameObjectsListen.add(player2);
            drawObjects.add(player2);
            updateObjects.add(player2);
            gameObjectsCollision.add(player2);

            drawObjects.add(ScoreBoard.getOurInstance());
            updateObjects.add(new GameLogic(LoadMap.getObjectID()));

            Map map = new Map(world, drawObjects, updateObjects, gameObjectsListen, gameObjectsCollision);
            maps.put(mapNumber, map);
        }
    }

    /**
     * Tries to load images, if the path is invalid then load errortexture.
     * @param path The path to load from.
     * @param size The imSize of the image, if the vector is (0,0) then it loads the default imSize.
     * @return Returns a javafx image.
     */
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

    public float getPixPerMeter(){return PIX_PER_METER;}

    public Map getMap(int mapNumber){
        return maps.get(mapNumber);
    }

    public static long getObjectID() {
        objectID++;
        return objectID;
    }


}
