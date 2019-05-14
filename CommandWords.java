import java.util.ArrayList;

/**
 * This class is part of the "Operation Wawel" application. 
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling, David J. Barnes, Kamil Kuzara
 * @version 2018.11
 */
public class CommandWords
{
    // an array that holds all valid command words
    private ArrayList<String> validCommands;

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        validCommands = new ArrayList<>();
    }
    
    /**
     * Add a new command to the list of valid commands.
     * 
     * @param String the command to be added to the list
     */
    public void addCommand(String commandWord)
    {
        validCommands.add(commandWord);
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(String command : validCommands){
            if(command.equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}