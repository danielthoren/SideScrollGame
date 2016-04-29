package gamelogic;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.Iterator;
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

    private List<DrawAndUpdateObject> drawAndUpdateObjectsStagedForAddition;
    private List<InputListener> inputListenersStagedForAddition;
    private List<CollisionListener> collisionListenersStagedForAddition;

    private World world;

    private Vec2 gravity;

    public Map(World world, List<DrawAndUpdateObject> drawAndUpdateObjectList, List<InputListener> gameObjectsListen, List<CollisionListener> gameObjectsCollision, Vec2 gravity) {
        this.drawAndUpdateObjectList = drawAndUpdateObjectList;
        this.collisionListenerList = gameObjectsCollision;
        this.inputListenerList = gameObjectsListen;
        this.gravity = gravity;
        this.world = world;
        drawANdUpdateObjectsStagedForRemoval = new ArrayList<DrawAndUpdateObject>(2);
        inputListenersStagedForRemoval = new ArrayList<InputListener>(2);
        collisionListenersStagedForRemoval = new ArrayList<CollisionListener>(2);
        bodiesStagedForRemoval = new ArrayList<Body>(2);
        drawAndUpdateObjectsStagedForAddition = new ArrayList<DrawAndUpdateObject>(2);
        inputListenersStagedForAddition = new ArrayList<InputListener>(2);
        collisionListenersStagedForAddition = new ArrayList<CollisionListener>(2);
    }

    /**
     * Removes objects staged for removal and clears the 'StagedForRemoval' lists
     */
    public void removeStagedOBjects (){
        //Removing all of the 'DrawAndUpdate' objects from the maps global list
        for (DrawAndUpdateObject objectRemove : drawANdUpdateObjectsStagedForRemoval){
            for (Iterator<DrawAndUpdateObject> iterator = drawAndUpdateObjectList.iterator(); iterator.hasNext();){
                DrawAndUpdateObject object = iterator.next();
                if (objectRemove.getID() == object.getID()){
                    iterator.remove();
                }
            }
        }
        //Removing all of the 'gamelogic.InputListener' objects from the maps global list
        for (InputListener listenerRemova : inputListenersStagedForRemoval){
            for (Iterator<InputListener> iterator = inputListenerList.iterator(); iterator.hasNext();) {
                InputListener inputListener = iterator.next();
                if (inputListener.getID() == listenerRemova.getID()){
                    iterator.remove();
                }
            }
        }
        //Removing all of the 'gamelogic.CollisionListener' objects from the maps global list
        for (CollisionListener collisionListenerRemove : collisionListenersStagedForRemoval){
            for (Iterator<CollisionListener> iterator = collisionListenerList.iterator(); iterator.hasNext();){
                CollisionListener collisionListener = iterator.next();
                if (collisionListener.getID() == collisionListenerRemove.getID()) {
                    iterator.remove();
                }
            }
        }
        //Destroying all of the bodies that are staged for removal
        for (Body body : bodiesStagedForRemoval){
            if (body != null) {
                world.destroyBody(body);
            }
            else {
                System.out.println("null body");
            }
        }
        //Clearing all of the 'StagedForRemoval' lists
        bodiesStagedForRemoval.clear();
        drawANdUpdateObjectsStagedForRemoval.clear();
        inputListenersStagedForRemoval.clear();
        collisionListenersStagedForRemoval.clear();
}

    /**
     * Adding objects staged for addition and clears the 'StagedForAddition' lists
     */
    public void addStagedObjects (){
        //Adding all of the staged 'DrawAndUpdate' objects to the maps global list
        for (DrawAndUpdateObject object : drawAndUpdateObjectsStagedForAddition){
            drawAndUpdateObjectList.add(object);
        }
        //Adding all of the staged 'gamelogic.InputListener' objects to the maps global list
        for (InputListener listener : inputListenersStagedForAddition){
            inputListenerList.add(listener);
        }
        //Adding all of the staged 'gamelogic.CollisionListener' objects to the maps global list
        for (CollisionListener collisionListener : collisionListenersStagedForAddition){
            collisionListenerList.add(collisionListener);
        }
        //Clearing the objects staged for addition
        drawAndUpdateObjectsStagedForAddition.clear();
        inputListenersStagedForAddition.clear();
        collisionListenersStagedForAddition.clear();
    }

    public void removeBody(Body body){bodiesStagedForRemoval.add(body);}

    public void removeCollisionListener(CollisionListener listener){collisionListenersStagedForRemoval.add(listener);}

    public void removeInputListener(InputListener listener){inputListenersStagedForRemoval.add(listener);}

    public void removeDrawAndUpdateObject(DrawAndUpdateObject object){drawANdUpdateObjectsStagedForRemoval.add(object);}

    public void addDrawAndUpdateObject(DrawAndUpdateObject object) {drawAndUpdateObjectsStagedForAddition.add(object);}

    public void addInputListener(InputListener object) { inputListenersStagedForAddition.add(object); }

    public void addCollisionListener(CollisionListener object) { collisionListenersStagedForAddition.add(object); }

    public List<DrawAndUpdateObject> getDrawAndUpdateObjectList() {return drawAndUpdateObjectList;}

    public List<InputListener> getInputListenerList() {
        return inputListenerList;
    }

    public List<CollisionListener> getCollisionListenerList() {return collisionListenerList;}

    public Vec2 getGravity() {return gravity;}

    public World getWorld() {return world;}
}
