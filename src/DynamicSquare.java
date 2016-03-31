import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Subclass of 'Square'. Creates a dynamic square on wich realtime physics is applied.
 */
public class DynamicSquare extends Square {

    private float density;
    private float restitution;
    //Holds the factor of wich the x-velocity is multiplied each tick, lessens the sliding of the object on STATIC surfaces.
    private float slideFactor;

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of the body
     * @param density The density of the body
     * @param restitution The restitution of the body (the bouncines of the body)
     * @param image The image representing the body in the visual realm
     */
    public DynamicSquare(World world, Vec2 pos, float friction, float density, float restitution, Image image) {
        super(world, pos, friction, image);
        body.setUserData(this);
        this.density = density;
        this.restitution = restitution;
        slideFactor = 0.95f;
        makeDynamic();
    }

    /**
     * Creates a square on wich realtime physics is applied.
     * @param world The world in wich to add its body
     * @param pos The position of the topleft corner of the square in meters
     * @param friction The friction of its body
     * @param density The density of its body
     * @param restitution The restitution of its body (the bounciness of the body)
     * @param color The color representing the body in the visual realm
     * @param width The width of the body in meters
     * @param height The height of the body in meters
     */
    public DynamicSquare(World world, Vec2 pos, float friction, float density, float restitution, Color color, double width, double height) {
        super(world, pos, friction, color, width, height);
        body.setUserData(this);
        this.restitution = restitution;
        this.density = density;
        slideFactor = 0.95f;
        //body.setLinearDamping(0.99f);
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

    @Override
    public void update(){
        //Prevents the squares from sliding across STATIC surfaces. More sliding with bigger slideFactor
        //body.setLinearVelocity(new Vec2(body.getLinearVelocity().x * 0.95f, body.getLinearVelocity().y));
    }

    /**
     * Holds the factor of wich the x-velocity is multiplied each tick, lessens the sliding of the object on STATIC surfaces.
     * @param slideFactor The factor, values between 0.1 and 0.99 are accepted
     */
    public void setSlideFactor(float slideFactor) {
        if (!(slideFactor < 0.1 || slideFactor > 0.99)){
            this.slideFactor = slideFactor;
        }
    }
}
