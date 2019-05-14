import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class Character is a part of "Operation Wawel" application.
 * It defines character's features as well as behaviours.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Character
{
    protected Room currentRoom;
    protected HashSet<Item> items;  // stores items carried by the character
    protected int injuryCounter;    // the number of injuries
    protected int maxInjuries;      // maximum number of injuries survivable
    
    // number of moves a character has to perform to heal one injury
    protected static final int HEALING_RATE = 2;  
    
    protected int movesToHeal;      // number of moves after the last healing
    protected int carriedWeight;
    private static final int MAX_WEIGHT = 45;    // maximum weight a character can carry
                                                 // in kilograms, we assume that every 
                                                 // character has the same average
                                                 // maximum weight they can carry
    
    /**
     * Constructor for objects of class Character
     */
    public Character(Room startingRoom)
    {
        currentRoom = startingRoom;
        items = new HashSet<>();
        carriedWeight = 0;      // the character does not hold any items
        injuryCounter = 0;
        maxInjuries = 1;    // standard for every character, may be overwritten by a subclass
        movesToHeal = 0;
    }
    
    /**
     * Return the current location of the character.
     *
     * @return Room return current room, i.e. where the character is
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Change the current location to the one specified by the direction.
     * 
     * @param String direction in which we want to move
     */
    public void changeRoom(String direction)
    {
        Room nextRoom = currentRoom.getExit(direction);
        currentRoom = nextRoom;     // move to the next room
        
        if(injuryCounter > 0){  // no need to count the moves
                                // to healing if there are no injuries
            movesToHeal++;
            if(canHeal()){
                heal();     // heal injury
            }
        }
    }
    
    /**
     * Check if a move is possible.
     * 
     * @param String direction in which we want to move 
     * @return Character.MovePossibility return the possibility state of a move
     */
    public MovePossibility isMovePossible(String direction)
    {
        Room nextRoom = currentRoom.getExit(direction);
        
        // return an appropriate result depending on
        // the exit availability
        if(nextRoom == null){
            return MovePossibility.NO_DOOR;
        }
        else if(!currentRoom.isExitOpen(direction)){
            return MovePossibility.DOOR_CLOSED;
        }
        else{
            return MovePossibility.POSSIBLE;
        }
    }
    
    /**
     * Attack the specified character with the specified weapon.
     * 
     * @param Character the character to be attacked
     * @param Weapon the weapon to be used
     */
    public void attack(Character character, Weapon weapon)
    {
        weapon.use(character);
    }
    
    /**
     * Get a weapon from the list of character's items.
     * Returns a useable weapon that was found first.
     * 
     * @return Weapon useable weapon, no specific one
     */
    public Weapon getWeapon()
    {
        for(Item item : items)
        {
            if(item instanceof Weapon){
                if(isUseableWeapon(item)){
                    return (Weapon)item;
                }
            }
        }
        
        // if we get here, there are no useable weapons in character's inventory
        return null;    
    }
    
    /**
     * Check if the specified item is a useable weapon.
     * 
     * @param Item item to be investigated
     * @return boolean return TRUE if the item is a useable weapon, FALSE otherwise
     */
    protected boolean isUseableWeapon(Item item)
    {
        if(item instanceof Weapon){
            return ((Weapon)item).useable();    // check if it can be used
        }
        else{
            return false;
        }
    }
    
    /**
     * Check if the character is able to attack others.
     * 
     * @return Character.AttackPossibility return the possibility state of an attack
     */
    public AttackPossibility canAttack()
    {
        AttackPossibility state = AttackPossibility.NO_WEAPON;  // predefine a state
        for(Item item : items)
        {
            if(item instanceof Weapon){
                if(isUseableWeapon(item)){
                    return AttackPossibility.POSSIBLE;  // a useable weapon found
                                                        // the character can attack
                }
                else{
                    state = AttackPossibility.NO_BULLETS;   // none of the so-far-found weapons
                                                            // have bullets in them
                }
            }
        }
        return state;
    }
    
    /**
     * Return the current amount of injuries.
     * 
     * @return int return the current number of injuries
     */
    public int getInjuriesNumber()
    {
        return injuryCounter;
    }
    
    /**
     * Return the maximum number of injuries the character can survive.
     * 
     * @return int return the maximum number of injuries
     */
    public int getMaxInjuriesNumber()
    {
        return maxInjuries;
    }
    
    /**
     * Raceive an injury.
     */
    public void receiveInjury()
    {
        injuryCounter++;
    }
    
    /**
     * Heal an injury.
     */
    protected void heal()
    {
        if(injuryCounter > 0){
            injuryCounter--;
        }
        movesToHeal = 0;
    }
    
    /**
     * Check if the character can already heal, or do they
     * have to perform more moves.
     * 
     * @return boolean return TRUE if the character can get healed, FALSE otherwise
     */
    protected boolean canHeal()
    {
        if(movesToHeal == HEALING_RATE){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Check is the character is dead.
     * 
     * @return boolean return TRUE if the character is dead, FALSE otherwise
     */
    public boolean isDead()
    {
        if(injuryCounter > maxInjuries){    // the current number of injuries has to exceed
                                            // the maximum number of injuries for the character
                                            // to be dead
            return true;
        }
        else{
            return false;
        }
    }
    
    // implementation of the commands that manipulate items (f.e. collecting, dropping):
    
    /**
     * Add an item to the list of character's items.
     * Use only to assign items when they are created, 
     * i.e. at the beginning of the game.
     * 
     * @param Item the item to be added
     */
    public void addItem(Item item)
    {
        items.add(item);
        increaseCarriedWeight(item.getWeight());
    }
    
    /**
     * Drop all items the character is carrying.
     * The items are dropped in the current room.
     * Use when the character dies.
     */
    public void dropAllItems()
    {
        // if the character is dead
        // drop their items in the same room
        for(Item item : items)
        {
            currentRoom.addItem(item);
        }
        items.clear();
        carriedWeight = 0;
    }
    
    /**
     * Drop all items the character is carrying.
     * The items are dropped in random rooms.
     * Use when the character is captured.
     * 
     * @param ArrayList<Room> the list of rooms to which the items will be put
     */
    public void dropAllItems(ArrayList<Room> rooms)
    {
        // if the character was captured
        // drop all their items in random rooms
        Random rand = new Random();
        int roomIndex;
        for(Item item : items)
        {
            roomIndex =rand.nextInt(rooms.size());
            Room destinationRoom = rooms.get(roomIndex);
            
            destinationRoom.addItem(item);
        }
        items.clear();
        carriedWeight = 0;
    }
    
    /**
     * Get the specified item.
     * 
     * @param String name of the item that is to be returned
     * @return Item return item if it was found, null otherwise
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
     * Check if the character can pick up an item with the specified weight.
     * 
     * @param int weight the character intends to lift
     * @return boolean return TRUE if the character can lift the specified weight, FALSE otherwise
     */
    protected boolean canLift(int weight)
    {
        if(carriedWeight + weight <= MAX_WEIGHT)
            return true;
        else
            return false;
    }
    
    /**
     * Increase the carried weight after collecting an item.
     * 
     * @param int weight of the collected item
     */
    protected void increaseCarriedWeight(int weight)
    {
        carriedWeight += weight;
    }
    
    /**
     * Decrease the carried weight after dropping an item.
     * 
     * @param int weight of the dropped item
     */
    protected void decreaseCarriedWeight(int weight)
    {
        carriedWeight -= weight;
    }
    
    /**
     * Enumeration class MovePossibility - used to describe
     * the possibilities of a move.
     *
     * @author Kamil Kuzara
     * @version 2018.11
     */
    public enum MovePossibility
    {
        POSSIBLE, DOOR_CLOSED, NO_DOOR
    }
    
    /**
     * Enumeration class AttackPossibility - used to describe
     * the possibilities of an attack.
     *
     * @author Kamil Kuzara
     * @version 2018.11
     */
    public enum AttackPossibility
    {
        POSSIBLE, NO_BULLETS, NO_WEAPON
    }
}