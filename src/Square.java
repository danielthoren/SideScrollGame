/**
 * Created by daniel on 2016-03-12.
 */
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Square implements DrawAndUpdateObject {

    protected Vec2 pos;
    protected Vec2 center;
    protected Body body;
    private Image image;
    private Color color;
    protected int pixPerMeter;
    protected Double height;
    protected Double width;

    public Square(){                                                                                   //Why is a default constructor needed and what does it do ?
        this.pos = null;
        this.center = null;
        this.body = null;
        this.image = null;
        this.color = null;
        this.pixPerMeter = 0;
        this.height = 0d;
        this.width = 0d;
    }

    public Square(World world, Vec2 pos, int pixPerMeters, Image image) {
        this.image = image;
        this.pixPerMeter = pixPerMeters;
        this.width = image.getWidth() / pixPerMeters;
        this.height = image.getHeight() / pixPerMeters;
        this.pos = pos;
        this.center = new Vec2(pos.x + (width.floatValue()/2), pos.y + (height.floatValue()/2));

        createBody(world);
    }

    public Square(World world, Vec2 pos, int pixPerMeter, Color color, double width, double height){
        this.pixPerMeter = pixPerMeter;
        this.pos = pos;
        this.color = color;
        this.width = width;
        this.height = height;
        this.image = null;
        this.center = new Vec2(pos.x + (this.width.floatValue()/2), pos.y + (this.height.floatValue()/2));

        createBody(world);
    }

    private void createBody(World world){
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(width.floatValue(), height.floatValue());

        fixtureDef.shape = polygonShape;
        fixtureDef.density =0.9f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);
        //bodyDef.type = BodyType.STATIC;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setType(BodyType.STATIC);
        body.setActive(true);
    }

    /**
     * Regular rectangle object does not need updating, static position.
     */
    public void update(){
        System.out.println(body.getPosition().y);
    }

    public void draw(GraphicsContext gc){
        if (image == null){
            gc.setFill(color);
            gc.fillRect(body.getPosition().x * pixPerMeter, body.getPosition().y * pixPerMeter, width * pixPerMeter, height * pixPerMeter);
        }
        else{
            gc.drawImage(image, pos.x * pixPerMeter, pos.y * pixPerMeter);
        }
    }
}
