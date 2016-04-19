import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.Iterator;

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
    private GraphicsContext gc;                             //The GraphicsContext with wich to draw
    private World world;
    private ContactListenerGame contactListenerGame;
    private double height, width;                           //The height and width of the window in pixels
    private int velocityIterations, positionIterations;     //Values deciding the accuracy of velocity and position
    private Map currentMap;
    private static int currentMapNumber = 1;                        ///////////////////Value needs to be moved to user interface!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    /**
     * Instanciates a game.
     * @param height The height of the window in pixels
     * @param width The width of the window in pixels
     */
    public GameComponent(double height, double width){
        this.height = height;
        this.width = width;

        LoadMap.getInstance().loadMap(currentMapNumber);
        currentMap = LoadMap.getInstance().getMap(currentMapNumber);
        world = currentMap.getWorld();

        contactListenerGame = new ContactListenerGame();
        world.setContactListener(contactListenerGame);

        velocityIterations = 6;  //Accuracy of jbox2d velocity simulation
        positionIterations = 3;  //Accuracy of jbox2d position simulation

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
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
     * Updates all of the game objects
     * Using the 'Iterator' to iterate o
     * @param nanosecScienceLast The time scinse the last update in nanoseconds
     */
    public void update(float nanosecScienceLast){
        world.step(nanosecScienceLast / 1000000000, velocityIterations,positionIterations);
        currentMap.removeStagedOBjects();
        currentMap.addStagedObjects();
        for (Iterator<DrawAndUpdateObject> iterator = currentMap.getDrawAndUpdateObjectList().iterator(); iterator.hasNext();){
            DrawAndUpdateObject obj = iterator.next();
                obj.update();
        }
    }

    /**
     * Draws all of the game objects on the 'canvas'
     */
    public void draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (DrawAndUpdateObject obj : currentMap.getDrawAndUpdateObjectList()){
            obj.draw(gc);
        }
    }

    public static void drawImage(GraphicsContext gc, Image image, Vec2 pos, float angle){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(pos.x), GameComponent.metersToPix(pos.y));
        //Rotating the top xy-plane of the stack (the one created above) to the current degree of the body
        gc.rotate(Math.toDegrees(angle));
        //Drawing the image
        double halfWidth = image.getWidth()/2;
        double halfHeight = image.getHeight()/2;
        gc.drawImage(image, -halfWidth, -halfHeight);
        //Popping the stack, removing the top element, thus leaving the original xy-plane at the top
        gc.restore();
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
