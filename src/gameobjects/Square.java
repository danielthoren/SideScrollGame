package gameobjects; /**
 * Created by daniel on 2016-03-12.
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import gamelogic.DrawAndUpdateObject;
import gamelogic.LoadMap;

/**
 * Class that creates a squareobject that is a part of the current world
 */
public class Square extends SolidObject implements DrawAndUpdateObject
{
    protected Double height;//The height of the square in meters
    protected Double width; //The width of the square in meters

    /**
     * Creates a square with static position and collision properites.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The friction of the body
     * @param image The image to display over the body (visible part of the square)
     */
    public Square(int ID, World world, Vec2 pos, float friction, Image image) {
	super(ID, pos, friction, image);
	width = image.getWidth() / LoadMap.getInstance().getPixPerMeter();
	height = image.getHeight() / LoadMap.getInstance().getPixPerMeter();

	createBody(world);
	body.setUserData(this);
    }

    /**
     * Creates a square with static position and collision properties.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The fiction of the body
     * @param color The color of the gameobjects.Square
     * @param width The width of the square in meters (world coordinates)
     * @param height The height of the square in meters (world coordinates)
     */
    public Square(int ID, World world, Vec2 pos, float friction, Color color, double width, double height){
	super(ID, pos, friction, color);
	this.width = width;
	this.height = height;
	createBody(world);
	body.setUserData(this);
    }

    /**
     * Creates the body of the 'gameobjects.Square' object.
     * @param world The world in wich to add its body.
     */
    protected void createBody(World world){
	FixtureDef fixtureDef = new FixtureDef();
	PolygonShape polygonShape = new PolygonShape();

	//Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
	//out on both sides of the centerpoint (bodyposition)
	polygonShape.setAsBox(width.floatValue()/2, height.floatValue()/2);

	//Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
	fixtureDef.shape = polygonShape;
	fixtureDef.density = 10;
	fixtureDef.friction = friction;
	fixtureDef.restitution = 0;

	//Creating the body using the fixtureDef and the BodyDef created beneath
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(pos);
	body = world.createBody(bodyDef);
	body.createFixture(fixtureDef);

	body.setType(BodyType.STATIC);
	body.setActive(true);
    }

    /**
     * Returns the world coordiate of the upper left corner of the body. The standard position is based on
     * the body´s centerpoint.
     * @return The world coordinates of the upper left corner of the body.
     */
    protected Vec2 getUpLeftCorner(){
	return new Vec2(body.getPosition().x - (width.floatValue()/2), body.getPosition().y + (height.floatValue()/2));
    }

    /**
     * Regular rectangle object does not need updating, static position.
     */
    public void update(){
    }

    @Override
    public void draw(GraphicsContext gc){
	super.drawSquare(gc, body.getPosition(), width, height);
    }
}
