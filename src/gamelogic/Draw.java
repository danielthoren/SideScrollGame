package gamelogic;

import javafx.scene.canvas.GraphicsContext;

/**
 * Every objects that needs updating must implement this interface.
 */
public interface Draw extends GameObject
{
    /**
     * The function that draws the object every frame
     * @param gc The GraphicsContext with wich to draw
     */
    void draw(GraphicsContext gc);
}
