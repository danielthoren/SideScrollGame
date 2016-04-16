import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kristiansikiric on 2016-04-16.
 */
public class ScoreBoard implements DrawAndUpdateObject {
    private static ScoreBoard ourInstance = new ScoreBoard();
    private static List<Player> players;

    public static ScoreBoard getInstance() {
        return ourInstance;
    }

    private ScoreBoard() {
        players = new ArrayList<>(2);
    }
    public void update(){

    }

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
    public void addPlayers(Player player){
        players.add(player);
    }
}
