import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Player implements InputListener
{
    private Vec2 speedVector;
    private Square playerSquare;

    public Player(Square playerSquare, Vec2 speedVector) {
	this.speedVector = speedVector;
	this.playerSquare = playerSquare;

	playerSquare.body.setType(BodyType.DYNAMIC);
	playerSquare.body.getFixtureList().setDensity(1f);
	playerSquare.body.getFixtureList().setRestitution(0.01f);
	playerSquare.body.getFixtureList().setFriction(0.2f);
	playerSquare.body.setFixedRotation(true);
    }

    public void update(){
    }

    public void draw(GraphicsContext gc){
	playerSquare.draw(gc);
    }

    public void inputAction(KeyEvent event){
	if (event.getCode() == KeyCode.A){
	    playerSquare.body.setLinearVelocity(new Vec2(-speedVector.x, 0));
	    System.out.println(playerSquare.body.getLinearVelocity());
	}
	if (event.getCode() == KeyCode.D){
	    playerSquare.body.setLinearVelocity(new Vec2(speedVector.x, 0));
	    System.out.println(playerSquare.body.getLinearVelocity());
	}

    }
}
