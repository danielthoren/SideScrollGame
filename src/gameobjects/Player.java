package gameobjects;

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
import characterspesific.DefaultJumpHandler;
import characterspesific.Inventory;
import characterspesific.JumpHandler;
import gamelogic.CollisionListener;
import gamelogic.Direction;
import gamelogic.DrawAndUpdateObject;
import gamelogic.GameComponent;
import gamelogic.InputListener;
import gamelogic.LoadMap;
import gamelogic.Map;
import gamelogic.SensorStatus;

/**
 * Class that holds the player which is controlled by the user playing the game.
 */
public class Player extends SolidObject implements DrawAndUpdateObject, CollisionListener, InputListener
{
    private Direction direction;
    private World world;
    private JumpHandler currentJumpHandler;
    private Inventory inventory;
    private KeyCode runRightCode, jumpCode, runLeftCode, pickUpCode, dropItemCode;
    private Sprite sprite;
    private Vec2 spriteIdleFrame;
    private Vec2 maxVelocity, acceleration, deceleration, size;
    private float restitution, density;
    private final float sensorThickness;
    private boolean isRunning, pickUpItem, grounded, collisionLeft, collisionRight;
    private static final boolean DRAW_SENSORS = false;                //Used for debugging, draws the sensorFixtures of the player
    private static final boolean DEBUG_DRAW = false;                 //Used for debugging, draws the bodyFixtures over the sprite
    private int score, actualHealth, visibleHealth, maxHealth;
    private long velocityZeroTimer;
    private boolean startTime = false;
    private int powerUpTime = 0;

    //!OBS: DefaultValues
    private static final int SIX_SECONDS = 360;
    private static final int DEFAULT_MAX_HEALTH = 100;
    private static final Vec2 DEFAULT_MAX_VELOCITY = new Vec2(10f, 20f);
    private static final float GROUNDED_THRESHOLD = 0.01f;
    private static final int APPROXIMATED_FRAME_TIME = 16;

    /**
     * Instantiates a player with the given parameters.
     * @param id The id of this specific gameObject. Used to identify and compare objects with each other.
     * @param world The world in which to create the player.
     * @param position The position at which to create the player.
     * @param friction The friction of the player body.
     * @param density The density of the player body.
     * @param acceleration The acceleration of the player. Used to accelerate when running/jumping.
     * @param deceleration The deceleration of the player. Used to stop the player from running.
     * @param sprite The sprite of the layer. Used to draw the player.
     */
    public Player(long id, World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Sprite sprite) {
        super(id, position, friction);
        this.size = sprite.getActualSizeOfSprite();
        this.sprite = sprite;
        spriteIdleFrame = new Vec2(1,1);
        sensorThickness = size.x / 10;
        constructorInit(world, density, acceleration, deceleration);
    }

    /**
     * Instantiates a player with the given parameters.
     * @param id The id of this specific gameObject. Used to identify and compare objects with each other.
     * @param world The world in which to create the player.
     * @param position The position at which to create the player.
     * @param friction The friction of the player body.
     * @param density The density of the player body.
     * @param acceleration The acceleration of the player. Used to accelerate when running/jumping.
     * @param deceleration The deceleration of the player. Used to stop the player from running.
     * @param color The color of the visual representation of the players body.
     * @param size The size of the players body.
     */
    public Player(long id, World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Color color, Vec2 size) {
        super(id, position, friction, color);
        this.size = size;
        spriteIdleFrame = null;
        sprite = null;
        sensorThickness = size.x / 10;
        constructorInit(world, density, acceleration, deceleration);
    }

