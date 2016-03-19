import javafx.scene.canvas.GraphicsContext;

/**
 * Created by daniel on 2016-03-19.
 */
public interface DrawAndUpdateObject {
    public void update();
    public void draw(GraphicsContext gc);
}
