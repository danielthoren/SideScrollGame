package gameobjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import gamelogic.DrawAndUpdateObject;
import gamelogic.GameComponent;

/**
 * This class creates a circle shape. It extends solid object. Every object that will be
 * in the shape of a circle will implement this class.
 */
public class Circle extends SolidObject implements DrawAndUpdateObject
{
	private Double radius;  //The radius of the circle

	/**
	 * Creates a circle with static position and collision properties.
	 * @param world The world in which to add its body
	 * @param pos The position at which to place the circle (units in meters)
	 * @param friction The friction of the body
	 * @param image The image to display over the body (visible part of the circle)
	 */
	public Circle(long objectID, World world, Vec2 pos, float friction, Image image) {
		super(objectID, pos, friction, image);
		this.radius = (double) GameComponent.pixToMeters((float) image.getWidth() / 2);
		createBody(world);
		body.setUserData(this);
	}

	/**
	 * Creates a circle with static position and collision properties.
	 * @param world The world in which to add its body
	 * @param pos The position at which to place the circle (units in meters)
	 * @param friction The fiction of the body
	 * @param color The color of the circle
	 * @param radius The radius of the circle
	 */
	public Circle(long objectID, World world, Vec2 pos, float friction, Color color, double radius){
		super(objectID, pos, friction, color);
		this.radius = radius;
		createBody(world);
		body.setUserData(this);
	}

	/**
	 * Creates the body of the 'CircleObject' and makes it static.
	 * @param world The world in which to add its body.
	 */
	protected void createBody(World world){
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circleShape = new CircleShape();

		//Do note that the SetAsBox takes half of the width and half of the height then spanning said measurements.
		//out on both sides of the centerpoint (bodyposition)
		circleShape.setRadius(radius.floatValue());

		//Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
		fixtureDef.shape = circleShape;
		fixtureDef.density = DENSITY;
		fixtureDef.friction = friction;
		fixtureDef.restitution = RESTITUTION;

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
		drawCircle(gc, body.getPosition(), radius);
	}
}
