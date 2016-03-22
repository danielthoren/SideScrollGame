import jdk.internal.util.xml.impl.Input;

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

    public Map(List<DrawAndUpdateObject> gameObjects, List<InputListener> gameObjectsListen) {
	this.gameObjects = gameObjects;
	this.gameObjectsListen = gameObjectsListen;
    }

    public List<DrawAndUpdateObject> getGameObjects() {
	return gameObjects;
    }

    public List<InputListener> getGameObjectsListen() {
	return gameObjectsListen;
    }
}
