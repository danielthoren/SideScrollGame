import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jbox2d.common.Vec2;

/**
 * Class handling a simple sprite. It contains draw-functions for the sprite image.
 */
public class Sprite {

    private Image image;          //Holds the SpriteImage
    private int columns;          //Holds the amount of columns in the image
    private int rows;             //Holds the amount of rows in the image
    private int updateInterval;   //Holds the amount of frames before changing image
    private int numberOfFrames;
    private Animation animation;
    private ImageView imageView;
    private Vec2 currentOffset;
    private final Vec2 spriteWindowSize;
    private int offsetMultiplier;

    public Sprite(Image image, int columns, int rows, int numberOfFrames, int updateInterval) {
        this.columns = columns;
        this.image = image;
        this.rows = rows;
        this.numberOfFrames = numberOfFrames;
        this.updateInterval = updateInterval;
        offsetMultiplier = 1;
        spriteWindowSize = new Vec2((float) image.getWidth() / columns, (float) image.getHeight() / rows);
        imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, spriteWindowSize.x, spriteWindowSize.y));
    }

    public void draw(GraphicsContext gc, Vec2 pos){

    }
}
