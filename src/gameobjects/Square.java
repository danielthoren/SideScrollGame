package gameobjects;
import gamelogic.Draw;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import gamelogic.LoadMap;

/**
 * Class that creates a squareobject that is a part of the current world
 */
public class Square extends SolidObject implements Draw
{
    protected Vec2 size;

    /**
     * Creates a square with static position and collision properites.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The friction of the body
     * @param image The image to display over the body (visible part of the square)
     */
    public Square(long objectID, World world, Vec2 pos, float friction, Image image) {
	super(objectID, pos, friction, image);
	size = new Vec2((float) image.getWidth() / LoadMap.getInstance().getPixPerMeter(), (float) image.getHeight() / LoadMap.getInstance().getPixPerMeter());
	createBody(world, 5f, friction, 0.01f);
	body.setUserData(this);
    }

    /**
     * Creates a square with static position and collision properties.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The fiction of the body
     * @param color The color of the gameobjects.Square
     * @param size The size of the object
     */
    public Square(long objectID, World world, Vec2 pos, float friction, Color color, Vec2 size){
	super(objectID, pos, friction, color);
	this.size = size;
	createBody(world, 5f, friction, 0.01f);
	body.setUserData(this);
    }

    /**
     * Creates the body of the 'gameobjects.Square' object.
     * @param world The world in wich to add its body.
     */
    protected void createBody(World world, float density, float friction, float restitution){
	FixtureDef fixtureDef = new FixtureDef();
	PolygonShape polygonShape = new PolygonShape();

	//Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
	//out on both sides of the centerpoint (bodyposition)
	polygonShape.setAsBox(size.x/2, size.y/2);

	//Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
	fixtureDef.shape = polygonShape;
	fixtureDef.density = density;
	fixtureDef.friction = friction;
	fixtureDef.restitution = restitution;

	//Creating the body using the fixtureDef and the BodyDef created beneath
	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(pos);
	body = world.createBody(bodyDef);
	body.createFixture(fixtureDef);

	body.setType(BodyType.STATIC);
	body.setActive(true);
    }

    @Override
    public void draw(GraphicsContext gc){
	//drawSquare(gc, body.getPosition(), width, height);
	drawBoxPolygonFixture(gc, body.getFixtureList());
    }

    public Vec2 getSize() {return size;}
}
