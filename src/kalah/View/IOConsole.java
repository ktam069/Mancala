package kalah.View;

import com.qualitascorpus.testsupport.IO;
import kalah.Settings;

import java.util.List;

public class IOConsole {
    private IO io;

    public IOConsole(IO io) {
        this.io = io;
    }

    public void printInvalidInput() {
        io.println("Invalid input. Move again.");
    }

    public void printInvalidMove() {
        io.println("House is empty. Move again.");
    }

    public void printGameState(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        String lineToPrint1, lineToPrint2;

        lineToPrint1 = formatLine1(storeSeeds, houseSeedsList);
        lineToPrint2 = formatLine2(storeSeeds, houseSeedsList);

        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
        io.println(lineToPrint1);
        io.println("|    |-------+-------+-------+-------+-------+-------|    |");
        io.println(lineToPrint2);
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
    }

    private String formatLine1(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        String lineToPrint;
        List<Integer> houseSeeds;
        int playerI = 0;

        // Format P2 Houses
        playerI = nextPlayerIndex(playerI);
        lineToPrint = "| P"+(playerI+1)+" |";
        houseSeeds = houseSeedsList.get(playerI);
        for (int i = houseSeeds.size()-1; i >= 0 ; i--) {
            lineToPrint += String.format("%2d[%2d] |", i+1, houseSeeds.get(i));
        }

        // Format P1 Store
        playerI = nextPlayerIndex(playerI);
        lineToPrint += String.format(" %2d |", storeSeeds.get(playerI));

        return lineToPrint;
    }

    private String formatLine2(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        String lineToPrint;
        List<Integer> houseSeeds;
        int playerI = 0;

        // Format P2 Store
        playerI = nextPlayerIndex(playerI);
        lineToPrint = String.format("| %2d |", storeSeeds.get(1));

        // Format P1 Houses
        playerI = nextPlayerIndex(playerI);
        houseSeeds = houseSeedsList.get(playerI);
        for (int i = 0; i < houseSeeds.size(); i++) {
            lineToPrint += String.format("%2d[%2d] |", i+1, houseSeeds.get(i));
        }
        lineToPrint += " P"+(playerI+1)+" |";

        return lineToPrint;
    }

    private int nextPlayerIndex(int playerI) {
        return (playerI+1) % Settings.NUM_PLAYERS;
    }

    public String getUserInput(int playerNum) {
        String queryStr = "Player P"+playerNum+"'s turn - Specify house number or 'q' to quit: ";
        String userInput = io.readFromKeyboard(queryStr);

        return userInput;
    }

    public boolean isInputToQuit(String userInput) {
        if (userInput == null) { return false; }
        return userInput.equals("q");
    }

    public void printPlayerScore(int player, int score) {
        int playerNum = player + 1;
        io.println("\tplayer "+playerNum+":"+score);
    }

    public void printPlayerWon(int winnerIndex) {
        int winnerNum = winnerIndex + 1;
        io.println("Player "+winnerNum+" wins!");
    }

    public void printGameOver() {
        io.println("Game over");
    }

    public void printGameTied() {
        io.println("A tie!");
    }
}
