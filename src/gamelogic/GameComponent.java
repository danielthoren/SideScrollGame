package gamelogic;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * This is the class that handles all the logic in the game. It is also a child of the class 'Parent' wich extends
 * 'Node'. Thus making this class a child of 'Node'. This enables the class to be added to another 'Node' or subclass
 * thereof higher up in the hiearchy (usually called the SceneGraph in javaFx). It also instansiates the 'Canvas' class
 * wich is added to this class (in the same manner as this class is added to another subclass of 'Node' higher up in the SceneGraph).
 * The 'Canvas' instance is used to draw all of the game objects on.
 */
public class GameComponent extends Parent
{
    private Canvas canvas;                                  //The canvas on wich to draw on
    private World world;
    private int velocityIterations, positionIterations;     //Values deciding the accuracy of velocity and position
    private Map currentMap;
    private static int currentMapNumber = 1;
    private static final long NANOS_PER_SECOND = 1000000000;
    /**
     * Instanciates a game.
     * @param height The height of the window in pixels
     * @param width The width of the window in pixels
     */
    public GameComponent(double height, double width){
        LoadMap.getInstance().loadMap(currentMapNumber);
        currentMap = LoadMap.getInstance().getMap(currentMapNumber);
        world = currentMap.getWorld();

        ContactListenerGame contactListenerGame;
        contactListenerGame = new ContactListenerGame();
        world.setContactListener(contactListenerGame);

        velocityIterations = 6;  //Accuracy of jbox2d velocity simulation
        positionIterations = 3;  //Accuracy of jbox2d position simulation

        canvas = new Canvas(width, height);
        getChildren().add(canvas);
        this.requestFocus();

        //Setting the keyevents to listen to
        this.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override public void handle(final KeyEvent event) {
                for (InputListener obj : currentMap.getInputListenerList()){
                    obj.inputAction(event);
                }
            }
        });
        this.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override public void handle(final KeyEvent event) {
                for (InputListener obj : currentMap.getInputListenerList()){
                    obj.inputAction(event);
                }
            }
        });
    }

    /**
     * Updates all of the game objects. Since 'jBox2d' is not threadsafe the method is syncronized with the
     * @param nanosecScienceLast The time scinse the last update in nanoseconds
     */
    public void update(float nanosecScienceLast){
        currentMap.removeStagedOBjects();
        currentMap.addStagedObjects();
        try {
            world.step(nanosecScienceLast / NANOS_PER_SECOND, velocityIterations, positionIterations);
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.exit(0);
        }
        for (Update obj : currentMap.getUpdateObjects()) {
            obj.update();
        }
    }

    /**
     * Draws all of the game objects on the 'canvas'
     */
    public void draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (Draw obj : currentMap.getDrawObjects()){
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

    public static int getCurrentMapNumber() {
        return currentMapNumber;
    }

}
