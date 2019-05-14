/**
 * Class Tool is a part of "Operation Wawel" application.
 * It extends class Item with behaviours characteristic 
 * for tools.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public abstract class Tool extends Item
{
    public Tool(String name, String description, int weight)
    {
        super(name,description,weight);
    }
    
    /**
     * The abstract method to be implemented by subclasses
     * of this class. The methods implement the action of
     * using a particular tool.
     * 
     * @param Object an object to work with
     */
    public abstract void use(Object object);
}
