import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;

public class Player implements InputListener
{
    private Vec2 acceleration;
    private Square playerSquare;
    private boolean isRunning;
    private Direction direction;
    private Vec2 maxVelocity;

    public Player(DynamicSquare playerSquare, Vec2 acceleration) {
        this.acceleration = acceleration;
        this.playerSquare = playerSquare;
        direction = Direction.NONE;
        isRunning = false;
        maxVelocity = new Vec2(10f, 20f);
        playerSquare.body.setUserData(this);

        playerSquare.body.setFixedRotation(true);

        playerSquare.body.setUserData(this);
    }

    public void update(){
        if (!isRunning){
            if (direction == Direction.RIGHT && playerSquare.body.getLinearVelocity().x > 0){
                playerSquare.body.applyForceToCenter(new Vec2(-acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && playerSquare.body.getLinearVelocity().x < 0){
                playerSquare.body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
        }
        else{
            if (direction == Direction.RIGHT && playerSquare.body.getLinearVelocity().x < maxVelocity.x){
                playerSquare.body.applyForceToCenter(new Vec2(acceleration.x, 0));
            }
            else if (direction == Direction.LEFT && playerSquare.body.getLinearVelocity().x > -maxVelocity.x){
                playerSquare.body.applyForceToCenter(new Vec2(-acceleration.x, 0));
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

    public void draw(GraphicsContext gc){
        playerSquare.draw(gc);
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
}
