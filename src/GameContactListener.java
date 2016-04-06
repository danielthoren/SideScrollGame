import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.collision.Manifold;

public class GameContactListener implements ContactListener
{

    private int n = 0;

    /**
     * Called when two fixtures begin to touch.
     * @param contact
     */
    public void beginContact(Contact contact){

	System.out.println(Integer.toString(++n) + " " + Boolean.toString(contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()));

	if (contact.getFixtureA().getBody().getUserData() instanceof Player && contact.getFixtureA().isSensor()){
	    Player player = (Player) contact.getFixtureA().getBody().getUserData();
	    player.isAirBorne = false;
	    System.out.print("player on ground:      ");
	}
	if (contact.getFixtureB().getBody().getUserData() instanceof Player && contact.getFixtureB().isSensor()){
	    Player player = (Player) contact.getFixtureB().getBody().getUserData();
	    player.isAirBorne = false;
	    System.out.println("player on ground");
	}
    }

    /**
     * Called when two fixtures cease to touch.
     * @param contact
     */
    public void endContact(Contact contact){
	if (contact.getFixtureA().getBody().getUserData() instanceof Player && contact.getFixtureA().isSensor()){
	    Player player = (Player) contact.getFixtureA().getBody().getUserData();
	    player.isAirBorne = true;
	    System.out.println("player in air");
	}
	if (contact.getFixtureB().getBody().getUserData() instanceof Player && contact.getFixtureB().isSensor()){
	    Player player = (Player) contact.getFixtureB().getBody().getUserData();
	    player.isAirBorne = true;
	    System.out.println("player in air");
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
    	 * @param contact
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
   	 * @param contact
   	 * @param impulse this is usually a pooled variable, so it will be modified after
   	 * this call
   	 */
   	public void postSolve(Contact contact, ContactImpulse impulse){

	}

}
