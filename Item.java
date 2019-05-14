/**
 * Class Item is a part of "Operation Wawel" application.
 * It defines item's features as well as behaviours.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Item
{
    private int weight;
    private String name;
    private String description;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description, int weight)
    {
        this.name=name;
        this.description = description;
        this.weight = weight;
    }
    
    /**
     * @return String return the name of the item.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return String return the description of the item.
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * @return int return the weight of the item.
     */
    public int getWeight()
    {
        return weight;
    }
}