    /**
     * Initialization values shared by the constructors. Used to initialize the player.
     * @param world The current game world.
     * @param density The density of the player.
     * @param acceleration The acceleration of the player.
     * @param deceleration The deceleration of the player.
     */
    private void constructorInit(World world, float density, Vec2 acceleration, Vec2 deceleration){
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.world = world;
        this.density = density;
        inventory = new Inventory(this);
        restitution = 0;
        score = 0;
        velocityZeroTimer = -1;
        direction = Direction.RIGHT;
        isRunning = false;
        grounded = false;
        collisionLeft = false;
        collisionRight = false;
        pickUpItem = false;
        //Default values, can be changed with setters
        maxVelocity = DEFAULT_MAX_VELOCITY;
        createBody(world);
        currentJumpHandler = new DefaultJumpHandler();
        maxHealth = DEFAULT_MAX_HEALTH;
        resetHealth(maxHealth);
        //Default values, can be changed with setters
        pickUpCode = KeyCode.E;
        runLeftCode = KeyCode.A;
        runRightCode = KeyCode.D;
        jumpCode = KeyCode.W;
        dropItemCode = KeyCode.G;
    }

    /**
     * Creates the players body using the 'size' parameter to create the different shapes that the body consists of.
     * The body is built up by two circles and a square that sit on top of each other in the following order:
     * circle
     * square
     * circle
     * This is to make the body move smoothly in the game world and to give a valid representation of collision with the
     * player body.
     *
     * !OBS: The function can be shorted but it would be pointless since all that happens in the function is the creation of the different parts
     * of the body, how the parts should look, be sized and positioned relative to each other. Thus the function is left as it is.
     * @param world The world in which to create the player body.
     */
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

        //Do note that the SetAsBox takes half of the width and half of the height then spanning said measurements
        //out on both sides of the center point (bodyPosition). The height of each element is first divided by two
        //(because the shapes takes half width and height) and then by 3 since there are 3 elements on a player.
        float radius;
        Vec2 middleBoxSize;
        Vec2 upperCirclePos;
        Vec2 bottomCirclePos;
        if (size.y / size.x >= 1) {
            radius = size.x/2;
            //size.x/50 is a scalable small number that is subtracted from the middle box to avoid an edge between the circle and the box.
            //noinspection MagicNumber
            middleBoxSize = new Vec2(size.x - size.x / 50 , radius * 2);
            upperCirclePos = (new Vec2(0, (size.y - radius * 4) / 2 > 0 ? -((size.y - radius * 4) / 2) - radius : -radius));
            bottomCirclePos = (new Vec2(0, (size.y - radius * 4) / 2 > 0 ? (size.y - radius * 4) / 2 + radius : radius));
        }
        else{
            radius = size.y/2;
            //size.x/50 is a scalable small number that is subtracted from the middle box to avoid an edge between the circle and the box.
            //noinspection MagicNumber
            middleBoxSize = new Vec2(size.y - size.y / 50, size.y);
            upperCirclePos = (new Vec2((size.x - radius * 4) / 2 > 0 ? -((size.x - radius * 4) / 2) - radius : -radius, 0));
            bottomCirclePos = (new Vec2((size.x - radius * 4) / 2 > 0 ? (size.x - radius * 4) / 2 + radius : radius, 0));
        }
        Vec2 bottomSensorPos = new Vec2(0, bottomCirclePos.y + radius);
        Vec2 bottomSensorSize = new Vec2(size.x - size.x / 4, sensorThickness * 2);
        Vec2 leftSensorPos = new Vec2(-size.x / 2 - sensorThickness, 0);
        Vec2 leftSensorSize = new Vec2(sensorThickness, size.y - size.y/5);
        Vec2 rightSensorPos = new Vec2(size.x/2 + sensorThickness, 0);
        Vec2 rightSensorSize = new Vec2(sensorThickness, size.y - size.y/5);

        //Initializing the shapes
        upperCircleShape.setRadius(radius);
        bottomCircleShape.setRadius(radius);
        middleBoxShape.setAsBox(middleBoxSize.x/2, middleBoxSize.y / 2);
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

        //Setting the group index to be able to prevent collision between the player and certain objects (for example
        //between the player and the inventory-items)
        upperCircle.filter.groupIndex = -(int) ID;
        middleBox.filter.groupIndex = -(int) ID;
        bottomCircle.filter.groupIndex = -(int) ID;
        bottomSensor.filter.groupIndex = -(int) ID;
        leftSensor.filter.groupIndex = -(int) ID;
        rightSensor.filter.groupIndex = -(int) ID;

