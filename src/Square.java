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

    protected Vec2 pos;
    protected Vec2 center;
    protected Body body;
    private Image image;
    private Color color;
    protected Double height;
    protected Double width;
    private float friction;

    public Square(){                                                                                   //Why is a default constructor needed and what does it do ?
        this.pos = null;
        this.center = null;
        this.body = null;
        this.image = null;
        this.color = null;
        this.height = 0d;
        this.width = 0d;
    }

    public Square(World world, Vec2 pos, float friction, Image image) {
        this.image = image;
        this.friction = friction;
        this.pos = pos;
        width = image.getWidth() / LoadMap.getInstance().getPixPerMeter();
        height = image.getHeight() / LoadMap.getInstance().getPixPerMeter();
        center = new Vec2(pos.x + (width.floatValue()/2), pos.y + (height.floatValue()/2));

        createBody(world);
    }

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

    private void createBody(World world){
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(width.floatValue(), height.floatValue());

        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0f;
        fixtureDef.friction = friction;
        fixtureDef.restitution = 0f;

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

    public void draw(GraphicsContext gc2d){
        if (image == null){
            gc2d.setFill(color);
            gc2d.fillRect(GameComponent.metersToPix(body.getPosition().x), GameComponent.metersToPix(body.getPosition().y), GameComponent.metersToPix(width.floatValue()), GameComponent.metersToPix(height.floatValue()));
        }
        else{
            gc2d.drawImage(image, GameComponent.metersToPix(body.getPosition().x), GameComponent.metersToPix(body.getPosition().y));
        }
    }
}
