import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kristiansikiric on 2016-04-16.
 */

/**
 * This singelton draws the score of the players.
 */
public class ScoreBoard implements DrawAndUpdateObject {
    private static ScoreBoard ourInstance = new ScoreBoard();
    private static List<Player> players; //Holds the players.

    public static ScoreBoard getInstance() {
        return ourInstance;
    }

    private ScoreBoard() {
        players = new ArrayList<>(2);
    }
    public void update(){

    }

    /**
     * Draws the score of each players.
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){
        int x = 100;
        int y = 50;
        int whichPlayer = 1;
        gc.setFill(Color.WHEAT);
        for (Player player : players){
            gc.fillText("Player" + whichPlayer+ " score: " + Integer.toString(player.getScore()), x,y);
            whichPlayer ++;
            y *= 2;
        }
    }

    /**
     * Adds a player to the list that holds all the players.
     * @param player The player to add to the list.
     */
    public void addPlayers(Player player){
        players.add(player);
    }
}
