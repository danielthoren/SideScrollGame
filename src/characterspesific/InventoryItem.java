package characterspesific;

import gamelogic.Draw;
import gamelogic.Update;
import gameobjects.Player;

/**
 * Interface of every item that can be picked up.
 *
 * !OBS: For the moment theese functions are only used in one class but the ability to add more is crusial. The interface is used in
 * the 'Inventory' class.
 */
public interface InventoryItem extends Update, Draw
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
     * @param player The player that picks up the item.
     */
    void pickUp(Player player);

    /**
     * Method for dropping the item
     */
    void drop();
}
