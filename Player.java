import java.util.ArrayList;

/**
 * Class Player is a part of "Operation Wawel" application.
 * It extends class Character with player's features and behaviours.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Player extends Character
{
    private ArrayList<Room> previousRooms;
    
    // minimum number of saved previous locations
    private static final int MIN_TRACK_LENGTH = 1;
    
    /**
     * Constructor for objects of class Player
     */
    public Player(Room startingRoom)
    {
        super(startingRoom);
        previousRooms = new ArrayList<>();
        previousRooms.add(currentRoom); // add the first room to the track
        maxInjuries = 6;    // overwrite the value from Character's constructor
                            // the player can receive more injuries
    }
    
    /** 
     * Go in the specified direction. Add the current room to
     * the list of previous rooms (tracks the movement for the 
     * use of "back" command).
     * 
     * @param String the direction in which we want to go
     */
    public void goRoom(String direction)
    {
        // leave current room
        Room nextRoom = currentRoom.getExit(direction);
        
        if((previousRooms.size() == MIN_TRACK_LENGTH && currentRoom != nextRoom)
            || previousRooms.size() > MIN_TRACK_LENGTH){
                previousRooms.add(currentRoom);
                // add the current room to the list of previous rooms if the track
                // is longer than one, or the next room is different
                // to the current
        }
        
        changeRoom(direction);
    }
    
    /** 
     * Go back to the previous room. If invoked at the beginning of the game 
     * when there is no previous room to go to, the character will go to the
     * room they are currently in, i.e they will stay in the same room.
     * When invoked a number of times in a row, the character actually moves back
     * along their track.
     */
    public void goBack()
    {
        if(previousRooms.size() > MIN_TRACK_LENGTH){
            // get the last room from the track
            int lastRoom = previousRooms.size() - 1;
            
            // change the room
            currentRoom = previousRooms.get(lastRoom);
            
            previousRooms.remove(lastRoom);
            
            if(injuryCounter > 0){  // no need to count the moves
                                    // to healing if there are no injuries
                movesToHeal++;
                if(canHeal()){
                    heal();     // heal injury
                }
            }
        }
    }
    
    /**
     * Teleport the player to the specified room. The room
     * does not have to be linked to the current room.
     * 
     * @param Room destination to which the player is moved
     * @param ArrayList<Room> the list of rooms to which all the 
     *                          items will be dropped randomly
     */
    public void teleport(Room destination, ArrayList<Room> rooms)
    {
        // change the room
        currentRoom = destination;
        
        // clear the track of previous rooms and add the current one
        previousRooms.clear();
        previousRooms.add(currentRoom);
        
        // drop all items to random locations
        dropAllItems(rooms);
    }
    
    /**
     * Attack the specified character with the specified weapon. 
     * 
     * @param Character the character to be attacked
     * @param String the name of the weapon to be used
     */
    public void attack(Character character, String weaponName)
    {
        Item weapon = getItem(weaponName);
        if(isUseableWeapon(weapon)){    // attack only if the weapon can be used
            attack(character, (Weapon)weapon);
            
            System.out.println("You have attacked the enemy.");
            System.out.println("Number of enemy's injuries: " + character.getInjuriesNumber() + "/"
                                 + character.getMaxInjuriesNumber());
        }
        else{
            System.out.println("This item is not a weapon or is not useable right now.");
        }
    }
    
    /**
     * Collect the specified item. To collect an item check if it actually is held
     * in the "currentRoom". If it is, then add this item to the character's item
     * list, remove it from the current room's item list and increase weight carried
     * by the character. The method also checks if the item does not exceed the maximum
     * weight that the character can carry.
     * 
     * If any of the checks indicate that the item cannot be collected, an appropriate
     * message is printed out on the screen and the method ends without executing the
     * operation of changing the item's holder.
     * 
     * @param String the name of the item to be collected
     */ 
    public void collectItem(String itemName)
    {
        // try to collect specified item
        Item item = currentRoom.getItem(itemName);

        if (item == null) {
            System.out.println("There is no such item in this room!");
        }
        else {
            int itemWeight = item.getWeight();
            if(canLift(itemWeight)){    // only collect if not too heavy
                items.add(item);
                currentRoom.removeItem(itemName);   // we already know we can safely remove
                                                    // the item from the room without losing it,
                                                    // because it has been assigned to the character
                increaseCarriedWeight(itemWeight);
                System.out.println("You have collected " + itemName + ".");
            }
            else
                System.out.println("You cannot collect this item! It weighs too much.");
        }
    }
    
    /**
     * Drop the specified item. Remove it from the list
     * of player's items and place it on the list of items 
     * of the current room.
     * 
     * @param String the name of the item to be dropped
     */
    public void dropItem(String itemName)
    {
        // try to drop specified item
        Item item = getItem(itemName);

        if (item == null) {
            System.out.println("You do not have such an item!");
        }
        else {
            int itemWeight = item.getWeight();
            currentRoom.addItem(item);  // add to the room
            items.remove(item);         // remove from the player
            decreaseCarriedWeight(itemWeight);
            System.out.println("You have dropped " + itemName + ".");
        }
    }
    
    /**
     * Print all the items the character is carrying to System.out.
     */
    public void listItems() 
    {
        if(items.isEmpty())
            System.out.println("You are not carrying any items.");
        else{
            System.out.println("Your inventory: ");
            for(Item item: items) {
                String itemName = item.getName();
                System.out.print(itemName + "  ");
            }
            System.out.println();
        }
    }
}
