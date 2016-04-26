/**
 * Created by daniel on 2016-03-12.
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Circle extends SolidObject implements DrawAndUpdateObject {
    private Double radious;  //The radious of the circle
    private final int ID;

    /**
     * Creates a circle with static position and collision properites.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the circle (units in meters)
     * @param friction The friction of the body
     * @param image The image to display over the body (visible part of the circle)
     */
    public Circle(int ID, World world, Vec2 pos, float friction, Image image) {
	super(pos, friction, image);
	this.ID = ID;
	this.radious = image.getWidth()/2;

	createBody(world);
	body.setUserData(this);
    }

    /**
     * Creates a circle with static position and collision properties.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the circle (units in meters)
     * @param friction The fiction of the body
     * @param color The color of the circle
     * @param radious The radious of the circle
     */
    public Circle(int ID, World world, Vec2 pos, float friction, Color color, double radious){
	super(pos, friction, color);
	this.ID = ID;
	this.radious = radious;

	createBody(world);
	body.setUserData(this);
    }

    /**
     * Creates the body of the 'CircleObject' and makes it static.
     * @param world The world in wich to add its body.
     */
    private void createBody(World world){
	FixtureDef fixtureDef = new FixtureDef();
	CircleShape circleShape = new CircleShape();

	//Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
	//out on both sides of the centerpoint (bodyposition)
	circleShape.setRadius(radious.floatValue());

	//Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
	fixtureDef.shape = circleShape;
	fixtureDef.density = 0f;
	fixtureDef.friction = friction;
	fixtureDef.restitution = 0f;

	//Creating the body using the fixtureDef and the BodyDef created beneath
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(pos);
	body = world.createBody(bodyDef);
	body.createFixture(fixtureDef);
	body.setType(BodyType.STATIC);
	body.setActive(true);
    }

    /**
     * Regular rectangle object does not need updating, static position.
     */
    public void update(){
    }

    /**
     * Draws the color, or texture over the body of the object. Do note that the Fx coordinates has pixels as unit while
     * the world coordinates has meters as unit.
     * @param gc The GraphicsContext to be used to draw with
     */
    public void draw(GraphicsContext gc){
	if (image == null){
	    super.drawCircle(gc, body.getPosition(), radious);
	}
	else{
	    super.drawSquare(gc, body.getPosition(), radious, radious);
	}
    }

    @Override public int getID() {return ID;}
}
