package gamelogic;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for all of the objects that needs updating and drawing
 */
public interface DrawAndUpdateObject extends GameObject{
    /**
     * The function that updates the object every frame
     */
    void update();

    /**
     * The function that draws the object every frame
     * @param gc The GraphicsContext with which to draw
     */
    void draw(GraphicsContext gc);
}
