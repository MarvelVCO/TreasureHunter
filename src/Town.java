/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private static String[] treasures = new String[]{"Crown", "Trophy", "Gem", "Dust"};
    public static String treasureFound = "";
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String townTreasure;
    private boolean gameOver;
    private boolean townisSearched;
    public Town () {}

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        gameOver = false;
    }

    public String getLatestNews() {
        return printMessage;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        townTreasure = treasures[(int)(Math.random() * 3 + 1)];


        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }
    public String huntForTreasure() {
        if(townisSearched) {
            return "You have already searched this town.";
        } else {
            hunter.addTreasure(townTreasure.equals("Dust") ? "" : townTreasure);
            townisSearched = true;
            treasureFound += townTreasure.equals("Dust") ? "" : townTreasure + " ,";
            return "Congrats! You found the treasure " + townTreasure;
        }
    }
    public String treasuresFound(){
        return treasureFound;
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak() && !hunter.isEasyMode()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = Colors.RED + "You couldn't find any trouble" + Colors.RESET;
        } else if(hunter.isSamuraiMode()) {
            printMessage = Colors.RED + "You want trouble, stranger...? \n*The brawler sees your sword and decides it is better to give up* \nForgive me stranger, here have my gold!"  + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;

            printMessage += "\nYou won the brawl and receive " + goldDiff + Colors.YELLOW + " gold" + Colors.RESET;
            hunter.changeGold(goldDiff);
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n"  + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage +=  Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + goldDiff + Colors.YELLOW + " gold" + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                gameOver = hunter.changeGold(-goldDiff);
            }
        }
    }
    public void dig(boolean hasShovel) {
        if(hasShovel) {
            System.out.println("You can't dig without a shovel");
        }
        double randNum = Math.random();
        if(randNum >= 0.5) {
            int coinsEarned = (int) (Math.random() * 20) + 1;
            System.out.println("You dug up " + coinsEarned + " coins");
            hunter.changeGold(coinsEarned);
        }
        if(randNum < 0.5) {
            System.out.println("You dug but only found dirt");
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random() * 6) + 1;
        switch (rnd) {
            case 1 -> {
                return new Terrain("Mountains", "Rope");
            }
            case 2 -> {
                return new Terrain("Ocean", "Boat");
            }
            case 3 -> {
                return new Terrain("Plains", "Horse");
            }
            case 4 -> {
                return new Terrain("Desert", "Water");
            }
            case 5 -> {
                return new Terrain("Jungle", "Machete");
            }
            case 6 -> {
                return new Terrain("Marsh", "Boots");
            }
        }
        return new Terrain("Mountains", "Rope");
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}