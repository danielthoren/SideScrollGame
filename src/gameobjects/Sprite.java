package gameobjects;

import gamelogic.DrawAndUpdateObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jbox2d.common.Vec2;
import gamelogic.GameComponent;

/**
 * Class handling a simple sprite. It contains draw-functions for the sprite image.
 */
public class Sprite implements DrawAndUpdateObject
{

    private final Image image;          //Holds the SpriteImage
    private final int columns;          //Holds the amount of columns in the image
    private int updateInterval;         //Holds the amount of frames before changing image
    private int numberOfFrames;
    private int currFrameNumber;
    private int currFramesSinceUpdate;
    private final long id;
    private Vec2 currentOffset;
    private Vec2 position;
    private Vec2 actualSizeOfSprite;    //Holds an approximate value of the actual imagesize inside of the spriteImages
    private final Vec2 spriteWindowSize;
    private float angle;
    private boolean flip;
    private boolean freeze;

    /**
     * Creates a spriteobject that draws a spritesheet with even spacing.
     * @param image The spriteSheet
     * @param columns The amount of columns in the spritesheet
     * @param rows The amount of rows in the spritesheet
     * @param numberOfFrames The number of frames in the spritesheet
     * @param updateInterval The amount of frames to wait before changing frame
     * @param position The center-position of the sprite in world coordinates
     * @param angle The angle of the sprite
     */
    public Sprite(long id, Image image, int columns, int rows, int numberOfFrames, int updateInterval, Vec2 position, float angle) {
        this.columns = columns;
        this.angle = angle;
        this.image = image;
        this.position = position;
        this.numberOfFrames = numberOfFrames;
        this.updateInterval = updateInterval;
        this.id = id;
        currentOffset = new Vec2(0,0);
        flip = false;
        freeze = false;
        spriteWindowSize = new Vec2((float) image.getWidth() / columns, (float) image.getHeight() / rows);
        actualSizeOfSprite = new Vec2(GameComponent.pixToMeters(spriteWindowSize.x), GameComponent.pixToMeters(spriteWindowSize.y));
        currFramesSinceUpdate = 0;
    }

    /**
     * Updates the current frame, meaning the current column and row.
     */
    public void update(){
        if (!freeze) {
            currFramesSinceUpdate++;
            if (currFramesSinceUpdate > updateInterval) {
                //Checking if the end of the spriteanimation has been reached. If so then reset all parameters.
                if (currFrameNumber >= numberOfFrames - 1) {
                    currentOffset.x = 0;
                    currentOffset.y = 0;
                    currFrameNumber = 0;
                }
                //Checking if the end of the columns has been reached, if so add one to rowOffset and reset columnOffset.
                if (currentOffset.x >= columns - 1) {
                    currentOffset.x = 0;
                    currentOffset.y += 1;
                } else {
                    currentOffset.x++;
                }
                currFramesSinceUpdate = 0;
                currFrameNumber++;
            }
        }
    }

    /**
     * Draws the sprite at the given position. If the 'flip' boolean is true the image is flipped over but stays
     * in the same position.
     * @param gc The GraphicsContext with wich to draw
     */
    public void draw(GraphicsContext gc){
        //Saving the current xy-plane to the gc stack
        gc.save();
        //Translating the original gc xy-plane to a new xy-plane with its origin in the center of this body and saving the
        //new xy-plane on top of the stack
        gc.translate(GameComponent.metersToPix(position.x), GameComponent.metersToPix(position.y));
        //Rotating the top xy-plane of the stack (the one created above) to the current degree of the body
        gc.rotate(Math.toDegrees(angle));
        //Drawing the body so that the center of the visual representation is in the new xy-planes origin

        if (flip){
            gc.drawImage(image, currentOffset.x * spriteWindowSize.x, currentOffset.y * spriteWindowSize.y, spriteWindowSize.x, spriteWindowSize.y,
                    spriteWindowSize.x / 2, -spriteWindowSize.y / 2, -spriteWindowSize.x, spriteWindowSize.y);
        }
        else {
            gc.drawImage(image, currentOffset.x * spriteWindowSize.x, currentOffset.y * spriteWindowSize.y, spriteWindowSize.x, spriteWindowSize.y,
                    -spriteWindowSize.x / 2, -spriteWindowSize.y / 2, spriteWindowSize.x, spriteWindowSize.y);
        }
        //Popping the stack, removing the top element, thus leaving the original xy-plane at the top
        gc.restore();
    }

    /**
     * Freezes the sprite on the specified frame
     * @param column The column of the frame to freeze on
     * @param row The row of the frame to freeze on
     */
    public void freezeOnFrame(int column, int row){
        freeze = true;
        currentOffset.x = column - 1;
        currentOffset.y = row - 1;
        currFrameNumber = (row - 1)*columns + column;
    }

    /**
     * Starts the sprite if it is frozen
     */
    public void startSprite(){
        freeze = false;
    }

    public void setFlip(boolean flip) {this.flip = flip;}

    public long getId() {return id;}

    public void setPosition(Vec2 position) {this.position = position;}

    public Vec2 getActualSizeOfSprite() {return actualSizeOfSprite;}

    public void setActualSizeOfSprite(Vec2 actualSizeOfSprite) {this.actualSizeOfSprite = actualSizeOfSprite;}
}
