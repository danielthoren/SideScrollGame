package gamelogic;

/**
 * All objects that will be added in any of the gameworld lists (the lists contained in the map) must implement this interface.
 * The iD of each object is used to safely remove items while not iterating through a list of any kind.
 */
public interface GameObject
{
    /**
     * returns the individual iD for the specific object.
     * @return int iD
     */
    long getId();
}
