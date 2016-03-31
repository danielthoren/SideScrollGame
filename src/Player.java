import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;

public class Player implements InputListener
{
    private Vec2 speedVector;
    private Square playerSquare;
    private boolean isRunning;

    public Player(Square playerSquare, Vec2 speedVector) {
        this.speedVector = speedVector;
        this.playerSquare = playerSquare;
        isRunning = false;

        playerSquare.body.setType(BodyType.DYNAMIC);
        playerSquare.body.getFixtureList().setDensity(100f);
        playerSquare.body.getFixtureList().setRestitution(0f);
        playerSquare.body.getFixtureList().setFriction(1000f);
        playerSquare.body.setFixedRotation(true);
    }

    public void update(){
        if (!isRunning){
            playerSquare.body.setLinearVelocity(new Vec2(0, playerSquare.body.getLinearVelocity().y));
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
