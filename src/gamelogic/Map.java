package gamelogic;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Holds the different values of the map. Having more than one instance of the map may be importaint in the future when
 * multiple maps may be needed for one session (doors and such).
 */
public class Map
{
    private List<Draw> drawObjects;
    private List<Update> updateObjects;
    private List<InputListener> inputListenerList;
    private List<CollisionListener> collisionListenerList;

    private List<Draw> drawObjectsStagedForRemoval;
    private List<Update> updateObjectsStagedForRemoval;
    private List<InputListener> inputListenersStagedForRemoval;
    private List<CollisionListener> collisionListenersStagedForRemoval;
    private List<Body> bodiesStagedForRemoval;

    private List<Draw> drawObjectsStagedForAddition;
    private List<Update> updateObjectsStagedForAddition;
    private List<InputListener> inputListenersStagedForAddition;
    private List<CollisionListener> collisionListenersStagedForAddition;

    private World world;

    /**
     * Creates an instance of 'Map' wich is a container for all of the world objects. It also contains an abstractionlayer
     * for removing and adding new objects to the world, preventing the program from crashing.
     * @param world The world containing the bodies of the starting objects.
     * @param drawObjects The objects implementing the 'Draw' interface.
     * @param updateObjects The objects implementing the 'Update' interface.
     * @param gameObjectsListen The objects implementing the 'InputListener' interface.
     * @param gameObjectsCollision The objects implementing the 'CollisionListener' interface.
     */
    public Map(World world, List<Draw> drawObjects, List<Update> updateObjects, List<InputListener> gameObjectsListen, List<CollisionListener> gameObjectsCollision) {
        this.drawObjects = drawObjects;
        this.updateObjects = updateObjects;
        this.collisionListenerList = gameObjectsCollision;
        this.inputListenerList = gameObjectsListen;
        this.world = world;
        drawObjectsStagedForRemoval = new ArrayList<>(2);
        updateObjectsStagedForRemoval = new ArrayList<>(2);
        inputListenersStagedForRemoval = new ArrayList<>(2);
        collisionListenersStagedForRemoval = new ArrayList<>(2);
        bodiesStagedForRemoval = new ArrayList<>(2);
        drawObjectsStagedForAddition = new ArrayList<>(2);
        updateObjectsStagedForAddition = new ArrayList<>(2);
        inputListenersStagedForAddition = new ArrayList<>(2);
        collisionListenersStagedForAddition = new ArrayList<>(2);
    }

    /**
     * Removes objects staged for removal and clears the 'StagedForRemoval' lists. Theese lists are a buffer to prevent
     * instantanious removal of objects during runtime, if objects were removed during iteration over lists containing theese
     * objects or during the world step then the program would crash.
     */
    public void removeStagedOBjects (){
        //List used to keep track of wich id:s bodies have been removed. Used to prevent multiple removals of the same body.
        //If a item that will be staged for removal when collision occurs with a type of object then two such bodies will
        //be added to the 'stagedForRemoval' if the item collides with two objects of said type between one step.
        Collection<Long> bodyIDRemoved = new ArrayList<>();
        //Destroying all of the bodies that are staged for removal
        for (Body body : bodiesStagedForRemoval){
            if (!bodyIDRemoved.contains(((GameObject)body.getUserData()).getId())) {
                bodyIDRemoved.add(((GameObject)body.getUserData()).getId());
                world.destroyBody(body);
            }
        }
        //Removing all of the 'Draw' objects from the maps global list
        for (Draw objectRemove : drawObjectsStagedForRemoval){
            for (Iterator<Draw> iterator = drawObjects.iterator(); iterator.hasNext();){
                Draw object = iterator.next();
                if (objectRemove.getId() == object.getId()){
                    iterator.remove();
                }
            }
        }
        //Removing all of the 'Update' objects from the maps global list
        for (Update objectRemove : updateObjectsStagedForRemoval){
            for (Iterator<Update> iterator = updateObjects.iterator(); iterator.hasNext();){
                Update object = iterator.next();
                if (objectRemove.getId() == object.getId()){
                    iterator.remove();
                }
            }
        }
        //Removing all of the 'gamelogic.InputListener' objects from the maps global list
        for (InputListener listenerRemova : inputListenersStagedForRemoval){
            for (Iterator<InputListener> iterator = inputListenerList.iterator(); iterator.hasNext();) {
                InputListener inputListener = iterator.next();
                if (inputListener.getId() == listenerRemova.getId()){
                    iterator.remove();
                }
            }
        }
        //Removing all of the 'gamelogic.CollisionListener' objects from the maps global list
        for (CollisionListener collisionListenerRemove : collisionListenersStagedForRemoval){
            for (Iterator<CollisionListener> iterator = collisionListenerList.iterator(); iterator.hasNext();){
                CollisionListener collisionListener = iterator.next();
                if (collisionListener.getId() == collisionListenerRemove.getId()) {
                    iterator.remove();
                }
            }
        }
        //Clearing all of the 'StagedForRemoval' lists
        bodiesStagedForRemoval.clear();
        updateObjectsStagedForRemoval.clear();
        drawObjectsStagedForRemoval.clear();
        inputListenersStagedForRemoval.clear();
        collisionListenersStagedForRemoval.clear();
}

    /**
     * Adding objects staged for addition and clears the 'StagedForAddition' lists. Theese lists are a buffer to prevent
     * instantanious addition of objects during runtime, if objects were removed during iteration over lists containing theese
     * objects or during the world step then the program would crash.
     */
    public void addStagedObjects (){
        //Adding all of the staged 'Draw' objects to the maps global list
        for (Draw object : drawObjectsStagedForAddition){
            drawObjects.add(object);
        }
        //Adding all of the staged 'Update' objects to the maps global list
        for (Update object : updateObjectsStagedForAddition){
            updateObjects.add(object);
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
        drawObjectsStagedForAddition.clear();
        updateObjectsStagedForAddition.clear();
        inputListenersStagedForAddition.clear();
        collisionListenersStagedForAddition.clear();
    }

    public void removeBody(Body body){bodiesStagedForRemoval.add(body);}

    public void removeCollisionListener(CollisionListener listener){collisionListenersStagedForRemoval.add(listener);}

    public void removeInputListener(InputListener listener){inputListenersStagedForRemoval.add(listener);}

    public void removeDrawObject(Draw object){drawObjectsStagedForRemoval.add(object);}

    public void removeUpdateObject(Update object){updateObjectsStagedForRemoval.add(object);}

    public void addDrawObject(Draw object) {drawObjectsStagedForAddition.add(object);}

    public void addUpdateObject(Update object) {updateObjectsStagedForAddition.add(object);}

    public void addInputListener(InputListener object) { inputListenersStagedForAddition.add(object);}

    public void addCollisionListener(CollisionListener object) { collisionListenersStagedForAddition.add(object);}

    public List<Draw> getDrawObjects() {return drawObjects;}

    public List<Update> getUpdateObjects() {return updateObjects;}

    public Iterable<InputListener> getInputListenerList() {return inputListenerList;}

    public Iterable<CollisionListener> getCollisionListenerList() {return collisionListenerList;}

    public World getWorld() {return world;}
}
