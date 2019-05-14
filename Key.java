/**
 * Class Key is a part of "Operation Wawel" application.
 * It extends class Tool with features and behaviours 
 * characteristic for keys. The class provides implementations
 * of Tool's abstract methods.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Key extends Tool
{
    private Room correspondingRoom;     // the room in which the key can be used
    
    /**
     * Constructor for objects of class Key
     */
    public Key(String name, String description, int weight, Room room)
    {
        super(name,description,weight);
        correspondingRoom = room;
    }

    /**
     * Use the key, i.e. open all exits in the given room.
     * The method will only be executed if the provided parameter
     * is of type Room.
     * 
     * @param Object object to work with (must be of type Room)
     */
    public void use(Object object)
    {
        if(object instanceof Room){     // a room has to be given as the parameter
            Room room = (Room)object;
            if(room == correspondingRoom)
            {
                room.openExits();
            }
            else
                System.out.println("This key cannot be used in this room!");
        }
    }
}