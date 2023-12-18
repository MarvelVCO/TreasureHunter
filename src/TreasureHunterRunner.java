public class TreasureHunterRunner {
    public static void main(String[] args) {
        System.out.println("Welcome to " + Colors.CYAN + "TREASURE HUNTER" + Colors.RESET + "!");
        TreasureHunter game = new TreasureHunter();
        game.play();
    }
}