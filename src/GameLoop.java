public class GameLoop
{
    private long lastFpsTime = 0;
    private long lastLoopTime = System.nanoTime();
    private int fps = 0;
    private boolean gameRunning;
    private GameFrame gameFrame;

    public GameLoop() {
	this.gameRunning = true;
	this.gameFrame = new GameFrame("Game");
    }

    public void gameLoop(final int targetFPS)
    {
	final long optimalTime = 1000000000 / targetFPS;

	// keep looping round til the game ends
	while (gameRunning)
	{
	    // work out how long its been since the last update, this
	    // will be used to calculate how far the entities should
	    // move this loop
	    long now = System.nanoTime();
	    long updateLength = now - lastLoopTime;
	    lastLoopTime = now;
	    double delta = updateLength / ((double)optimalTime);

	    // update the frame counter
	    lastFpsTime += updateLength;
	    fps++;

	    // update our FPS counter if a second has passed since
	    // we last recorded
	    if (lastFpsTime >= 1000000000)
	    {
		System.out.println("(FPS: "+fps+")");
		lastFpsTime = 0;
		fps = 0;
	    }

	    // update the game logic

	    // draw everyting

	    // we want each frame to take 10 milliseconds, to do this
	    // we've recorded when we started the frame. We add 10 milliseconds
	    // to this and then factor in the current time to give
	    // us our final value to wait for
	    // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
	    try{
		Thread.sleep((lastLoopTime-System.nanoTime() + optimalTime)/1000000 );
	    }catch (InterruptedException e){
		Thread.currentThread().interrupt();
		//throw new RuntimeException("Thread interrupt, save and exit!");
	    }

	}
    }
}