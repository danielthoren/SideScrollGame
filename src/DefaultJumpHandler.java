import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Handles the default jump behaviour, applying vector underneath object.
 */
public class DefaultJumpHandler implements JumpHandler {

    /**
     * Creates a default JumpHandler, the default JumpHandler takes no arguments except for in the jump function.
     */
    public DefaultJumpHandler() {
    }
    /**
     * The jump function that must be called from the object that is supposed to jump.
     */
    public void jump(Player player) {
        if (player.getGrounded()) {
            float impulse = player.body.getMass() * 5;
            player.body.applyLinearImpulse(new Vec2(0f, -impulse), player.body.getWorldCenter());
        }
    }
}
