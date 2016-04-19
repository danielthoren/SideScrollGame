import jdk.internal.util.xml.impl.Input;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the different values of the map. Having more than one instance of the map may be importaint in the future when
 * multiple maps may be needed for one session (doors and such).
 */
public class Map
{
    private List<DrawAndUpdateObject> drawAndUpdateObjectList;
    private List<InputListener> inputListenerList;
    private List<CollisionListener> collisionListenerList;

    private List<DrawAndUpdateObject> drawANdUpdateObjectsStagedForRemoval;
    private List<InputListener> inputListenersStagedForRemoval;
    private List<CollisionListener> collisionListenersStagedForRemoval;
    private List<Body> bodiesStagedForRemoval;

    private World world;

    private Vec2 gravity;

    public Map(World world, List<DrawAndUpdateObject> drawAndUpdateObjectList, List<InputListener> gameObjectsListen, List<CollisionListener> gameObjectsCollision, Vec2 gravity) {
        this.drawAndUpdateObjectList = drawAndUpdateObjectList;
        this.collisionListenerList = gameObjectsCollision;
        this.inputListenerList = gameObjectsListen;
        this.gravity = gravity;
        this.world = world;
        drawANdUpdateObjectsStagedForRemoval = new ArrayList<DrawAndUpdateObject>(4);
        inputListenersStagedForRemoval = new ArrayList<InputListener>(4);
        collisionListenersStagedForRemoval = new ArrayList<CollisionListener>(4);
        bodiesStagedForRemoval = new ArrayList<Body>(4);
    }

    /**
     * Removes objects staged for removal and clears the 'StagedForRemoval' lists
     */
    public void removeStagedOBjects (){
        //Removing all of the 'DrawAndUpdate' objects from the maps global list
        for (DrawAndUpdateObject object : drawANdUpdateObjectsStagedForRemoval){
            drawAndUpdateObjectList.remove(object);
        }
        //Removing all of the 'InputListener' objects from the maps global list
        for (InputListener listener : inputListenersStagedForRemoval){
            inputListenerList.remove(listener);
        }
        //Removing all of the 'CollisionListener' objects from the maps global list
        for (CollisionListener collisionListener : collisionListenersStagedForRemoval){
            collisionListenerList.remove(collisionListener);
        }
        //Destroying all of the bodies that are staged for removal
        for (Body body : bodiesStagedForRemoval){
            world.destroyBody(body);
        }
        //Clearing all of the 'StagedForRemoval' lists
        bodiesStagedForRemoval.clear();
        drawANdUpdateObjectsStagedForRemoval.clear();
        inputListenersStagedForRemoval.clear();
        collisionListenersStagedForRemoval.clear();
}

    public void removeBody(Body body){bodiesStagedForRemoval.add(body);}

    public void removeCollisionListener(CollisionListener listener){
        collisionListenerList.remove(listener);
    }

    public void removeInputListener(InputListener listener){
        inputListenerList.remove(listener);
    }

    public void removeDrawAndUpdateObject(DrawAndUpdateObject object){
        drawAndUpdateObjectList.remove(object);
    }

    public List<DrawAndUpdateObject> getDrawAndUpdateObjectList() {return drawAndUpdateObjectList;}

    public List<InputListener> getInputListenerList() {
        return inputListenerList;
    }

    public List<CollisionListener> getCollisionListenerList() {return collisionListenerList;}

    public Vec2 getGravity() {return gravity;}

    public World getWorld() {return world;}

    public void addDrawAndUpdateObject(DrawAndUpdateObject object) {drawANdUpdateObjectsStagedForRemoval.add(object);}

    public void addInputListener(InputListener object) {
        inputListenerList.add(object);
    }

    public void addCollisionListener(CollisionListener object) {
        collisionListenerList.add(object);
    }

    public List<DrawAndUpdateObject> getDrawANdUpdateObjectsStagedForRemoval() {return drawANdUpdateObjectsStagedForRemoval;}
}
