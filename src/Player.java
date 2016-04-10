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
import org.jbox2d.dynamics.contacts.Contact;

public class Player extends SolidObject implements InputListener, DrawAndUpdateObject, CollisionListener
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
    private Boolean grounded;
    private Boolean collisionLeft;
    private Boolean collisionRight;
    private static boolean drawSensors = true;                 //Used for debugging, draws the sensorFixtures of the player
    private final int ID;
    private int score;                                        // The score of the player

    public Player(int ID, World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Vec2 size, Color color) {
        super(position, friction, color);
        this.ID = ID;
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
        grounded = false;
        collisionLeft = false;
        collisionRight = false;
        //Default values, can be changed with setters
        maxVelocity = new Vec2(10f, 20f);
        createBody(world);
        body.setUserData(this);
        score = 0;
    }

    private void createBody(World world){
        FixtureDef upperCircle = new FixtureDef();
        FixtureDef middleBox = new FixtureDef();
        FixtureDef bottomCircle = new FixtureDef();
        FixtureDef bottomSensor = new FixtureDef();
        FixtureDef leftSensor = new FixtureDef();
        FixtureDef rightSensor = new FixtureDef();

        PolygonShape middleBoxShape = new PolygonShape();
        PolygonShape bottomSensorShape = new PolygonShape();
        PolygonShape leftSensorShape = new PolygonShape();
        PolygonShape rightSensorShape = new PolygonShape();
        CircleShape upperCircleShape = new CircleShape();
        CircleShape bottomCircleShape = new CircleShape();

        //Do note that the SetAsBox takes half of the width and half of the height then spanning said measurments
        //out on both sides of the centerpoint (bodyposition). The height of each element is first divided by two
        //(because the shapes takes half width and height) and then by 3 since there are 3 elements on a player.
        float middleBoxHeight = (size.y - size.x);
        float radious = size.x/2;
        Vec2 upperCirclePos = new Vec2(0f, -((size.y - radious*4)/2) - radious);
        Vec2 bottomCirclePos = new Vec2(0f, ((size.y - radious*4)/2) + radious);
        Vec2 bottomSensorPos = new Vec2(0f, bottomCirclePos.y + radious);
        Vec2 bottomSensorSize = new Vec2(size.x - size.x/2 , sensorThickness);
        Vec2 leftSensorPos = new Vec2(-size.x, 0f);
        Vec2 leftSensorSize = new Vec2(sensorThickness, size.y - size.y/5);
        Vec2 rightSensorPos = new Vec2(size.x, 0f);
        Vec2 rightSensorSize = new Vec2(sensorThickness, size.y - size.y/5);

        //Initializing the shapes
        upperCircleShape.setRadius(size.x/2);
        bottomCircleShape.setRadius(size.x/2);
        middleBoxShape.setAsBox(size.x/2, middleBoxHeight / 2);
        bottomSensorShape.setAsBox(bottomSensorSize.x / 2, bottomSensorSize.y / 2, bottomSensorPos, 0);
        leftSensorShape.setAsBox(leftSensorSize.x / 2, leftSensorSize.y / 2, leftSensorPos, 0);
        rightSensorShape.setAsBox(rightSensorSize.x / 2, rightSensorSize.y / 2, rightSensorPos, 0);

        //Setting the position of the circles
        upperCircleShape.m_p.set(upperCirclePos);
        bottomCircleShape.m_p.set(bottomCirclePos);

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
        bottomSensor.userData = new SensorStatus(Direction.DOWN);
        leftSensor.shape = leftSensorShape;
        leftSensor.isSensor = true;
        leftSensor.density = 0;
        leftSensor.friction = 0;
        leftSensor.userData = new SensorStatus(Direction.LEFT);
        rightSensor.shape = rightSensorShape;
        rightSensor.isSensor = true;
        rightSensor.density = 0;
        rightSensor.friction = 0;
        rightSensor.userData = new SensorStatus(Direction.RIGHT);

        //Creating the body using the fixtureDef and the BodyDef created beneath
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(center);
        body = world.createBody(bodyDef);
        if (middleBoxHeight > 0){body.createFixture(middleBox);}
        body.createFixture(upperCircle);
        body.createFixture(bottomCircle);
        body.createFixture(bottomSensor);
        body.createFixture(rightSensor);
        body.createFixture(leftSensor);
        body.setType(BodyType.DYNAMIC);
        body.setFixedRotation(true);
        body.setUserData(this);
        body.setActive(true);
        body.setSleepingAllowed(false);
    }

    public void update(){
        System.out.println(score);
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
                    drawSensor(gc, fixture, ((SensorStatus)fixture.getUserData()).isDrawSensor());
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

    public void beginContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureA().isSensor()){
            switch (((SensorStatus)contact.getFixtureA().getUserData()).getPosition()){
                case DOWN :
                    grounded = true;
                    break;
                case LEFT: collisionLeft = true;
                    break;
                case RIGHT: collisionRight = true;
                    break;
            }
            ((SensorStatus)contact.getFixtureA().getUserData()).setDrawSensor(true);
        }
       	if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureB().isSensor()){
            switch (((SensorStatus)contact.getFixtureB().getUserData()).getPosition()){
                case DOWN : grounded = true;
                    break;
                case LEFT: collisionLeft = true;
                    break;
                case RIGHT: collisionRight = true;
                    break;
            }
            ((SensorStatus)contact.getFixtureB().getUserData()).setDrawSensor(true);
       	}
    }

    public void endContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureA().isSensor()){
            switch (((SensorStatus)contact.getFixtureA().getUserData()).getPosition()){
                case DOWN : grounded = false;
                    break;
                case LEFT: collisionLeft = false;
                    break;
                case RIGHT: collisionRight = false;
                    break;
            }
            ((SensorStatus)contact.getFixtureA().getUserData()).setDrawSensor(false);
        }
        if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureB().isSensor()){
            switch (((SensorStatus)contact.getFixtureB().getUserData()).getPosition()){
                case DOWN : grounded = false;
                    break;
                case LEFT: collisionLeft = false;
                    break;
                case RIGHT: collisionRight = false;
                    break;
            }
            ((SensorStatus)contact.getFixtureB().getUserData()).setDrawSensor(false);
        }
    }

    private void jump(){
        float impulse = body.getMass() * 5;
        body.applyLinearImpulse(new Vec2(0f, -impulse), body.getWorldCenter());
    }

    /**
     * Override function of equals checking if this instance of player and the 'obj' are exactly the same by using
     * the players unique ID number.
     * @param obj The object to compare to
     * @return If the obj is the same as this
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player && ((Player) obj).getID() == this.getID()){return true;}
        else {return false;}
    }

    public void setRestitution(float restitution) {this.restitution = restitution;}

    public void setMaxVelocity(Vec2 maxVelocity){
        this.maxVelocity = maxVelocity;
    }

    public int getID() {return ID;}

    public void addScore(int score){
        this.score += score;
    }
}
