package kalah;

import com.qualitascorpus.testsupport.IO;

import java.util.List;

public class IOHandler {
    private IO io;

    public IOHandler(IO io) {
        this.io = io;
    }

    public void printInvalidInput() {
        io.println("Invalid input. Move again.");
    }

    public void printInvalidMove() {
        io.println("House is empty. Move again.");
    }

    public void printGameState(List<Integer> seedsL1, List<Integer> seedsL2, List<Integer> playerNums, List<Integer> storeSeeds) {
        String lineToPrint;

        io.println("+---------------+");

        // Format P2 Store
        lineToPrint = String.format("|       | P%d %2d |", playerNums.get(0), storeSeeds.get(1));
        io.println(lineToPrint);

        io.println("+-------+-------+");

        // Format Players' Houses
        int maxIndex = seedsL1.size() - 1;
        int j;
        for (int i = 0; i < seedsL1.size(); i++) {
            j = maxIndex - i;
            lineToPrint = String.format("|%2d[%2d] |", i+1, seedsL2.get(i));
            lineToPrint += String.format("%2d[%2d] |", j+1, seedsL1.get(j));
            io.println(lineToPrint);
        }

        io.println("+-------+-------+");

        // Format P1 Store
        lineToPrint = String.format("| P%d %2d |       |", playerNums.get(1), storeSeeds.get(0));
        io.println(lineToPrint);

        io.println("+---------------+");
    }

    public String getUserInput(int playerNum) {
        String queryStr = "Player P"+playerNum+"'s turn - Specify house number or 'q' to quit: ";
        String userInput = io.readFromKeyboard(queryStr);

        return userInput;
    }

    public boolean isInputToQuit(String userInput) {
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
