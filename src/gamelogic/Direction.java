package gamelogic;

/**
 * Created by daniel on 2016-03-31.
 */

/**
 * This is the directions of the player.
 */
public enum Direction {
    /**
     * UP is jump, DOWN is used for the sensor so we know when the player is grounded,
     * LEFT is the direction left for movement and sensor, RIGHT is the direction right
     * for movement and sensor. NONE is the default direction when we are standing still.
     */
    UP, DOWN, LEFT, RIGHT, NONE
}