        //Creating the body using the fixtureDef and the BodyDef created beneath
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos);
        body = world.createBody(bodyDef);
        if (middleBoxSize.y > 0){body.createFixture(middleBox);}
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

    /**
     * The update function runs every frame and updates the functions in player that needs updating. For example it updates
     * the acceleration/deceleration of the player. It also checks that the sensor of the player body is not wrong and so on.
     */
    public void update(){
        runHandler();
        if(startTime){
            powerUpTime++;
            if (powerUpTime > SIX_SECONDS){
                powerUpTime = 0;
                currentJumpHandler = new DefaultJumpHandler();
                startTime = false;
            }
        }

        //Ensures that the sensor value is not wrong. If velocity.y is 0 for two frames then the character is grounded
        if (body.getLinearVelocity().y > -GROUNDED_THRESHOLD && body.getLinearVelocity().y < GROUNDED_THRESHOLD && !grounded){
            if (velocityZeroTimer == -1){velocityZeroTimer = System.currentTimeMillis();}
            else if (System.currentTimeMillis() - velocityZeroTimer >= APPROXIMATED_FRAME_TIME * 2){
                velocityZeroTimer = -1;
                grounded = true;
            }
        }
        else{
            velocityZeroTimer = -1;
        }

        //Handling updates for the sprite if the player has one.
        if (sprite != null){

            if (!isRunning){sprite.freezeOnFrame((int)spriteIdleFrame.x, (int)spriteIdleFrame.y);}
            else{sprite.startSprite();}
            //Updating the sprites position so that the sprite is drawn over the player body.
            sprite.setPosition(body.getPosition());
            //Flips the sprite to make the playerSprite appear to be facing the correct direction.
            if (direction == Direction.LEFT){sprite.setFlip(false);}
            else {sprite.setFlip(true);}
        }
    }

    /**
     * Handles the acceleration and deceleration of the character
     */
    private void runHandler(){
        //Decelerating the player from running to a stop
        if (!isRunning && grounded){
            if (body.getLinearVelocity().x > deceleration.x / 4){
                body.applyForceToCenter(new Vec2(-deceleration.x, 0));
            }
            else if (body.getLinearVelocity().x < -deceleration.x / 4){
                body.applyForceToCenter(new Vec2(deceleration.x, 0));
            }
            else{
                body.setLinearVelocity(new Vec2(0f, body.getLinearVelocity().y));
            }
        }
        if (isRunning && grounded){
            //Code for smooth acceleration when on the ground
            if (direction == Direction.RIGHT && body.getLinearVelocity().x < maxVelocity.x){
                body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x > -maxVelocity.x){
                body.applyForceToCenter(new Vec2(-acceleration.x, 0));
            }
        }
        else if (isRunning && !grounded){
            //Code for smooth acceleration when in the air
            if (direction == Direction.RIGHT && body.getLinearVelocity().x < maxVelocity.x){
                body.applyForceToCenter(new Vec2(acceleration.x / 10, 0));
            }
            else if (direction == Direction.LEFT && body.getLinearVelocity().x > -maxVelocity.x){
                body.applyForceToCenter(new Vec2(-acceleration.x / 10, 0));
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawHealthBar(gc);
        //'DEBUG_DRAW' used for debugging. It draws the body and the texture so that one can observe their relative positions.
        if (sprite == null || DEBUG_DRAW) {
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                if (fixture.getType() == ShapeType.CIRCLE) {
                    drawCircleFixture(gc, fixture);
                } else if (fixture.getType() == ShapeType.POLYGON && !fixture.isSensor()) {
                    drawBoxPolygonFixture(gc, fixture);
                } else if (fixture.getType() == ShapeType.POLYGON && fixture.isSensor() && DRAW_SENSORS) {
                    drawSensor(gc, fixture, ((SensorStatus) fixture.getUserData()).isDrawSensor());
                }
            }
        }
    }

    /**
     * Sets the health to the given parameter.
     * @param health The health of the player.
     */
    public void resetHealth(int health){
        visibleHealth = health;
        actualHealth = visibleHealth;
    }

    /**
     * The amount of damage taken.
     * @param damage The damage taken.
     */
    public void damage(int damage){
        actualHealth -= damage;
    }


    /**
     * Draws the health bar directly over each player.
     * @param gc The graphicsContext on which to draw on.
     */
    private void drawHealthBar(GraphicsContext gc){
        final int healthBarWidth = 100;
        final int healthBarHeight = 20;
        //The difference between the visible health and the actual health
        int healthDelta = visibleHealth - actualHealth;
        //This bar shows how much health you have lost.
        gc.setFill(Color.RED);
        gc.fillRect(GameComponent.metersToPix(body.getPosition().x) - healthBarWidth / 2,
                    GameComponent.metersToPix(body.getPosition().y - (size.y / 2)) - healthBarHeight, healthBarWidth, healthBarHeight);
        //Shows the healthDelta to give a better representation of how much HP you lost.
        gc.setFill(Color.WHITE);
        gc.fillRect(GameComponent.metersToPix(body.getPosition().x) - healthBarWidth / 2 + actualHealth,
                    GameComponent.metersToPix(body.getPosition().y - (size.y / 2)) - healthBarHeight, healthDelta, healthBarHeight);

        //This bar shows you your current health.
        gc.setFill(Color.GREEN);
        gc.fillRect(GameComponent.metersToPix(body.getPosition().x) - healthBarWidth / 2,
                    GameComponent.metersToPix(body.getPosition().y - (size.y / 2)) - healthBarHeight, actualHealth, healthBarHeight);
        //This if-statement makes the bar "roll", it makes the
        //health bar change much smoother.

        if(actualHealth < visibleHealth){
            visibleHealth -= 1;
            if (actualHealth <= 0){
                System.out.println("Dead");
                respawn(gc);
            }
        }
        else if(actualHealth != visibleHealth){
            visibleHealth = actualHealth;
            if (actualHealth <= 0){
                System.out.println("Dead");
                respawn(gc);
            }
        }

    }

    /**
     * This method re-spawns the player after death.
     * @param gc The graphicsContext on which to draw on.
     */
    private void respawn(GraphicsContext gc){
        Map map = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber());
        map.removeBody(body);
        map.removeDrawAndUpdateObject(this);
        map.removeCollisionListener(this);
        map.removeInputListener(this);
        createBody(world);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addCollisionListener(this);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addDrawAndUpdateObject(this);
        LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).addInputListener(this);
        resetHealth(maxHealth);
        addScore(-1);
    }

    /**
     * Heals the player the amount held by the Class 'gameobjects.FirstAidBox'. If the amount of the box
     * exceeds the maximum health of the player it is only healed the amount to bring it up
     * to max health.
     * @param heal The amount the player should be healed.
     */
    public void heal(int heal){
        if (actualHealth + heal > maxHealth){
            damage(-(maxHealth - actualHealth)); //Damage is called with negative value because -(-) = +.
        }
        else{
            damage(-heal);
        }
    }

    /**
     * Handles the inputs that the player listens to.
     * @param event The KeyEvent contains all the information about the input.
     */
    public void inputAction(KeyEvent event){
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode() == runLeftCode) {
                isRunning = true;
                direction = Direction.LEFT;
            }
            if (event.getCode() == runRightCode) {
                isRunning = true;
                direction = Direction.RIGHT;
            }
            if (event.getCode() == jumpCode){
                jump();
            }
            if(event.getCode() == pickUpCode){
                pickUpItem = true;
            }
            if (event.getCode() == dropItemCode){
                inventory.dropCurrentItem();
            }
        }
        else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            if (event.getCode() == runLeftCode){
                isRunning = false;
            }
            if (event.getCode() == runRightCode){
                isRunning = false;
            }
            if (event.getCode() == pickUpCode){
                pickUpItem = false;
            }
        }
    }

    /**
     * Handles the collision checks of the player. If a sensor collides with something then set the specific sensor
     * collision status to either true or false. This is used to determine if the player can jump or not (the bottom sensor
     * must collide with something) among other things.
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     */
    public void beginContact(Contact contact){
        boolean playerContact = false;
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureA().isSensor()){
            sensorSwitch(contact.getFixtureA(), true);
            ((SensorStatus)contact.getFixtureA().getUserData()).setDrawSensor(true);
            playerContact = true;
        }
       	if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureB().isSensor()) {
            sensorSwitch(contact.getFixtureB(), true);
            ((SensorStatus) contact.getFixtureB().getUserData()).setDrawSensor(true);
            playerContact = true;
        }

        if(!isRunning && playerContact) {
            contact.setFriction(100);
        }
    }

    /**
     * Handles the collisionChecks of the player. If a sensor collides with something then set the specific sensor
     * collision status to either true or false. This is used to determine if the player can jump or not (the bottom sensor
     * must collide with something) among other things.
     * @param contact A object containing the two bodies and fixtures that made contact. It also contains collisiondata
     */
    public void endContact(Contact contact){
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureA().isSensor()){
            sensorSwitch(contact.getFixtureA(), false);
            ((SensorStatus)contact.getFixtureA().getUserData()).setDrawSensor(false);
        }
        if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureB().isSensor()){
            sensorSwitch(contact.getFixtureB(), false);
            ((SensorStatus)contact.getFixtureB().getUserData()).setDrawSensor(false);
            }
    }

    /**
     * Checks which sensor has collided and then sets the appropriate flag to the specified value.
     * This is used to determine if the player is on the ground of not (among other things).
     * @param fixture The sensor Fixture that has collided.
     * @param setValue The value to set the flag to (false if collision has ended and true if collision has begun)
     */
    private void sensorSwitch(Fixture fixture, boolean setValue){
        //The cases beneath are the only ones that are going to be sent to this function due to how the body is built up. More might
        //be added later though.
        //noinspection EnumSwitchStatementWhichMissesCases
        switch (((SensorStatus)fixture.getUserData()).getPosition()){
            case DOWN : grounded = setValue;
                break;
            case LEFT: collisionLeft = setValue;
                break;
            case RIGHT: collisionRight = setValue;
                break;
        }
    }

    /**
     * Calls the jumpHandler function that then makes the player jump.
     */
    private void jump(){
        currentJumpHandler.jump(this);
    }

    public Inventory getInventory() {return inventory;}

    public Vec2 getSize() {return size;}

    public void setRestitution(float restitution) {this.restitution = restitution;}

    public void setMaxVelocity(Vec2 maxVelocity){this.maxVelocity = maxVelocity;}

    public void addScore(int score){this.score += score;}

    public boolean getGrounded() {return grounded;}

    public boolean getCollisionLeft() {return collisionLeft;}

    public boolean getCollisionRight() {return collisionRight;}

    public void setRunRightCode(KeyCode runRightCode) {this.runRightCode = runRightCode;}

    public void setRunLeftCode(KeyCode runLeftCode) {this.runLeftCode = runLeftCode;}

    public void setJumpCode(KeyCode jumpCode) {this.jumpCode = jumpCode;}

    public Vec2 getPosition() {return body.getPosition();}

    public int getScore() {return score;}

    public int getActualHealth() {return actualHealth;}

    public int getVisibleHealth() {return visibleHealth;}

    public void setCurrentJumpHandler(JumpHandler currentJumpHandler) {this.currentJumpHandler = currentJumpHandler;}

    public boolean isPickUpItem() {return pickUpItem;}

    public void setStartTime(boolean startTime) {this.startTime = startTime;}

    public Direction getDirection() {return direction;}

    /**
     * Ensures that the vector can only contain integers
     * @param x The x position of the idleFrame.
     * @param y The y position of the idleFrame.
     */
    public void setSpriteIdleFrame(int x, int y) {
        this.spriteIdleFrame = new Vec2(x, y);
    }
}
