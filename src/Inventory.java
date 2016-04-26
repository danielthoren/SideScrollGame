import java.util.ArrayList;
import java.util.List;

/**
 * The inventory of a player
 */
public class Inventory {

    private Player player;
    private List<SquareInventoryItem> inventoryItemList;
    private int equippedItemIndex;
    private int occupiedInventorySlots;
    private int maxInventorySize;

    /**
     * The inventory of a player.
     * @param player The player to whom the inventory belongs.
     */
    public Inventory(Player player) {
        this.player = player;

        inventoryItemList = new ArrayList<SquareInventoryItem>(4);
        //The size of the current inventory
        occupiedInventorySlots = 0;
        //Default value
        maxInventorySize = 4;
        //WHen value is -1 there is no equipped item
        equippedItemIndex = -1;
    }

    /**
     * Cycles through the inventory items, equipping the one next in the list.
     */
    public void cycle (){
        if (occupiedInventorySlots > 0) {
            equippedItemIndex = (equippedItemIndex + 1) % occupiedInventorySlots;
            inventoryItemList.get(equippedItemIndex).equip();
        }
    }

    /**
     * Adds item and equipps it if the inventory is not full.
     * @param inventoryItem The item to be added to the inventory.
     */
    public boolean addItem(SquareInventoryItem inventoryItem){
        if (occupiedInventorySlots < maxInventorySize) {
            if (equippedItemIndex != -1) {
                inventoryItemList.get(equippedItemIndex).unEquip();
            }
            inventoryItemList.add(inventoryItem);
            inventoryItem.pickUp(player);
            equippedItemIndex = inventoryItemList.indexOf(inventoryItem);
            return true;
        }
        return false;
    }

    public void dropCurrentItem(){
        if (equippedItemIndex != -1) {
            inventoryItemList.get(equippedItemIndex).drop();
            inventoryItemList.remove(equippedItemIndex);
            equippedItemIndex = -1;
        }
    }

    public int getOccupiedInventorySlots() {return occupiedInventorySlots;}

    public void setOccupiedInventorySlots(int occupiedInventorySlots) {this.occupiedInventorySlots = occupiedInventorySlots;}
}
