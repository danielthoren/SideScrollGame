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

    private float density = 10;
    private float restitution = 0.01f;

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of the body
     * @param image The image representing the body in the visual realm
     */
    public DynamicSquare(int ID, World world, Vec2 pos, float friction, Image image) {
        super(ID, world, pos, friction, image);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of its body
     * @param color The color representing the body in the visual realm
     * @param width The width of the body in meters
     * @param height The height of the body in meters
     */
    public DynamicSquare(int ID, World world, Vec2 pos, float friction, Color color, double width, double height) {
        super(ID, world, pos, friction, color, width, height);
        body.setUserData(this);
        makeDynamic();
    }

    /**
     * Makes the body the 'super' classes constructor created dynamic and adds the restitution and density to the body.
     */
    private void makeDynamic(){
        this.body.setType(BodyType.DYNAMIC);
        this.body.getFixtureList().setRestitution(restitution);
        this.body.getFixtureList().setDensity(density);
        this.body.setFixedRotation(false);
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}
