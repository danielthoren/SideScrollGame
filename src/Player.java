import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Player extends SolidObject implements InputListener
{

    private Square playerSquare;
    private Direction direction;
    private World world;
    private Vec2 maxVelocity;
    private Vec2 acceleration;
    private Vec2 size;
    private float restitution;
    private float density;
    private boolean isRunning;

    public Player(World world, Vec2 position, float friction, Vec2 acceleration, Vec2 size, Color color) {
        super(position, friction, color);
        center = new Vec2(position.x + size.x/2, position.y + size.y/2);
        this.acceleration = acceleration;
        this.world = world;
        this.size = size;
        this.playerSquare = null;
        density = 10;
        restitution = 0;
        direction = Direction.NONE;
        isRunning = false;
        //Default values, can be changed with setters
        maxVelocity = new Vec2(10f, 20f);
        createBody(world);
        body.setUserData(this);
    }

    private void createBody(World world){
        FixtureDef upperCircle = new FixtureDef();
        FixtureDef middleBox = new FixtureDef();
        FixtureDef bottomCircle = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        CircleShape upperCircleShape = new CircleShape();
        CircleShape bottomCircleShape = new CircleShape();

        //Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
        //out on both sides of the centerpoint (bodyposition). The height of each element is first divided by two
        //(because the shapes takes half width and height) and then by 3 since there are 3 elements on a player.
        float middleBoxHeight = size.y - size.x*2;
        float radious = size.x/2;

        upperCircleShape.setRadius(size.x/2);
        polygonShape.setAsBox(size.x/2, middleBoxHeight);
        upperCircleShape.m_p.set(0f, middleBoxHeight + radious);
        bottomCircleShape.m_p.set(0f, -middleBoxHeight - radious);
        polygonShape.m_centroid.set(0,0);

        //Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
        middleBox.shape = polygonShape;
        middleBox.density = density;
        middleBox.friction = friction;
        middleBox.restitution = restitution;
        bottomCircle.shape = bottomCircleShape;
        bottomCircle.density = density;
        bottomCircle.friction = friction;
        bottomCircle.restitution = restitution;
        upperCircle.shape = upperCircleShape;
        upperCircle.density = density;
        upperCircle.friction = friction;
        upperCircle.restitution = restitution;

        //Creating the body using the fixtureDef and the BodyDef created beneath
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(center);
        body = world.createBody(bodyDef);
        if (middleBoxHeight > 0){body.createFixture(middleBox);}
        body.createFixture(upperCircle);
        body.createFixture(bottomCircle);
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(true);
        body.setActive(true);
    }

    public void update(){
        if (!isRunning){
            if (direction == Direction.RIGHT && body.getLinearVelocity().x > 0){
                body.applyForceToCenter(new Vec2(-acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x < 0){
                body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
        }
        else{
            if (direction == Direction.RIGHT && body.getLinearVelocity().x < maxVelocity.x){
                body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x > -maxVelocity.x){
                body.applyForceToCenter(new Vec2(-acceleration.x, 0));
            }
        }
        /*
        //Good debug print, prints the friction values of all collisions with STATIC Square objects
        for (ContactEdge edge = playerSquare.body.getContactList(); edge != null; edge = edge.next){
            if (edge.contact.isTouching() && edge.contact.getFixtureA().getBody().getUserData() instanceof Square){
                System.out.println("Contact Friction: " + edge.contact.getFriction() + " Square: " + edge.contact.getFixtureA().getFriction() + " Player: " + edge.contact.getFixtureB().getFriction());
            }
        }
        */
    }

    public void draw(GraphicsContext gc) {

        if (body == null) {
            playerSquare.draw(gc);
        } else {
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                System.out.println(fixture.getType());
            }
        }
    }

    public void inputAction(KeyEvent event){
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode() == KeyCode.A) {
                isRunning = true;
                direction = Direction.LEFT;
            }
            if (event.getCode() == KeyCode.D) {
                isRunning = true;
                direction = Direction.RIGHT;
            }
        }
        else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            if (event.getCode() == KeyCode.A){
                isRunning = false;
            }
            if (event.getCode() == KeyCode.D){
                isRunning = false;
            }
        }

    }

    public void setMaxVelocity(Vec2 maxVelocity){
        this.maxVelocity = maxVelocity;
    }
}
