package gameobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Subclass of 'gameobjects.Square'. Creates a dynamic square on wich realtime physics is applied.
 */
public class DynamicSquare extends Square {

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of the body
     * @param image The image representing the body in the visual realm
     */
    public DynamicSquare(long objectID, World world, Vec2 pos, float friction, Image image) {
        super(objectID, world, pos, friction, image);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of its body
     * @param color The color representing the body in the visual realm
     * @param size The size of the object
     */
    public DynamicSquare(long objectID, World world, Vec2 pos, float friction, Color color, Vec2 size) {
        super(objectID, world, pos, friction, color, size);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Makes the body the 'super' classes constructor created dynamic and adds the RESTITUTION and DENSITY to the body.
     */
    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
        this.body.getFixtureList().setRestitution(RESTITUTION);
        this.body.getFixtureList().setDensity(DENSITY);
        this.body.setFixedRotation(false);
    }
}
