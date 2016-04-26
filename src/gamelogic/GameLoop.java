package gamelogic;

import javafx.animation.AnimationTimer;
import userinterface.GameFrame;

/**
 * Responsible for updating and drawing the game at a constant rate. It also keeps track of the current fps.
 */
public class GameLoop extends AnimationTimer
{
    private long lastFpsTime = 0;
    private long lastLoopTime = System.nanoTime();
    private int fps = 0;
	private GameFrame gameFrame;

	/**
	 * Initialices the 'Gameloop'
	 * @param gameFrame The 'userinterface.GameFrame to update
     */
    public GameLoop(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
    }

	/**
	 * The function called for every interval. Holds the code that updates and draws the game every frame.
	 * @param now
     */
	@Override
	public void handle(long now)
    {
		//Calclate the time the previous rendering has taken
		float updateLength = now - lastLoopTime;
		lastLoopTime = now;

		// update the frame counter
		lastFpsTime += updateLength;
		fps++;

		// update our FPS counter if a second has passed since
		// we last recorded
		if (lastFpsTime >= 1000000000)
		{
			//System.out.println("(FPS: "+fps+")");
			lastFpsTime = 0;
			fps = 0;
		}

		// update the gameFrame logic
		gameFrame.update(updateLength);
		// draw everyting
		gameFrame.draw();
	}

	public int getFps() {
		return fps;
	}
}