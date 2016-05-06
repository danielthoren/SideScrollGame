package gamelogic;

import javafx.animation.AnimationTimer;

/**
 * Responsible for updating and drawing the game at a constant rate. It also keeps track of the current fps.
 *
 * !OBS Is instantiated in 'GameFrame' and used to update the 'GameFrame'
 */
public class GameLoop extends AnimationTimer
{
	private long lastFpsTime;
	private long lastLoopTime;
	private int fps;
	private int currFps;
	private GameComponent gameComponent;
	private static final long NANOS_PER_SECOND = 1000000000;

	/**
	 * Initialises the 'Gameloop'
	 * @param gameComponent The gameComponent of the game
	 */
	public GameLoop(GameComponent gameComponent) {
		this.gameComponent = gameComponent;
		currFps = 0;
		fps = 0;
		lastFpsTime = 0;
		lastLoopTime = System.nanoTime();
	}

	/**
	 * The function called for every interval. Holds the code that updates and draws the game every frame.
	 * @param now the current time in nanoseconds.
	 */
	@Override
	public void handle(long now)
	{
		//Calculate the time the previous rendering has taken
	/*
	long updateLength = System.nanoTime() - lastLoopTime;
	lastLoopTime = System.nanoTime();
	*/
		long updateLength = now - lastLoopTime;
		lastLoopTime = now;

		// update the frame counter
		lastFpsTime += updateLength;
		fps++;

		// update our FPS counter if a second has passed since
		// we last recorded
		if (lastFpsTime >= NANOS_PER_SECOND)
		{
			System.out.println("(FPS: "+fps+")");
			lastFpsTime = 0;
			currFps = fps;
			fps = 0;
		}
		// update the gameFrame logic
		gameComponent.update(updateLength);
		// draw everything
		gameComponent.draw();
	}

	public int getCurrFps() {
		return currFps;
	}
}