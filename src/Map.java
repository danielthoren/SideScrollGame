import jdk.internal.util.xml.impl.Input;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the different values of the map. Having more than one instance of the map may be importaint in the future when
 * multiple maps may be needed for one session (doors and such).
 */
public class Map
{
    private List<DrawAndUpdateObject> gameObjects;
    private List<InputListener> gameObjectsListen;

    private Vec2 gravity;

    public Map(List<DrawAndUpdateObject> gameObjects, List<InputListener> gameObjectsListen, Vec2 gravity) {
        this.gameObjects = gameObjects;
        this.gameObjectsListen = gameObjectsListen;
        this.gravity = gravity;
    }

    public List<DrawAndUpdateObject> getGameObjects() {
        return gameObjects;
    }

    public List<InputListener> getGameObjectsListen() {
        return gameObjectsListen;
    }

    public Vec2 getGravity() {return gravity;}
}
