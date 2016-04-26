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
    private Direction direction;
    private World world;
    private JumpHandler currentJumpHandler;
    private Inventory inventory;
    private KeyCode runLeftCode;
    private KeyCode runRightCode;
    private KeyCode jumpCode;
    private KeyCode pickUpCode;
    private KeyCode dropItemCode;
    private Sprite sprite;
    private Vec2 maxVelocity;
    private Vec2 acceleration;
    private Vec2 deceleration;
    private Vec2 size;
    private float restitution;
    private float density;
    private final float sensorThickness;
    private boolean isRunning;
    private boolean grounded;
    private boolean collisionLeft;
    private boolean collisionRight;
    private boolean pickUpItem;
    private static boolean drawSensors = false;                //Used for debugging, draws the sensorFixtures of the player
    private static boolean debugDraw = false;                 //Used for debugging, draws the bodyfixtures over the sprite
    private final int ID;                                     //The unique id of the specific instance of player
    private int score;                                        //The score of the player
    private int actualHealth;                                 //Here we apply the gamelogic.
    private int visibleHealth;                                //This is the health we are showing.
    private long velocityZeroTimer;                           //Keeps track of how long the bodys y velocity has been 0
    private int maxHealth = 100;
    private int deathCount = 0;

    public Player(int ID, World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Sprite sprite) {
        super(position, friction);
        this.ID = ID;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.world = world;
        this.size = sprite.getActualSizeOfSprite();
        this.density = density;
        this.sprite = sprite;
        inventory = new Inventory(this);
        restitution = 0;
        score = 0;
        sensorThickness = size.x / 10;
        velocityZeroTimer = -1;
        direction = Direction.NONE;
        isRunning = false;
        grounded = false;
        collisionLeft = false;
        collisionRight = false;
        pickUpItem = false;
        //Default values, can be changed with setters
        maxVelocity = new Vec2(10f, 20f);
        createBody(world);
        currentJumpHandler = new WallJumpHandler();
        resetHealth(maxHealth);
        //Default valued, can be changed with setters
        pickUpCode = KeyCode.E;
        runLeftCode = KeyCode.A;
        runRightCode = KeyCode.D;
        jumpCode = KeyCode.W;
        dropItemCode = KeyCode.G;
    }

    public Player(int ID, World world, Vec2 position, float friction, float density, Vec2 acceleration, Vec2 deceleration, Color color, Vec2 size) {
        super(position, friction, color);
        this.ID = ID;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.world = world;
        this.size = size;
        this.density = density;
        restitution = 0;
        inventory = new Inventory(this);
        score = 0;
        sprite = null;
        sensorThickness = size.x / 10;
        velocityZeroTimer = -1;
        direction = Direction.NONE;
        isRunning = false;
        grounded = false;
        pickUpItem = false;
        collisionLeft = false;
        collisionRight = false;
        //Default values, can be changed with setters
        maxVelocity = new Vec2(10f, 20f);
        createBody(world);
        currentJumpHandler = new WallJumpHandler();
        resetHealth(maxHealth);
        //Default valued, can be changed with setters
        pickUpCode = KeyCode.E;
        runLeftCode = KeyCode.A;
        runRightCode = KeyCode.D;
        jumpCode = KeyCode.W;
        dropItemCode = KeyCode.G;
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
        float radious;
        Vec2 middleBoxSize;
        Vec2 upperCirclePos;
        Vec2 bottomCirclePos;
        if (size.y / size.x >= 1) {
            radious = size.x/2;
            middleBoxSize = new Vec2(size.x - size.x / 50 , size.x);
            upperCirclePos = ((size.y - radious*4)/2 > 0) ? (new Vec2(0f, -((size.y - radious * 4) / 2) - radious)) : (new Vec2(0f, -radious));
            bottomCirclePos = ((size.y - radious*4)/2 > 0) ? (new Vec2(0f, ((size.y - radious * 4) / 2) + radious)) : (new Vec2(0f, radious));
        }
        else{
            radious = size.y/2;
            middleBoxSize = new Vec2(size.y - size.y / 50, size.y);
            upperCirclePos = ((size.x - radious*4)/2 > 0) ? (new Vec2(-((size.x - radious * 4) / 2) - radious, 0f)) : (new Vec2(-radious, 0f));
            bottomCirclePos = ((size.x - radious*4)/2 > 0) ? (new Vec2(((size.x - radious * 4) / 2) + radious, 0f)) : (new Vec2(radious, 0f));
        }
        Vec2 bottomSensorPos = new Vec2(0f, bottomCirclePos.y + radious);
        Vec2 bottomSensorSize = new Vec2(size.x - size.x / 4, sensorThickness * 2);
        Vec2 leftSensorPos = new Vec2(-size.x / 2 - sensorThickness, 0f);
        Vec2 leftSensorSize = new Vec2(sensorThickness, size.y - size.y/5);
        Vec2 rightSensorPos = new Vec2(size.x/2 + sensorThickness, 0f);
        Vec2 rightSensorSize = new Vec2(sensorThickness, size.y - size.y/5);

        //Initializing the shapes
        upperCircleShape.setRadius(radious);
        bottomCircleShape.setRadius(radious);
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
        upperCircle.filter.groupIndex = -ID;
        middleBox.filter.groupIndex = -ID;
        bottomCircle.filter.groupIndex = -ID;
        bottomSensor.filter.groupIndex = -ID;
        leftSensor.filter.groupIndex = -ID;
        rightSensor.filter.groupIndex = -ID;

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

    public void update(){
        runHandler();

        //Ensures that the sensorvalue is not wrong. If velocity.y is 0 for two frames then the character is grounded
        if (body.getLinearVelocity().y > -0.01 && body.getLinearVelocity().y < 0.01 && !grounded){
            if (velocityZeroTimer == -1){velocityZeroTimer = System.currentTimeMillis();}
            else if (System.currentTimeMillis() - velocityZeroTimer >= 32){
                velocityZeroTimer = -1;
                grounded = true;
            }
        }
        else{
            velocityZeroTimer = -1;
        }

        //Handling updates for the sprite if the player has one.
        if (sprite != null){

            if (!isRunning){sprite.freezeOnFrame(8, 1);}
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
        if (sprite == null || debugDraw) {
            for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                if (fixture.getType() == ShapeType.CIRCLE) {
                    drawCircleFixture(gc, fixture);
                } else if (fixture.getType() == ShapeType.POLYGON && !fixture.isSensor()) {
                    drawBoxPolygonFixture(gc, fixture);
                } else if (fixture.getType() == ShapeType.POLYGON && fixture.isSensor() && drawSensors) {
                    drawSensor(gc, fixture, ((SensorStatus) fixture.getUserData()).isDrawSensor());
                }
            }
        }
    }

    /**
     * Sets the health.
     * @param health The health of the player.
     */
    public void resetHealth(int health){
        actualHealth = visibleHealth = health;
    }

    /**
     * The amount of damage taken.
     * @param damage The damage taken.
     */
    public void damage(int damage){
        actualHealth -= damage;
    }

    /**
     * Draws the healthbar directly over each player.
     * @param gc The graphicscontext on which to draw on.
     */
    private void drawHealthBar(GraphicsContext gc){
        int healthBarWidth = 100;
        int healthBarHeight = 20;
        //This bar shows how much health you have lost.
        gc.setFill(Color.RED);
        gc.fillRect(GameComponent.metersToPix(body.getPosition().x)-healthBarWidth/2,
                GameComponent.metersToPix(body.getPosition().y-(size.y/2))-healthBarHeight, healthBarWidth, healthBarHeight);
        //This bar shows you your current health.
        gc.setFill(Color.GREEN);
        gc.fillRect(GameComponent.metersToPix(body.getPosition().x)-healthBarWidth/2,
                GameComponent.metersToPix(body.getPosition().y-(size.y/2))-healthBarHeight, visibleHealth, healthBarHeight);
        //This if-statement makes the bar "roll", it makes the
        //healthbar change much smoother.
        if(actualHealth < visibleHealth){
            visibleHealth -= 1;
            if (visibleHealth == 0){
                System.out.println("Dead");
                respawn(gc);
            }
        }
        else if(actualHealth != visibleHealth){
            visibleHealth = actualHealth;
            if (visibleHealth == 0){
                System.out.println("Dead");
                respawn(gc);
            }
        }
    }

    /**
     * This method respawns the player after death.
     * @param gc The graphicscontext on which to draw on.
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
        deathCount++;
    }

    /**
     * Heals the player the amount held by the Class 'FirstAidBox'. If the amount of the box
     * exceeds the maximum health of the player it is only healed the amount to bring it up
     * to max health.
     * @param heal The amount the player should be healed.
     */
    public void heal(int heal){
        int tmp;
        if (actualHealth + heal > maxHealth){
            tmp = maxHealth - actualHealth;
            damage(-tmp); //Damage is called with negative value because -(-) = +.
        }
        else{
            damage(-heal);
        }
    }

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

    public void beginContact(Contact contact){
        boolean playerContact = false;
        if (contact.getFixtureA().getBody().getUserData().equals(this) && contact.getFixtureA().isSensor()){
            ((SensorStatus)contact.getFixtureA().getUserData()).setDrawSensor(true);
            playerContact = true;
        }
       	if (contact.getFixtureB().getBody().getUserData().equals(this) && contact.getFixtureB().isSensor()) {
            ((SensorStatus) contact.getFixtureB().getUserData()).setDrawSensor(true);
            playerContact = true;
        }

        if(!isRunning && playerContact) {
            contact.setFriction(100);
        }
    }

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

    private void sensorSwitch(Fixture fixture, boolean setValue){
        switch (((SensorStatus)fixture.getUserData()).getPosition()){
            case DOWN : grounded = setValue;
                break;
            case LEFT: collisionLeft = setValue;
                break;
            case RIGHT: collisionRight = setValue;
                break;
        }
    }

    private void jump(){
        currentJumpHandler.jump(this);
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

    public Inventory getInventory() {return inventory;}

    public Vec2 getSize() {return size;}

    public void setRestitution(float restitution) {this.restitution = restitution;}

    public void setMaxVelocity(Vec2 maxVelocity){
        this.maxVelocity = maxVelocity;
    }

    public boolean getGrounded() {return grounded;}

    public boolean getLeftCollision() {return collisionLeft;}

    public boolean getRightCollision() {return collisionRight;}

    public int getID() {return ID;}

    public void addScore(int score){
        this.score += score;
    }

    public void setRunRightCode(KeyCode runRightCode) {
        this.runRightCode = runRightCode;
    }

    public void setRunLeftCode(KeyCode runLeftCode) {
        this.runLeftCode = runLeftCode;
    }

    public void setJumpCode(KeyCode jumpCode) {
        this.jumpCode = jumpCode;
    }

    public Vec2 getPosition() {return body.getPosition();}

    public int getScore() {
        return score;
    }

    public int getActualHealth() {
        return actualHealth;
    }

    public int getVisibleHealth() {
        return visibleHealth;
    }

    public boolean isPickUpItem() {return pickUpItem;}

    public Direction getDirection() {return direction;}
}
