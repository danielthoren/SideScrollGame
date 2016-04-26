package characterspesific;

import gameobjects.Player;
import org.jbox2d.common.Vec2;

/**
 * Handles the default jump behaviour, applying vector underneath object.
 */
public class DefaultJumpHandler implements JumpHandler {

    /**
     * Creates a default characterspesific.JumpHandler, the default characterspesific.JumpHandler takes no arguments except for in the jump function.
     */
    public DefaultJumpHandler() {
    }
    /**
     * The jump function that must be called from the object that is supposed to jump.
     */
    public void jump(Player player) {
        if (player.getGrounded()) {
            float impulse = player.getBody().getMass() * 5;
            player.getBody().applyLinearImpulse(new Vec2(0f, -impulse), player.getBody().getWorldCenter());
        }
    }
}
