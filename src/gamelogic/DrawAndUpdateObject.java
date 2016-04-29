package gamelogic;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for all of the objects that needs updating and drawing
 */
public interface DrawAndUpdateObject extends GameObject{
    /**
     * The function that updates the object every frame
     */
    public void update();

    /**
     * The function that draws the object every frame
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc);
}
