import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;

public class Player implements InputListener
{
    private Vec2 speedVector;

    public Player(Square playerSquare, Vec2 speedVector) {
	this.speedVector = speedVector;
    }

    public void update(){

    }

    public void draw(GraphicsContext gc){

    }

    public void inputAction(){

    }
}
