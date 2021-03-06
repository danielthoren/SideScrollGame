package gameobjects;

import gamelogic.GameComponent;
import gamelogic.LoadMap;
import gamelogic.Update;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;



/**
 * This class creates a movingplatform.
 */
public class MovingPlatform extends Square implements Update{
    private Vec2 startPos;                  //The start posistion of the platform
    private Vec2 endPos;                    // The position where the platform should return
    private Vec2 positiveVelocity;          // The velocity of the platform in the positive direction
    private Vec2 negativeVelocity;          // The velocity of the platform in the negative direction
    private float platformSpeed;            // The speed which the platform should travel.


    /**
     * Creates a square that will become a moving platform.
     * @param world The game world
     * @param pos  The start position
     * @param friction The friction on the platform
     * @param image   The image texture on the platform
     * @param endPos  The end posistion
     */
    public MovingPlatform(long objectID, World world, Vec2 pos, float friction, Image image, Vec2 endPos) {
        super(objectID, world, pos, friction, image);
        startPos = pos;
        this.endPos = endPos;
        platformSpeed = 1;
        calculateVelocity();
        makeBodyKinematic();
    }

    /**
     * Creates a square that will become a moving platform.
     * @param world The game world
     * @param startPos  The posistion where tha platform is creates (start position)
     * @param friction The friction on the platform
     * @param color  The collor of the platform
     * @param size The size of the platform
     * @param endPos The posistion where the platform should return
     */
    public MovingPlatform(long objectID, World world, Vec2 startPos, float friction, Color color, Vec2 size, Vec2 endPos) {
        super(objectID, world, startPos, friction, color, size);
        this.startPos = startPos;
        this.endPos = endPos;
        platformSpeed = 1;   //The platform speed is set to 1m/s
        calculateVelocity();
        makeBodyKinematic();
    }

    /**
     * Makes the body kinematic.
     */
    private void makeBodyKinematic(){
        body.setType(BodyType.KINEMATIC);
    }

    /**
     * This method makes the platform you could say.
     * Depending on the position of the platform and the x values the platforms velocity is set
     * so it moves in the right direction.
     */
    @Override
    public void update(){
        //If endpos.x == startpos.x the speed is set depending on the y-position. Otherwise the velicity will never change
        //direction and the platform will continue endlessly.
        if (endPos.x == startPos.x){
            if (body.getPosition().y <= startPos.y){
                body.setLinearVelocity(positiveVelocity);
            }
            else if (body.getPosition().y >= endPos.y){
                body.setLinearVelocity(negativeVelocity);
            }
        }
        else if (body.getPosition().x <= startPos.x) {
            body.setLinearVelocity(positiveVelocity);
        }
        else if (body.getPosition().x >= endPos.x){
            body.setLinearVelocity(negativeVelocity);
        }
    }

    /**
     * This method calculates the velocity in each direction (x and y) so the speed
     * is equal to the platform speed.
     */
    private void calculateVelocity() {
        //If endpos.x == startpos.x it means that the platform will only move
        //in the y-direction. Therefore setting this speed in a different if-statement
        // so we dont get division by zero.
        //!OBS: if startpos.x == startpos.y then there is no xVelocity, thus it is set to zero.
        if (endPos.x - startPos.x == 0) {
            Double xVelocity = 0d;
            Double yVelocity = platformSpeed * Math.sin(Math.PI/2);
            positiveVelocity = new Vec2(xVelocity.floatValue(), yVelocity.floatValue());
            negativeVelocity = new Vec2(-xVelocity.floatValue(), -yVelocity.floatValue());
        }
        // Calculates the speed of the platform in the x and y direction.
        else {
            double alpha = Math.atan((endPos.y - startPos.y) / (endPos.x - startPos.x));
            Double xVelocity = platformSpeed * Math.cos(alpha);
            Double yVelocity = platformSpeed * Math.sin(alpha);
            positiveVelocity = new Vec2(xVelocity.floatValue(), yVelocity.floatValue());
            negativeVelocity = new Vec2(-xVelocity.floatValue(), -yVelocity.floatValue());
        }
    }

    /**
     * Sets the platform speed
     * @param speed the speed in m/s
     */
    public void setPlatformSpeed(float speed){
        this.platformSpeed = speed;
        calculateVelocity();
    }

}
