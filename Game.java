import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *  This class is the main class of the "Operation Wawel" application. 
 *  "Operation Wawel" is a very involving, text based adventure game. 
 *  The game is loosely based on historical events of World War II.
 *  The player's goal is to explore the Wawel Castle, in which precious artwork
 *  is kept, collect that artwork and safely get it out of the building.
 *  There are a number of things that are to make it a challenge. Some of the
 *  rooms are closed and can only be opened with a key, there are enemies 
 *  that will try to attack the player.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, characters and items, creates the parser and starts the game.
 *  It also evaluates and executes the commands that the parser returns.
 * 
 * @author  Michael Kölling, David J. Barnes, Kamil Kuzara
 * @version 2018.11
 */
public class Game
{
    private Parser parser;
    private Player player;
    private ArrayList<Room> rooms;      // stores all the rooms in the game,
                                        // used for random assigning of characters 
                                        // and items to rooms
    
    private ArrayList<Room> openRooms;  // stores all open rooms in the game,
                                        // needed to control assigning to random rooms
                                        // f.e. a key cannot be assigned to a closed 
                                        // room, because that would eliminate
                                        // the chance of opening the door
    
    private HashSet<Room> occupiedRooms;    // a set of rooms occupied by enemies
                                            // necessary to keep a room occupied 
                                            // only by one enemy
    
    private HashSet<Character> enemies;     // stores all the enemies
    
    private HashSet<Item> artwork;      // stores all the items that need to be 
                                        // collected in order to win the game
                                        
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        Room startingRoom = createRooms();    // create all the rooms
        
        player = new Player(startingRoom);   // create the player
        
        createEnemies();    // create enemies of the player
        
        createItems();  // create all items in the game
        
