/**
 * Class Weapon is a part of "Operation Wawel" application.
 * It extends class Tool with features and behaviours 
 * characteristic for weapons. The class provides implementations
 * of Tool's abstract methods.
 *
 * @author Kamil Kuzara
 * @version 2018.11
 */
public class Weapon extends Tool
{
    int bullets;     // number of bullets in the weapon

    /**
     * Constructor for objects of class Weapon
     */
    public Weapon(String name, String description, int weight, int bullets)
    {
        super(name, description, weight);
        this.bullets = bullets;
    }

    /**
     * Use the weapon, i.e. attack another character with it.
     * The method will only be executed if the provided parameter
     * is of type Character.
     * 
     * @param Object object to work with (must be of type Character)
     */
    public void use(Object object)
    {
        if(object instanceof Character){        // a character has to be given as the parameter
            Character character = (Character)object;
            if(bullets > 0){
                bullets--;
                character.receiveInjury();
            }
            else{
                System.out.println("You are out of bullets! Try another weapon.");
            }
        }
    }
    
    /**
     * Check if the weapon is useable, i.e. if it
     * has bullets in it.
     * 
     * @return boolean return TRUE if the weapon can be used, FALSE otherwise
     */
    public boolean useable()
    {
        if(bullets > 0){
            return true;
        }
        else{
            return false;
        }
    }
}
