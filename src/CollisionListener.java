import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public interface CollisionListener
{
    public void beginContact(Contact contact);
    public void endContact(Contact contact);
}