        createParser();     // create the parser and add valid commands
    }

    /**
     * Create all the rooms and link their exits together.
     * 
     * @return Room starting location
     */
    private Room createRooms()
    {
        rooms = new ArrayList();
        openRooms = new ArrayList();
        
        Room outside, dragonsDen, mainChamber, cathedral, audienceHall, ballroom, arsenal;
      
        // create the rooms
        outside = new Room("outside the main entrance of the Wawel Castle");
        rooms.add(outside);
        openRooms.add(outside);
        
        dragonsDen = new Room("in the Dragon's Den");
        rooms.add(dragonsDen);
        openRooms.add(dragonsDen);
        
        mainChamber = new Room("in the main chamber");
        rooms.add(mainChamber);
        openRooms.add(mainChamber);
        
        cathedral = new Room("in the cathedral");
        rooms.add(cathedral);
        openRooms.add(cathedral);
        
        audienceHall = new Room("in the audience hall");
        rooms.add(audienceHall);
        
        ballroom = new Room("in the ballroom");
        rooms.add(ballroom);
        
        arsenal = new Room("in the arsenal");
        rooms.add(arsenal);
        openRooms.add(arsenal);
        
        // initialise room exits
        outside.setExit("east", cathedral, true);
        outside.setExit("north", mainChamber, true);
        
        cathedral.setExit("west", outside, true);
        
        mainChamber.setExit("south", outside, true);
        mainChamber.setExit("east", ballroom, false);
        mainChamber.setExit("west", audienceHall, false);
        mainChamber.setExit("down", arsenal, true);
        
        ballroom.setExit("west", mainChamber, false);
        
        audienceHall.setExit("east", mainChamber, false);
        
        arsenal.setExit("up", mainChamber, true);
        arsenal.setExit("west", dragonsDen, true);
        
        dragonsDen.setExit("east", arsenal, true);

        return outside;  // start game outside, i.e. return starting location
    }

    /**
     * Create all the enemies.
     */
    private void createEnemies()
    {
        occupiedRooms = new HashSet<>();
        enemies = new HashSet<>();
        
        // generate pseudorandom index of the starting room for an enemy
        Random rand = new Random();
        int roomIndex = 1 + rand.nextInt(rooms.size() - 1);     // first room from the list
                                                                // not considered, we do not
                                                                // want any enemies in the same
                                                                // room as th player at the
                                                                // beginning of the game
        
        // create enemy no. 1
        Room enemyLocation = rooms.get(roomIndex);
        Character enemy = new Character(enemyLocation);
        enemies.add(enemy);
        occupiedRooms.add(enemyLocation);
        
        //create enemy no.2
        roomIndex = 1 + rand.nextInt(rooms.size() - 1);
        enemyLocation = rooms.get(roomIndex);
        enemy = new Character(enemyLocation);
        enemies.add(enemy);
        occupiedRooms.add(enemyLocation);
    }
    
    /**
     * Create all items and assign them to various rooms, chests and characters.
     */
    private void createItems()
    {       
        artwork = new HashSet<>();
        Item item;      // temporary variable to store consecutively created items
        Random rand = new Random();
        int roomIndex;
        Room room;      // temporary variable to store rooms in which the items are disposed
        
        item = new Chest("chest", "A chest that can store items.", 150);
        roomIndex = 1 + rand.nextInt(rooms.size() - 1);     // items are not disposed outside
        room = rooms.get(roomIndex);
        room.addItem(item);     // dispose the chest in a room
        
        item = new Item("leonardo", "'Lady with an Ermine' by Leonardo da Vincii.", 17);
        Item tempItem = room.getItem("chest");      // the item will be put in the chest
        if(tempItem instanceof Chest){      // try to put the item in the chest
            ((Chest)tempItem).addItem(item);
        }
        else{    // if failed to put in the chest, dispose in the room
            room.addItem(item);
        }
        artwork.add(item);      // add to the list of target items
            
        item = new Item("raphael", "'Portrait of a Young Man' by Raphael.", 7);
        roomIndex = 1 + rand.nextInt(rooms.size() - 1);
        room = rooms.get(roomIndex);
        room.addItem(item);
        artwork.add(item);
        
        item = new Item("rembrandt", "'Landscape with the Good Samaritan' by Rembrandt.", 12);
        roomIndex = 1 + rand.nextInt(rooms.size() - 1);
        room = rooms.get(roomIndex);
        room.addItem(item);
        artwork.add(item);
        
        item = new Key("key", "A key to open all locked doors in the main chamber.", 1, rooms.get(2));
        roomIndex = 1 + rand.nextInt(openRooms.size() - 1);
        room = openRooms.get(roomIndex);        // dispose in one of the open rooms
        room.addItem(item);
        
        item = new Weapon("gun1", "A gun.", 3, 10);
        player.addItem(item);       // give weapon to the player
        
        // create and give weapons to the enemies
        int gunNumber = 2;
        for(Character enemy : enemies)
        {
            item = new Weapon("gun" + gunNumber, "A gun.", 3, 10);
            enemy.addItem(item);
            gunNumber++;
        }
    }
    
    /**
     * Create the parser.
     */
    private void createParser()
    {
        // create the parser 
        parser = new Parser();
        
        // add all valid commands
        parser.addCommand("go");
        parser.addCommand("back");
        parser.addCommand("quit");
        parser.addCommand("help");
        parser.addCommand("collect");
        parser.addCommand("drop");
        parser.addCommand("use");
        parser.addCommand("open");
        parser.addCommand("list");
        parser.addCommand("attack");
    }
    
    /**
     * Main method. Used to start the game.
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        game.play();
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            moveCharacters();   // try to move each enemy around the building
            
            Room currentRoom = player.getCurrentRoom();
            boolean roomOccupied = isOccupied(currentRoom);
            
            boolean endTheGame;
            
            if(roomOccupied){   // if the room was entered by an enemy, trigger interaction
                endTheGame = interact(currentRoom);
            }
            else{       // else, read and process command
                System.out.println();
                System.out.println(player.getCurrentRoom().getLongDescription());

                Command command = parser.getCommand();
                endTheGame = processCommand(command);
            }
            
            if(!endTheGame){   // if there was no signal to end the game, meaning
                               // the player did not die, check if the player won
                finished = checkForWin();
            }
            else{
                finished = true;
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the \"Operation Wawel\" game!");
        System.out.println("You are about to play an amazing game set in the times of WWII.");
        System.out.println("You are a Polish spy who managed to find the location where the priceless artwork,");
        System.out.println("stolen by the Nazis, is stored. Your goal is to infiltrate the Wawel Castle, ");
        System.out.println("in which they are hidden, and get them out safely. You must watch out, though.");
        System.out.println("There are Nazi soldiers in the castle who might try to attack you.");
        System.out.println();
        System.out.println("Number of items you have to collect to win: " + artwork.size());
        System.out.println("Number of enemies in the game: " + enemies.size());
        System.out.println("Maximum number of injuries: " + player.getMaxInjuriesNumber());
        System.out.println();
        System.out.println("Type 'help' if you need help.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param Command the command to be processed
     * @return boolean return TRUE if the command ends the game, FALSE otherwise
     */
    private boolean processCommand(Command command)
    {
        boolean endTheGame = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if(commandWord.equals("help")) {
            printHelp();
            return false;
        }
        else if(commandWord.equals("go")) {
            boolean moveMade = processGo(command);      // try to change rooms
            
            if(moveMade){
                // invoke interaction(...) method to see if an interaction
                // should be triggered
                Room room = player.getCurrentRoom();
                endTheGame = interaction(room);
            }
            else{
                return false;
            }
        }
        else if(commandWord.equals("back")) {
            endTheGame = processBack();      // go to the previous room
        }
        else if(commandWord.equals("list")){
            processList(command);
        }
        else if(commandWord.equals("collect")){
            processCollect(command);
        }
        else if(commandWord.equals("drop")){
            processDrop(command);
        }
        else if(commandWord.equals("open")){
            processOpen(command);
        }
        else if(commandWord.equals("use")){
            processUse(command);
        }
        else if(commandWord.equals("quit")) {
            endTheGame = quit(command);
        }
        else{
            System.out.println("You cannot use this command now!");
        }
        
        return endTheGame;
    }
    
    /**
     * Move all the characters (except the player).
     */
    private void moveCharacters()
    {
        if(enemies.size() > 0){     // execute only if there exist characters
            String[] directions = {"north", "south", "east", "west", "up", "down"};
            Random rand = new Random();
            
            for(Character enemy : enemies)
            {
                int index = rand.nextInt(6);    // generate one of 6 possible directions
                String direction = directions[index];
                
                // check if the character can move in the desired direction
                Character.MovePossibility canMove = enemy.isMovePossible(direction);
                
                // if the move is possible try to make it
                if(canMove == Character.MovePossibility.POSSIBLE){
                    Room room = enemy.getCurrentRoom().getExit(direction);
                    boolean roomOccupied = isOccupied(room);
                    
                    // move to another room only if there are no 
                    // other characters there
                    if(!roomOccupied){
                        moveCharacter(enemy, direction);
                    }
                }
            }
        }
    }
    
    /**
     * Move a single character.
     * 
     * @param Character the character to move
     * @param String direction in which to move the character
     */
    private void moveCharacter(Character character, String direction)
    {
        // remove the old location from the list of occupied rooms
        occupiedRooms.remove(character.getCurrentRoom());
        
        // change character's location
        character.changeRoom(direction);
        
        // add the new location to the list of occupied rooms
        occupiedRooms.add(character.getCurrentRoom());
    }
    
    /**
     * Print out the interaction opening message for the player.
     */
    private void printInteractionMessage()
    {
        System.out.println();
        System.out.println("You are " + player.getCurrentRoom().getShortDescription() + ".");
        System.out.println("You have run across an enemy soldier!");
    }
    
    /**
     * Check if there is an enemy in the room. If yes, trigger interaction.
     * 
     * @param Room the room where the player is
     * @return boolean return TRUE if the result ends the game, FALSE otherwise
     */
    private boolean interaction(Room room)
    {
        // if the inspected room is occupied by an enemy, trigger interaction
        if(isOccupied(room)){
            boolean endTheGame = interact(room);
            return endTheGame;
        }
        else{
            return false;
        }
    }
    
    /**
     * Process interaction between the player and an enemy. A pseudorandom
     * number is generated, based on which the type of interaction is chosen.
     * The player will either be engaged in a fight with the enemy, or captured
     * and transported (teleported) to a random room.
     * 
     * @param Room the room where the player is
     * @return boolean return TRUE if the result ends the game, FALSE otherwise
     */
    private boolean interact(Room room)
    {
        printInteractionMessage();
        
        Random rand = new Random();
        
        int enemyAction = rand.nextInt();
        
        // only approx. one time out of five the player will be 
        // captured and teleported
        if(enemyAction % 5 != 0){
            printFightMessage();
            Character enemy = enemyInRoom(room);    // get the enemy
            
            // engage the characters in a fight
            boolean endTheGame = fight(player, enemy);     
            return endTheGame;
        }
        else{
            printTeleportingMessage();
            
            // randomly choose a room for the player to be moved to,
            // the 'outside' is omitted hence "1 + ...",
            // only choose from the list of open rooms
            int roomIndex = 1 + rand.nextInt(openRooms.size() - 1);
            Room destination = openRooms.get(roomIndex);
            
            // move the player to the destination and drop all
            // their items in random open rooms
            player.teleport(destination, openRooms);
            return false;   // the game is not finished
        }
    }
    
    /**
     * Print out the capture message for the player.
     */
    private void printTeleportingMessage()
    {
        System.out.println("The soldier attacked you quietly from the back. You have been captured and moved to another room.");
        System.out.println("All your items have been taken away from you and are now scattered around the building.");
    }
    
    /**
     * Print out the fight opening message for the player.
     */
    private void printFightMessage()
    {
        System.out.println("You have been attacked! You have two choices: return fire or run away.");
        System.out.println("Available commands:");
        System.out.println("attack  go  back");
    }
    
    /**
     * Process a fight between two characters: the player and an enemy.
     * The fight goes on until one of the characters dies or the player 
     * decides to run away.
     * 
     * @param Player the player
     * @param Character the enemy
     * @return boolean return TRUE if the result ends the game, FALSE otherwise
     */
    private boolean fight(Player player, Character enemy)
    {
        Character.AttackPossibility canEnemyAttack;
        
        while(true)
        {
            // check if the enemy can attack the player
            // if they can, perform the attack
            // if they can't, do nothing
            canEnemyAttack = enemy.canAttack();
            if(canEnemyAttack == Character.AttackPossibility.POSSIBLE){
                enemy.attack(player, enemy.getWeapon());
                System.out.println();
                System.out.println("You have been hit!");
                System.out.println("Number of injuries: " + player.getInjuriesNumber() + "/"
                                    + player.getMaxInjuriesNumber());
            }
                        
            // only process the command if the player is not dead,
            // the player might have been killed with the first 
            // enemy attack in this fight
            if(!player.isDead()){
                Command command = parser.getCommand();
                String commandWord = command.getCommandWord();
                
                if(command.isUnknown()) {
                    System.out.println("I don't know what you mean...");
                }
                else if(commandWord.equals("go")){
                    boolean moveMade = processGo(command);      // try to change rooms
                    
                    if(moveMade){
                        // invoke interaction(...) method to see if an interaction
                        // should be triggered
                        Room room = player.getCurrentRoom();
                        boolean endTheGame = interaction(room);
                        return endTheGame;
                    }
                }
                else if(commandWord.equals("back")){
                    boolean endTheGame = processBack();      // go to the previous room
                    return endTheGame;
                }
                else if(commandWord.equals("attack")){
                    processAttack(command, enemy);
                }
                else{
                    System.out.println("You cannot use this command now!");
                }
                
                // remove the enemy if they are dead and finish the fight
                if(enemy.isDead()){
                    // all the enemy's items are dropped in the current room
                    enemy.dropAllItems();
                    
                    // the current room is no longer occupied
                    occupiedRooms.remove(enemy.getCurrentRoom());
                    
                    // remove the enemy from the game
                    enemies.remove(enemy);
                    
                    System.out.println("You eliminated the enemy soldier!");
                    System.out.println("Enemies left: " + enemies.size());
                    
                    return false;    // the game is NOT over, so return false
                }
            }
            else{
                System.out.println("The enemy killed you! You lost!");
                return true;    // send a signal to end the game
            }
        }
    }
    
    /**
     * Check if the player has won the game.
     * 
     * @return boolean return TRUE if the player has won, FALSE otherwise
     */
    private boolean checkForWin()
    {
        Room currentRoom = player.getCurrentRoom();
        if(currentRoom == rooms.get(0)){    // the player has to be outside
            for(Item item : artwork)
            {
                // try to find the artwork in the player's inventory
                String artworkName = item.getName();
                Item soughtItem = player.getItem(artworkName);
                
                // if at least one of the sought items is not in the 
                // player's inventory there is no need to look further,
                // the player has not won
                if(soughtItem == null){
                    return false;
                }
            }
            System.out.println("You win!");
            return true;    // the player has won, send a signal to end the game
        }
        return false;   // the player did not win if they are not outside
    }
    
    /**
     * Check if the given room is currently occupied by an enemy soldier.
     * 
     * @param Room the room to inspect
     * @return boolean return TRUE if the room is occupied, FALSE otherwise
     */
    private boolean isOccupied(Room room)
    {
        for(Room location : occupiedRooms)
        {
            if(room == location)
                return true;
        }
        
        // if we get here the room is not occupied, i.e. there are no characters in the room
        return false;
    }
    
    /**
     * Get the enemy soldier in the specified room.
     * 
     * @param Room the room to get the enemy from
     * @return Character return the enemy if there is one, null otherwise
     */
    private Character enemyInRoom(Room room)
    {
        for(Character enemy : enemies)
        {
            if(enemy.getCurrentRoom() == room){
                return enemy;
            }
        }
        
        // if we get here, there are no characters in the room
        return null;
    }

    // implementations of the user commands:
    
    /**
     * Process the "go" command.
     * 
     * @param Command the command to process
     * @return boolean return TRUE if the move was possible, FALSE otherwise
     */
    private boolean processGo(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }
        
        String direction = command.getSecondWord();
        
        // check if the player can move in the specified direction
        Character.MovePossibility canMove = player.isMovePossible(direction);
        
        // act accordingly to the result of the checking
        switch(canMove){
            case POSSIBLE:
                player.goRoom(direction);
                break;
            case DOOR_CLOSED:
                System.out.println("This door is closed!");
                break;
            case NO_DOOR:
                System.out.println("There is no door!");
                break;
        }
        
        if(canMove == Character.MovePossibility.POSSIBLE){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Process the "back" command. After executing the move,
     * check if an interaction should be started in the new location.
     * 
     * @return boolean return TRUE if the result ends the game, FALSE otherwise
     */
    private boolean processBack()
    {
        player.goBack();
        
        // invoke interaction(...) method to see if an interaction
        // should be triggered
        Room room = player.getCurrentRoom();
        return interaction(room);
    }
    
    /**
     * Process the "attack" command.
     * 
     * @param Command the command to process
     * @param Character the enemy to be attacked
     */
    private void processAttack(Command command, Character enemy)
    {
        // check if the player can attack at all
        Character.AttackPossibility canPlayerAttack = player.canAttack();
        switch(canPlayerAttack){
            case POSSIBLE:  // the attack is possible
                if(command.hasSecondWord()){
                    String enemyName = command.getSecondWord();
                    if(enemyName.equals("enemy")){
                        if(command.hasThirdWord()){
                            String weaponName = command.getThirdWord();
                            
                            // attack 'enemy' with 'weaponName'
                            player.attack(enemy, weaponName);        
                        }
                        else{
                            System.out.println("You must specify the weapon.");
                        }
                    }
                    else{
                        System.out.println("Incorrect enemy name! Try using: enemy.");
                    }
                }
                else{
                    System.out.println("Attack who?");
                }
                break;
            
            case NO_BULLETS:
                System.out.println("Your weapons are out of bullets!");
                break;
            
            case NO_WEAPON:
                System.out.println("You do not have any weapons!");
                break;
        }
    }
    
    /**
     * Process the "list" command.
     * 
     * @param Command the command to process
     */
    private void processList(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what items to list...
            System.out.println("What items do you want to list? Available: ");
            System.out.println("player  room");
        }
        else{
            String location = command.getSecondWord();      // analyse the command to know
                                                            // whose items to list (player's
                                                            // or room's)
            
            if(location.equals("player")){
                player.listItems();     // list items the player is carrying
            }
            else if(location.equals("room")){
                player.getCurrentRoom().listItems();    // list items in this room
            }
            else{
                System.out.println("The command's operand is incorrect.");
            }
        }
    }
    
    /**
     * Process the "collect" command.
     * 
     * @param Command the command to process
     */
    private void processCollect(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to collect...
            System.out.println("Collect what? You must specify the item!");
            return;
        }
        String itemName = command.getSecondWord();
        player.collectItem(itemName);
    }
    
    /**
     * Process the "drop" command.
     * 
     * @param Command the command to process
     */
    private void processDrop(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("Drop what? You must specify the item!");
            return;
        }
        String itemName = command.getSecondWord();
        player.dropItem(itemName);
    }
    
    /**
     * Process the "open" command.
     * 
     * @param Command the command to process
     */
    private void processOpen(Command command)
    {
        if(command.hasSecondWord()){
            String chestName = command.getSecondWord();
            
            // get the chest from the room
            Item chest = player.getCurrentRoom().getItem(chestName);
            if(chest == null){
                System.out.println("There is no such item in this room!");
            }
            else if(chest instanceof Chest){
                // cast the object 'chest' on the type Chest to be able
                // to invoke the correct method, we can do this because
                // we already know that 'chest' is an instance of Chest,
                
                // open the chest
                ((Chest)chest).use(player.getCurrentRoom());
            }
            else
                System.out.println("This item is not a chest.");
        }
        else
            System.out.println("Open what?");
    }
    
    /**
     * Process the "use" command.
     * 
     * @param Command the command to process
     */
    private void processUse(Command command)
    {
        if(command.hasSecondWord()){
            String keyName = command.getSecondWord();
            
            // get the key from player's list of items
            Item key = player.getItem(keyName);
            if(key == null){
                System.out.println("You do not have such an item in your inventory!");
            }
            else if(key instanceof Key){
                // we can only try to use a key if we know that
                // it actually is a key
                
                String[] directions = {"north", "south", "east", "west", "up", "down"};
                for(String direction : directions)
                {   
                    // add newly opened rooms to the list of opened rooms,
                    // does not work for the further rooms linked
                    // to a newly opened one
                    Room currentRoom = player.getCurrentRoom();
                    Room room = currentRoom.getExit(direction);
                    if(room != null && !currentRoom.isExitOpen(direction)){
                        openRooms.add(room);
                    }
                }
                
                // open all directions
                ((Key)key).use(player.getCurrentRoom());
            }
            else
                System.out.println("This item is not a key.");
        }
        else
            System.out.println("Use what?");
    }

    /**
     * Print out help information.
     * Here we print helpful information and a list of the 
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are a spy whose job is to infiltrate the Wawel Castle,");
        System.out.println("where three priceless paintings are kept, and get these paintings");
        System.out.println("out of the building safely. Their codenames are:");
        System.out.println("leonardo  raphael  rembrandt");
        System.out.println();
        System.out.println("Number of enemies: " + enemies.size());
        System.out.println("Number of injuries: " + player.getInjuriesNumber() + "/"
                            + player.getMaxInjuriesNumber());
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param Command the command to be processed
     * @return boolean return TRUE if this command quits the game, FALSE otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}