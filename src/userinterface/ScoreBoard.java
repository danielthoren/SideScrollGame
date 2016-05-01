package userinterface;

import gamelogic.DrawAndUpdateObject;
import gameobjects.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * This singelton draws the score of the players.
 */
public final class ScoreBoard implements DrawAndUpdateObject
{
    private static ScoreBoard ourInstance = new ScoreBoard();
    private static List<Player> players = new ArrayList<>(2);
    private final static int OBJECT_ID = -1;

    public static ScoreBoard getOurInstance() {
        return ourInstance;
    }

    private ScoreBoard() {}

    public void update(){}

    /**
     * Draws the score of each players.
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){
        int x = 100;    //This is the x-coordinate of the text drawn on the screen.
        int y = 50; // This is the y-coordinate of the text drawn on the screen.
        int whichPlayer = 1;
        gc.setFill(Color.WHEAT);
        for (Player player : players){
            gc.fillText("gameobjects.Player" + whichPlayer+ " score: " + Integer.toString(player.getScore()), x,y);
            whichPlayer ++;
            y *= 2; //Increases the y-coordinate for each player so the text is drawn in a nice line.
        }
    }

    /**
     * Adds a player to the list that holds all the players.
     * @param player The player to add to the list.
     */
    public void addPlayers(Player player){
        players.add(player);
    }

    public long getId(){
        return OBJECT_ID;
    }
}
