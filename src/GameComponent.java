import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

public class GameComponent extends Parent
{
    private ArrayList<DrawAndUpdateObject> gameObjects;
    private Canvas canvas;
    private GraphicsContext gc2d;
    private World world;
    private Vec2 gravity;
    private double height, width;
    private int velocityIterations, positionIterations;

    public GameComponent(double height, double width){
        this.height = height;
        this.width = width;
        velocityIterations = 6;  //Accuracy of jbox2d velocity simulation
        positionIterations = 3;  //Accuracy of jbox2d position simulation

        gravity = new Vec2(0f, 9.82f);
        world = new World(gravity);

        canvas = new Canvas(width, height);
        gc2d = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        gameObjects = new ArrayList<DrawAndUpdateObject>(10);

        LoadMap.getInstance().loadMap(world, 1);
        gameObjects = LoadMap.getInstance().getMap();
    }

    public void update(float nanosecScienceLast){
        world.step(nanosecScienceLast / 1000000000, velocityIterations,positionIterations);
        for (DrawAndUpdateObject obj : gameObjects){
            obj.update();
        }
    }

    public void draw(){
        GraphicsContext gc2d = canvas.getGraphicsContext2D();
        gc2d.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (DrawAndUpdateObject obj : gameObjects){
            obj.draw(gc2d);
        }
    }
}
