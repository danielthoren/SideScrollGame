package gameobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Created by daniel on 2016-03-31.
 */
public class DynamicCircle extends Circle {

    /**
     * Creates a square on which real time physics is applied.
     * @param world The world in which to add its body
     * @param pos The position of the top left corner of the square in meters
     * @param friction The friction of the body
     * @param image The image representing the body in the visual realm
     */
    public DynamicCircle(long objectID, World world, Vec2 pos, float friction, Image image) {
        super(objectID, world, pos, friction, image);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Creates a square on which real time physics is applied.
     * @param world The world in which to add its body
     * @param pos The position of the top left corner of the square in meters
     * @param friction The friction of its body
     * @param color The color representing the body in the visual realm
     */
    public DynamicCircle(long objectID, World world, Vec2 pos, float friction, Color color, double radius) {
        super(objectID, world, pos, friction, color, radius);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Makes the body the 'super' classes constructor created dynamic and adds the RESTITUTION and DENSITY to the body.
     */
    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
        this.body.setFixedRotation(false);
    }
}
