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
    private List<CollisionListener> gameObjectsCollision;

    private Vec2 gravity;

    public Map(List<DrawAndUpdateObject> gameObjects, List<InputListener> gameObjectsListen, List<CollisionListener> gameObjectsCollision, Vec2 gravity) {
        this.gameObjects = gameObjects;
        this.gameObjectsCollision = gameObjectsCollision;
        this.gameObjectsListen = gameObjectsListen;
        this.gravity = gravity;
    }

    public List<DrawAndUpdateObject> getGameObjects() {
        return gameObjects;
    }

    public List<InputListener> getGameObjectsListen() {
        return gameObjectsListen;
    }

    public List<CollisionListener> getGameObjectsCollision() {return gameObjectsCollision;}

    public Vec2 getGravity() {return gravity;}

    public void removeSolidObject(Object object){
        gameObjects.remove(object);
    }
}
