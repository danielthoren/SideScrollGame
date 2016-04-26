package gamelogic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.collision.Manifold;

import java.util.List;

/**
 * The contactlistener iterates over all of the objects implementing the 'gamelogic.CollisionListener' interface, informing them
 * of the collision if they have collided.
 */
public class ContactListenerGame implements ContactListener
{
    /**
     * Called when two fixtures begin to touch.
     * @param contact An object containing information about the collision.
     */
    public void beginContact(Contact contact) {
	List<CollisionListener> listeners = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getCollisionListenerList();
	for (CollisionListener obj : listeners){
	    if (contact.getFixtureA().getBody().getUserData().equals(obj) || contact.getFixtureB().getBody().getUserData().equals(obj)){
		obj.beginContact(contact);
	    }
	}
    }

    /**
     * Called when two fixtures cease to touch.
     * @param contact An object containing information about the collision.
     */
    public void endContact(Contact contact){
	List<CollisionListener> listeners = LoadMap.getInstance().getMap(GameComponent.getCurrentMapNumber()).getCollisionListenerList();
	for (CollisionListener obj : listeners){
	    if (contact.getFixtureA().getBody().getUserData().equals(obj) || contact.getFixtureB().getBody().getUserData().equals(obj)){
		obj.endContact(contact);
	    }
	}
    }

    /**
    	 * This is called after a contact is updated. This allows you to inspect a
    	 * contact before it goes to the solver. If you are careful, you can modify the
    	 * contact manifold (e.g. disable contact).
    	 * A copy of the old manifold is provided so that you can detect changes.
    	 * Note: this is called only for awake bodies.
    	 * Note: this is called even when the number of contact points is zero.
    	 * Note: this is not called for sensors.
    	 * Note: if you set the number of contact points to zero, you will not
    	 * get an EndContact callback. However, you may get a BeginContact callback
    	 * the next step.
    	 * Note: the oldManifold parameter is pooled, so it will be the same object for every callback
    	 * for each thread.
    	 * @param contact An object containing information about the collision.
    	 * @param oldManifold
    	 */
    public void preSolve(Contact contact, Manifold oldManifold){

    }

    /**
   	 * This lets you inspect a contact after the solver is finished. This is useful
   	 * for inspecting impulses.
   	 * Note: the contact manifold does not include time of impact impulses, which can be
   	 * arbitrarily large if the sub-step is small. Hence the impulse is provided explicitly
   	 * in a separate data structure.
   	 * Note: this is only called for contacts that are touching, solid, and awake.
   	 * @param contact An object containing information about the collision.
   	 * @param impulse this is usually a pooled variable, so it will be modified after
   	 * this call
   	 */
   	public void postSolve(Contact contact, ContactImpulse impulse){

	}

}
