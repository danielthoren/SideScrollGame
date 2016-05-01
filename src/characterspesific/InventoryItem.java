package characterspesific;

import gameobjects.Player;
import gamelogic.DrawAndUpdateObject;

public interface InventoryItem extends DrawAndUpdateObject
{
    /**
     * Method to equip the item on the current player.
     */
    public void equip();

    /**
     * Method for unequiping the item on the current player
     */
    public void unEquip();

    /**
     * Method that makes the player pick up the item
     * @param player The player that picks up the item
     */
    public void pickUp(Player player);

    /**
     * Method for dropping the item
     */
    public void drop();
}
