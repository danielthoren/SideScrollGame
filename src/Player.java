import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;

public class Player implements InputListener
{
    private Vec2 speedVector;
    private Square playerSquare;
    private boolean isRunning;

    public Player(DynamicSquare playerSquare, Vec2 speedVector) {
        this.speedVector = speedVector;
        this.playerSquare = playerSquare;
        isRunning = false;
        playerSquare.body.setUserData(this);

        playerSquare.body.setFixedRotation(true);

        playerSquare.body.setUserData(this);
    }

    public void update(){
        if (!isRunning){
            playerSquare.body.setLinearVelocity(new Vec2(0, playerSquare.body.getLinearVelocity().y));
        }
        for (ContactEdge edge = playerSquare.body.getContactList(); edge != null; edge = edge.next){
            //System.out.println(edge.contact.getFixtureA().getBody().getUserData());
            if (edge.contact.isTouching() && edge.contact.getFixtureA().getBody().getUserData() instanceof Square){
                System.out.println(edge.contact.getFriction() + "square: " + edge.contact.getFixtureA().getFriction() + "player: " + edge.contact.getFixtureB().getFriction());
            }
        }
    }

    public void draw(GraphicsContext gc){
        playerSquare.draw(gc);
    }

    public void inputAction(KeyEvent event){
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode() == KeyCode.A) {
                isRunning = true;
                playerSquare.body.setLinearVelocity(new Vec2(-speedVector.x, 0));
            }
            if (event.getCode() == KeyCode.D) {
                isRunning = true;
                playerSquare.body.setLinearVelocity(new Vec2(speedVector.x, 0));
            }
        }
        else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)){
            if (event.getCode() == KeyCode.A){
                isRunning = false;
                playerSquare.body.setLinearVelocity(new Vec2(0, playerSquare.body.getLinearVelocity().y));
            }
            if (event.getCode() == KeyCode.D){
                isRunning = false;
                playerSquare.body.setLinearVelocity(new Vec2(0, playerSquare.body.getLinearVelocity().y));
            }
        }

    }
}
