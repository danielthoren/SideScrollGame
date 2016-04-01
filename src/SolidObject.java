import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

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
     * @param image The image to display over the body (visible part of the object)
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
     * the world coordinates has meters as unit.
     * @param gc The GraphicsContext to be used to draw with
     */
    public void drawSquare(GraphicsContext gc, Double width, Double height){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(body.getPosition().x), GameComponent.metersToPix(body.getPosition().y));
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
     * the world coordinates has meters as unit.
     * @param gc The GraphicsContext to be used to draw with
     */
    public void drawCircle(GraphicsContext gc, Double radious, Color color){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(body.getPosition().x), GameComponent.metersToPix(body.getPosition().y));
        //Rotating the top xy-plane of the stack (the one created above) to the current degree of the body
        gc.rotate(Math.toDegrees(body.getAngle()));
        //Drawing the body so that the center of the visual representation is in the new xy-planes origin
        gc.setFill(color);
        float pixRadious  = GameComponent.metersToPix(radious.floatValue());
        gc.fillOval(-pixRadious, -pixRadious, 2 * pixRadious, 2 * pixRadious);
        //Popping the stack, removing the top element, thus leaving the original xy-plane at the top
        gc.restore();
    }
}
