/**
 * Created by daniel on 2016-03-12.
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Square implements DrawAndUpdateObject {

    protected Vec2 pos;     //The position of the upper left corner of the square in meters
    protected Vec2 center;  //The center position of the square in meters (calculated using 'pos', 'height' and 'width')
    protected Body body;    //The body of the square
    private Image image;    //The image representing the square in the visual realm (=none if no image)
    private Color color;    //The color of the square (=none if no color)
    protected Double height;//The height of the square in meters
    protected Double width; //The width of the square in meters
    private float friction; //The friction of the square´s body

    /**
     * The default constructor of the class. Should never be used!
     */
    private Square(){                                                                                   //Why is a default constructor needed and what does it do ?
        this.pos = null;
        this.center = null;
        this.body = null;
        this.image = null;
        this.color = null;
        this.height = 0d;
        this.width = 0d;
    }

    /**
     * Creates a square with static position and collision properites.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The friction of the body
     * @param image The image to display over the body (visible part of the square)
     */
    public Square(World world, Vec2 pos, float friction, Image image) {
        this.image = image;
        this.friction = friction;
        this.pos = pos;
        width = image.getWidth() / LoadMap.getInstance().getPixPerMeter();
        height = image.getHeight() / LoadMap.getInstance().getPixPerMeter();
        center = new Vec2(pos.x + (width.floatValue()/2), pos.y + (height.floatValue()/2));

        createBody(world);
    }

    /**
     * Creates a square with static position and collision properties.
     * @param world The world in wich to add its body
     * @param pos The position at wich to place the square (units in meters)
     * @param friction The fiction of the body
     * @param color The color of the Square
     * @param width The width of the square in meters (world coordinates)
     * @param height The height of the square in meters (world coordinates)
     */
    public Square(World world, Vec2 pos, float friction, Color color, double width, double height){
        this.friction = friction;
        this.pos = pos;
        this.color = color;
        this.width = width;
        this.height = height;
        this.image = null;
        center = new Vec2(pos.x + (this.width.floatValue()/2), pos.y + (this.height.floatValue()/2));

        createBody(world);
    }

    /**
     * Creates the body of the 'Square' object.
     * @param world The world in wich to add its body.
     */
    private void createBody(World world){
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        //Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
        //out on both sides of the centerpoint (bodyposition)
        polygonShape.setAsBox(width.floatValue()/2, height.floatValue()/2);

        //Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0f;
        fixtureDef.friction = friction;
        fixtureDef.restitution = 0f;

        //Creating the body using the fixtureDef and the BodyDef created beneath
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(center);
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

    /**
     * Draws the color, or texture over the body of the object. Do note that the Fx coordinates has pixels as unit while
     * the world coordinates has meters as unit.
     * @param gc The GraphicsContext to be used to draw with
     */
    public void draw(GraphicsContext gc){
        if (image == null){
            gc.save();
                gc.translate(GameComponent.metersToPix(getUpLeftCorner().x), GameComponent.metersToPix(getUpLeftCorner().y));
                gc.rotate(Math.toDegrees(body.getAngle()));
                gc.setFill(color);
                float halfWidth = GameComponent.metersToPix(width.floatValue()) / 2;
                float halfHeight = GameComponent.metersToPix(height.floatValue()) / 2;
                gc.fillRect(-halfWidth, -halfHeight, 2 * halfWidth, 2 * halfHeight);
            gc.restore();
        }
        else{
            gc.drawImage(image, GameComponent.metersToPix(getUpLeftCorner().x), GameComponent.metersToPix(getUpLeftCorner().y));
        }
    }


}
