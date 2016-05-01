package characterspesific;

import gameobjects.Player;
import gamelogic.DrawAndUpdateObject;

public interface InventoryItem extends DrawAndUpdateObject
{
    /**
     * Method to equip the item on the current player.
     */
    void equip();

    /**
     * Method for unequiping the item on the current player
     */
    void unEquip();

    /**
     * Method that makes the player pick up the item
<<<<<<< HEAD
     * @param player The player that picks up the item.
=======
     * @param player The player that picks up the item
>>>>>>> Develop
     */
    void pickUp(Player player);

    /**
     * Method for dropping the item
     */
    void drop();
}
