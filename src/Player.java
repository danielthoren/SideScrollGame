import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;

public class Player extends SolidObject implements InputListener
{

    private Square playerSquare;
    private Direction direction;
    private World world;
    Fixture groundSensor;
    private Vec2 maxVelocity;
    private Vec2 acceleration;
    private Vec2 deceleration;
    private Vec2 size;
    private float restitution;
    private float density;
    private int jumpCount;                                     //Keeps track of the times the player has jumped since last on the ground
    private boolean isRunning;
    public boolean isAirBorne;
    private static boolean drawSensors = true;                 //Used for debugging, draws the sensorFixtures of the player

    public Player(World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Vec2 size, Color color) {
        super(position, friction, color);
        center = new Vec2(position.x + size.x/2, position.y + size.y/2);
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.world = world;
        this.size = size;
        this.playerSquare = null;
        this.density = density;
        restitution = 0;
        jumpCount = 0;
        direction = Direction.NONE;
        isRunning = false;
        isAirBorne = true;
        //Default values, can be changed with setters
        maxVelocity = new Vec2(10f, 20f);
        createBody(world);
        body.setUserData(this);
    }

    private void createBody(World world){
        FixtureDef upperCircle = new FixtureDef();
        FixtureDef middleBox = new FixtureDef();
        FixtureDef bottomCircle = new FixtureDef();
        FixtureDef bottomSensor = new FixtureDef();

        PolygonShape middleBoxShape = new PolygonShape();
        PolygonShape bottomSensorShape = new PolygonShape();
        CircleShape upperCircleShape = new CircleShape();
        CircleShape bottomCircleShape = new CircleShape();

        //Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
        //out on both sides of the centerpoint (bodyposition). The height of each element is first divided by two
        //(because the shapes takes half width and height) and then by 3 since there are 3 elements on a player.
        float middleBoxHeight = (size.y - size.x);
        float radious = size.x/2;
        Vec2 upperCirclePos = new Vec2(0f, ((size.y - radious*4)/2) + radious);
        Vec2 bottomCirclePos = new Vec2(0f, -((size.y - radious*4)/2) - radious);
        //Vec2 bottomSensorPos = new Vec2(0f, bottomCirclePos.y - radious - sensorThickness);
        //Vec2 bottomSensorSize = new Vec2(size.x , 0.1f);
        Vec2 bottomSensorPos = new Vec2(size.x, 0);
        Vec2 bottomSensorSize = new Vec2(sensorThickness + 0.2f , size.y);

        upperCircleShape.setRadius(size.x/2);
        bottomCircleShape.setRadius(size.x/2);
        middleBoxShape.setAsBox(size.x/2, middleBoxHeight / 2);
        bottomSensorShape.setAsBox(bottomSensorSize.x / 2, bottomSensorSize.y / 2, new Vec2(-1.5f, 0), 0);
        System.out.println(radious);

        upperCircleShape.m_p.set(upperCirclePos);
        middleBoxShape.m_centroid.set(0f, 0f);
        bottomCircleShape.m_p.set(bottomCirclePos);
        bottomSensorShape.m_centroid.set(0, 0);//bottomSensorPos);

        //Creating the fixture of the body. The concrete part that can be touched (the part that can collide)
        upperCircle.shape = upperCircleShape;
        upperCircle.density = density;
        upperCircle.friction = 0;
        upperCircle.restitution = restitution;
        upperCircle.userData = upperCirclePos;
        upperCircle.isSensor = false;
        middleBox.shape = middleBoxShape;
        middleBox.density = density;
        middleBox.friction = 0;
        middleBox.restitution = restitution;
        middleBox.userData = new Vec2(size.x, middleBoxHeight);
        middleBox.isSensor = false;
        bottomCircle.shape = bottomCircleShape;
        bottomCircle.density = density;
        bottomCircle.friction = friction;
        bottomCircle.restitution = restitution;
        bottomCircle.userData = bottomCirclePos;
        bottomCircle.isSensor = false;
        bottomSensor.shape = bottomSensorShape;
        bottomSensor.isSensor = true;
        bottomSensor.density = 0;
        bottomSensor.friction = 0;
        bottomSensor.userData = "sensor";
        bottomSensor.userData = bottomSensorSize;

        System.out.println(bottomCirclePos);

        //Creating the body using the fixtureDef and the BodyDef created beneath
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(center);
        body = world.createBody(bodyDef);
        if (middleBoxHeight > 0){body.createFixture(middleBox);}
        body.createFixture(upperCircle);
        body.createFixture(bottomCircle);
        groundSensor = body.createFixture(bottomSensor);
        //groundSensor.setSensor(true);
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(true);
        body.setUserData(this);
        body.setActive(true);
        body.setSleepingAllowed(false);
    }

    public void update(){
        if (!isRunning){
            if (direction == Direction.RIGHT && body.getLinearVelocity().x > 0){
                body.applyForceToCenter(new Vec2(-deceleration.x, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x < 0){
                body.applyForceToCenter(new Vec2(deceleration.x, 0));
            }
            else{
                body.setLinearVelocity(new Vec2(0f, body.getLinearVelocity().y));
            }
        }
        else{
            //Code for smooth dexeleration when keys released
            if (direction == Direction.RIGHT && body.getLinearVelocity().x < maxVelocity.x){
                body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x > -maxVelocity.x){
                body.applyForceToCenter(new Vec2(-acceleration.x, 0));
            }
        }
        //Good debug print
        /*
        for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()){
            if (fixture.isSensor()){
                PolygonShape poly = (PolygonShape) fixture.getShape();
                System.out.println(poly.m_centroid);
            }
        }*/
        //Good debug print, prints the friction values of all collisions with STATIC Square objects
        /*
        for (ContactEdge edge = body.getContactList(); edge != null; edge = edge.next){
            if (edge.contact.isTouching() && edge.contact.getFixtureA().getBody().getUserData() instanceof Square){
                System.out.println("Contact Friction: " + edge.contact.getFriction() + " Square: " + edge.contact.getFixtureA().getFriction() + " Player: " + edge.contact.getFixtureB().getFriction());
            }
        }*/
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (body == null) {
            playerSquare.draw(gc);
        } else {
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                if (fixture.getType() == ShapeType.CIRCLE){
                    drawCircleFixture(gc, fixture);
                }
                else if (fixture.getType() == ShapeType.POLYGON && !fixture.isSensor()){
                    drawBoxPolygonFixture(gc, fixture);
                }
                else if (fixture.getType() == ShapeType.POLYGON && fixture.isSensor() && drawSensors){
                    drawSensor(gc, fixture, !isAirBorne);
                }
            }
        }
    }

    public void inputAction(KeyEvent event){
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                isRunning = true;
                direction = Direction.LEFT;
            }
            if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                isRunning = true;
                direction = Direction.RIGHT;
            }
            if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP){
                jump();
            }
        }
        else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT){
                isRunning = false;
            }
            if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT){
                isRunning = false;
            }
        }
    }

    private void airborne(boolean isAirBorne){
        this.isAirBorne = isAirBorne;
    }

    private void jump(){
        float impulse = body.getMass() * 5;
        body.applyLinearImpulse(new Vec2(0f, -impulse), body.getWorldCenter());
    }

    public void setRestitution(float restitution) {this.restitution = restitution;}

    public void setMaxVelocity(Vec2 maxVelocity){
        this.maxVelocity = maxVelocity;
    }
}
