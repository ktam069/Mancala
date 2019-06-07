package kalah.View;

import com.qualitascorpus.testsupport.IO;

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
        List<String> linesToPrint = ASCIIBoard.formatBoardAsLines(storeSeeds, houseSeedsList);

        for (String line : linesToPrint) {
            io.println(line);
        }
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
