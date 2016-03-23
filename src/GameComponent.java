import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class that handles all the logic in the game. It is also a child of the class 'Parent' wich extends
 * 'Node'. Thus making this class a child of 'Node'. This enables the class to be added to another 'Node' or subclass
 * thereof higher up in the hiearchy (usually called the SceneGraph in javaFx). It also instansiates the 'Canvas' class
 * wich is added to this class (in the same manner as this class is added to another subclass of 'Node' higher up in the SceneGraph).
 * The 'Canvas' instance is used to draw all of the game objects on.
 */
public class GameComponent extends Parent
{
    private List<DrawAndUpdateObject> gameObjects;     //The physical objects both drawn and updated
    private List<InputListener> gameObjectsListen;     //The physical objects like above who listen to keyevents
    private Canvas canvas;                                  //The canvas on wich to draw on
    private GraphicsContext gc;                             //The GraphicsContext with wich to draw
    private World world;
    private Vec2 gravity;
    private double height, width;                           //The height and width of the window in pixels
    private int velocityIterations, positionIterations;     //Values deciding the accuracy of velocity and position
    private Map currentMap;

    /**
     * Instanciates a game.
     * @param height The height of the window in pixels
     * @param width The width of the window in pixels
     */
    public GameComponent(double height, double width){
        this.height = height;
        this.width = width;

        velocityIterations = 6;  //Accuracy of jbox2d velocity simulation
        positionIterations = 3;  //Accuracy of jbox2d position simulation

        gravity = new Vec2(0f, 9.82f);
        world = new World(gravity);

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        this.requestFocus();
        gameObjects = new ArrayList<DrawAndUpdateObject>(10);

        LoadMap.getInstance().loadMap(world, 1);
        gameObjects = LoadMap.getInstance().getMap(1).getGameObjects();
        currentMap = LoadMap.getInstance().getMap(1);                                                 ///////////////////Value needs to be moved to user interface

        //Setting the keyevents to listen to
        this.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override public void handle(final KeyEvent event) {
                for (InputListener obj : currentMap.getGameObjectsListen()){
                    obj.inputAction(event);
                }
            }
        });
        this.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override public void handle(final KeyEvent event) {
                for (InputListener obj : currentMap.getGameObjectsListen()){
                    obj.inputAction(event);
                }
            }
        });
    }

    /**
     * Updates all of the game objects
     * @param nanosecScienceLast The time scinse the last update in nanoseconds
     */
    public void update(float nanosecScienceLast){
        world.step(nanosecScienceLast / 1000000000, velocityIterations,positionIterations);
        for (DrawAndUpdateObject obj : currentMap.getGameObjects()){
            obj.update();
        }
        for (InputListener obj : currentMap.getGameObjectsListen()){
            obj.update();
        }
    }

    /**
     * Draws all of the game objects on the 'canvas'
     */
    public void draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (DrawAndUpdateObject obj : currentMap.getGameObjects()){
            obj.draw(gc);
        }
        for (InputListener obj : currentMap.getGameObjectsListen()){
            obj.draw(gc);
        }
    }

    /**
     * Converts the input meters (world coordinates) to pixels (java Fx coordinates) using the scale factor loaded in
     * the 'MapLoader' singgelton.
     * @param meters The amount of meters to be converted
     * @return The amount of pixels the meters corresponds to
     */
    public static float metersToPix(float meters){
        return meters * LoadMap.getInstance().getPixPerMeter();
    }

    /**
     * Converts the input pixels (java Fx coordinates) to meters (world coordinates) using the scale factor loaded in
     * the 'MapLoader' singgelton.
     * @param pix The amount of pixels to be converted
     * @return The amount of meters the pixels corresponds to
     */
    public static float pixToMeters(float pix){
        return pix / LoadMap.getInstance().getPixPerMeter();
    }
}
