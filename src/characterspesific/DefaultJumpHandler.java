package characterspesific;

import gameobjects.Player;
import org.jbox2d.common.Vec2;

/**
 * Handles the default jump behaviour, applying vector underneath object.
 */
public class DefaultJumpHandler implements JumpHandler {

    /**
     * The jump function that must be called from the object that is supposed to jump.
     */
    public void jump(Player player) {
        if (player.getGrounded()) {
            float impulse = player.getBody().getMass() * 5;
            //Setting the x-vector to 0 since jumping does not accelerate in the x-axis
            player.getBody().applyLinearImpulse(new Vec2(0f, -impulse), player.getBody().getWorldCenter());
        }
    }
}
