import org.jbox2d.common.Vec2;

/**
 * Enabling the player to jump against walls
 */
public class WallJumpHandler implements JumpHandler {

    /**
     * Creates a WallJumpHandler, the default JumpHandler takes no arguments except for in the jump function.
     */
    public WallJumpHandler() {
    }
    /**
     * The jump function that must be called from the object that is supposed to jump.
     */
    public void jump(Player player) {
        if (player.getGrounded()) {
            float impulse = player.body.getMass() * 5;
            player.body.applyLinearImpulse(new Vec2(0f, -impulse), player.body.getWorldCenter());
        }
        else if (player.getLeftCollision() || player.getRightCollision()){
            Vec2 velocity = player.body.getLinearVelocity();
            float impulseY = player.body.getMass() * 5;
            float impulseX = player.body.getMass() * 3;
            if (velocity.x < 0){
                player.body.applyLinearImpulse(new Vec2(impulseX, -impulseY), player.body.getWorldCenter());
            }
            else{
                player.body.applyLinearImpulse(new Vec2(-impulseX, -impulseY), player.body.getWorldCenter());
            }
        }
    }
}
