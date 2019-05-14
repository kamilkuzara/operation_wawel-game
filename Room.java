import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Operation Wawel" application.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighbouring room.
 * 
 * @author  Michael Kölling, David J. Barnes, Kamil Kuzara
 * @version 2018.11
 */
public class Room 
{
    private String description;
    private HashMap<String, Room> exits;            // stores exits of this room
    private HashMap<String, Boolean> exitStates;    // stores information about exits
                                                    // (open - true; closed - false)
    
    private HashSet<Item> items;                    // items that are in the room

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        exitStates = new HashMap<>();
        items = new HashSet<>();
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit.
     * @param neighbour  The room to which the exit leads.
     * @param state Current state of the exit door.
     */
    public void setExit(String direction, Room neighbor, boolean state) 
    {
        exits.put(direction, neighbor);
        exitStates.put(direction, state);
    }

    /**
     * Check if the given exit is open.
     * 
     * @param String the exit's direction.
     * @return boolean return TRUE if the exit is open, FALSE otherwise
     */
    public boolean isExitOpen(String direction)
    {
        return exitStates.get(direction);
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Open all exits of the room.
     */
    public void openExits()
    {
        Set<String> keys = exitStates.keySet();
        exitStates = new HashMap<>();   // create a new map to hold the exits' states
        for(String key : keys)
        {
            exitStates.put(key, true);
            
            //open the other room linked by the exit
            Room room = exits.get(key);
            String direction = getOppositeDirection(key);            
            room.openExit(direction);
        }
        System.out.println("You have opened all the exits in the room.");
    }
    
    /**
     * Open the specified exit of the room.
     * 
     * @param String the direction in which the door should be opened
     */
    public void openExit(String direction)
    {
        exitStates.remove(direction);
        exitStates.put(direction, true);
    }
    
    /**
     * Get an opposite direction to the one specified.
     */
    private String getOppositeDirection(String direction)
    {
        if(direction.equals("north")){
            return "south";
        }
        else if(direction.equals("south")){
            return "north";
        }
        else if(direction.equals("east")){
            return "west";
        }
        else if(direction.equals("west")){
            return "east";
        }
        else if(direction.equals("up")){
            return "down";
        }
        else if(direction.equals("down")){
            return "up";
        }
        else{   // if the direction is none of the above
            return null;
        }
    }
    
    // methods to manipulate items that are in the room:
    
    /**
     * Put the item in the room (f.e. when it is dropped by a character, 
     * when new item is created and has to be assigned to some room).
     * I.e. add an item to the list of items stored in the room.
     * 
     * @param Item item that is added
     */
    public void addItem(Item item)
    {
        items.add(item);
    }
    
    /**
     * Get the specified item.
     * 
     * @param String name of the item to return
     * @return Item return item if there is one with the name "itemName", null otherwise
     */
    public Item getItem(String itemName) 
    {
        for(Item item : items)
        {
            String currentItemName = item.getName();
            if(itemName.equals(currentItemName)){
                return item;
            }
        }
        return null;    // if we get here that means there is no item
                        // with the name "itemName"
    }
    
    /**
     * Remove the item from the room (f.e. when it gets collected by a character), 
     * i.e remove it from the list of items stored in the room.
     * 
     * @param String name of the item to be removed from the list
     */
    public void removeItem(String itemName)     // when invoking this method one must make sure the item
                                                // that is removed from the room is assigned to other object
                                                // (a character or a chest), otherwise the item 
                                                // will be lost from the game
    {
        for(Item item : items)  // go through all items trying to find the one to remove
        {
            String currentItemName = item.getName();
            if(itemName.equals(currentItemName)){
                items.remove(item);
                return;
            }
        }
    }
    
    /**
     * Print all the items there are in the room to System.out.
     */
    public void listItems() 
    {
        if(items.isEmpty())
            System.out.println("The room is empty.");
        else{
            System.out.println("Contents of the room:");
            for(Item item: items) {
                String itemName = item.getName();
                System.out.print(itemName + "  ");
            }
            System.out.println();
        }
    }
}