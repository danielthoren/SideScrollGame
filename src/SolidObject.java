import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 * Created by daniel on 2016-03-31.
 */
public class SolidObject {
    protected Vec2 pos;     //The position of the upper left corner of the square in meters
    protected Vec2 center;  //The center position of the square in meters (calculated using 'pos', 'height' and 'width')
    protected Body body;    //The body of the square
    protected Image image;    //The image representing the square in the visual realm (=none if no image)
    protected Color color;    //The color of the square (=none if no color)
    protected float friction; //The friction of the square´s body

    /**
     * The default constructor of the class. Should never be used!
     */
    protected SolidObject(){                                                                                   //Why is a default constructor needed and what does it do ?
        this.pos = null;
        this.center = null;
        this.body = null;
        this.image = null;
        this.color = null;
    }

    /**
     * Sets variables for a Solid Object with static position and collision properites.
     * @param pos The position at wich to place the Object (units in meters)
     * @param friction The friction of the body
     * @param image The image to display over the body (visible part of the object).
     */
    public SolidObject(Vec2 pos, float friction, Image image) {
        this.image = image;
        this.friction = friction;
        this.pos = pos;
        color = null;
        Double imHeight = image.getHeight();
        Double imWIdth = image.getWidth();
        center = new Vec2(pos.x + (imWIdth.floatValue()/2), pos.y + (imHeight.floatValue()/2));
    }

    /**
     * Sets variables for a Solid Object with static position and collision properties.
     * @param pos The position at wich to place the object (units in meters)
     * @param friction The fiction of the body
     * @param color The color of the object
     */
    public SolidObject(Vec2 pos, float friction, Color color){
        this.friction = friction;
        this.pos = pos;
        this.color = color;
        this.image = null;
        center = null;
    }

    /**
     * Draws the color, or texture over the body of the object. Do note that the Fx coordinates has pixels as unit while
     * the world coordinates has meters as unit. Also do note that the function draws the square at the origin of the
     * bodycoordinates.
     * @param gc The GraphicsContext to be used to draw with
     */
    protected void drawSquare(GraphicsContext gc, Vec2 position, Double width, Double height){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(position.x), GameComponent.metersToPix(position.y));
        //Rotating the top xy-plane of the stack (the one created above) to the current degree of the body
        gc.rotate(Math.toDegrees(body.getAngle()));
        //Drawing the body so that the center of the visual representation is in the new xy-planes origin
        gc.setFill(color);
        float halfWidth = GameComponent.metersToPix(width.floatValue()) / 2;
        float halfHeight = GameComponent.metersToPix(height.floatValue()) / 2;
        if (image == null){gc.fillRect(-halfWidth, -halfHeight, 2 * halfWidth, 2 * halfHeight);}
        else{gc.drawImage(image, -halfWidth, -halfHeight, 2 * halfWidth, 2 * halfHeight);}
        //Popping the stack, removing the top element, thus leaving the original xy-plane at the top
        gc.restore();
    }

    /**
     * Draws the color, or texture over the body of the object. Do note that the Fx coordinates has pixels as unit while
     * the world coordinates has meters as unit. Also do note that the function draws the circle at the origo of the
     * bodycoordinates.
     * @param gc The GraphicsContext to be used to draw with
     */
    protected void drawCircle(GraphicsContext gc, Vec2 position, Double radious){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(position.x), GameComponent.metersToPix(position.y));
        //Rotating the top xy-plane of the stack (the one created above) to the current degree of the body
        gc.rotate(Math.toDegrees(body.getAngle()));
        //Drawing the body so that the center of the visual representation is in the new xy-planes origin
        gc.setFill(color);
        float pixRadious  = GameComponent.metersToPix(radious.floatValue());
        if (image == null) {gc.fillOval(-pixRadious, -pixRadious, 2 * pixRadious, 2 * pixRadious);}
        else {gc.drawImage(image, -pixRadious/2, pixRadious/2);}
        //Popping the stack, removing the top element, thus leaving the original xy-plane at the top
        gc.restore();
    }

    /**
     * Draws a given square type 'PolygonFixture' (Initialized with the 'SetAsBox()' function).
     * @param gc The 'GraphicsContext' with wich to paint.
     * @param fixture The fixture to paint.
     *
     */
    protected void drawBoxPolygonFixture(GraphicsContext gc, Fixture fixture){
        //Setting pointer to shape object of type 'PolygonShape' (we are sure this is a 'PolygonShape', thus casting)
        PolygonShape polygon = (PolygonShape) fixture.getShape();

        //Calculating the size
        Vec2 size = new Vec2(Math.abs(polygon.getVertex(0).x - polygon.getVertex(1).x), Math.abs(polygon.getVertex(0).y - polygon.getVertex(3).y));

        //Calculating the global coordinates of the center of the square
        Vec2 globalPosition = new Vec2(fixture.getBody().getPosition().x + polygon.getVertex(1).x - size.x / 2, fixture.getBody().getPosition().y + polygon.getVertex(2).y - size.y / 2);

        //Draws the square at the global coordinates
        drawSquare(gc, globalPosition, (double)size.x, (double)size.y);
    }

    /**
     * Draws any given 'Fixture' with a 'Circleshape'.
     * @param gc The 'GraphicsContext' with wich to draw.
     * @param fixture The fixture to draw
     */
    protected void drawCircleFixture(GraphicsContext gc, Fixture fixture){
        //Casting the attatched 'Shape' to CircleShape (this must be a circleshape for the Fixture to be a circle)
        CircleShape circle = (CircleShape) fixture.getShape();
        //Calculating the global coordinates of the circles center
        Vec2 fixturePos = new Vec2(body.getPosition().x, body.getPosition().y - circle.m_p.y);
        Float radious = fixture.getShape().getRadius();
        //Drawing the circle
        drawCircle(gc, fixturePos, radious.doubleValue());
    }


    protected void drawSensor(GraphicsContext gc, Fixture sensor, boolean isTriggered){
        Color tmpColor = color;
        if (isTriggered) {color = Color.GREEN;}
        else {color = Color.RED;}

        drawBoxPolygonFixture(gc, sensor);

        color = tmpColor;
    }
}
