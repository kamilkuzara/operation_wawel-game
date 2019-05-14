import java.util.HashSet;

/**
 * Class Chest is a part of "Operation Wawel" application.
 * It extends class Tool with features and behaviours 
 * characteristic for chests. The class provides implementations
 * of Tool's abstract methods.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Chest extends Tool
{
    private boolean isOpen;
    private HashSet<Item> contents;
    
    /**
     * Constructor for objects of class Chest
     */
    public Chest(String name, String description, int weight)
    {
        super(name,description,weight);
        contents = new HashSet<>();
        isOpen = false;
    }

    /**
     * Use the chest, i.e. open it. When the chest is open, all its 
     * contents drop out in the room. The method will only be executed
     * if the provided parameter is of type Room. The room provided should
     * be the one in which the chest is.
     * 
     * @param Object object to work with (must be of type Room)
     */
    public void use(Object object)
    {
        if(object instanceof Room){     // a room has to be given as the parameter
            Room room = (Room)object;
            if(!isOpen)     // a chest cannot be opened twice
            {
                // put all items in the room
                for(Item item : contents)
                {
                    room.addItem(item);
                    contents.remove(item);
                }
                isOpen = true;
                System.out.println("The chest is now open.");
            }
            else
                System.out.println("You have already opened this chest!");
        }
    }
    
    /**
     * Add the item to the chest's contents list, i.e. put
     * the item in the chest. Although it is possible, one should not
     * add another chest here, because it makes little sense.
     * 
     * @param Item the item to be added
     */
    public void addItem(Item item)
    {
        contents.add(item);
    }
}
