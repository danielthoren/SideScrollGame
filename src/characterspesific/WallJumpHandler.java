package characterspesific;

import gameobjects.Player;
import org.jbox2d.common.Vec2;

/**
 * Enabling the player to jump against walls
 */
public class WallJumpHandler implements JumpHandler {

    private DefaultJumpHandler defaultJumpHandler;

    /**
     * Creates a character specific.WallJumpHandler, the default character specific. JumpHandler takes no arguments except for in the jump function.
     */
    public WallJumpHandler() {
        defaultJumpHandler = new DefaultJumpHandler();
    }
    /**
     * The jump function that must be called from the object that is supposed to jump.
     */
    public void jump(Player player) {
        if (player.getGrounded()) {
            defaultJumpHandler.jump(player);
        }
        if (player.getCollisionLeft() || player.getCollisionRight()){
            Vec2 velocity = player.getBody().getLinearVelocity();
            float impulseY = player.getBody().getMass() * 5;
            float impulseX = player.getBody().getMass() * 3;
            if (velocity.x < 0){
                player.getBody().applyLinearImpulse(new Vec2(impulseX, -impulseY), player.getBody().getWorldCenter());
            }
            else{
                player.getBody().applyLinearImpulse(new Vec2(-impulseX, -impulseY), player.getBody().getWorldCenter());
            }
        }
    }
}
