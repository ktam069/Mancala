package kalah.View;

import com.qualitascorpus.testsupport.IO;

import java.util.List;

public class IOConsole implements ViewInterface {
    private IO io;

    public IOConsole(IO io) {
        this.io = io;
    }

    @Override
    public void printInvalidInput() {
        io.println("Invalid input. Move again.");
    }

    @Override
    public void printInvalidMove() {
        io.println("House is empty. Move again.");
    }

    @Override
    public void printGameState(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        List<String> linesToPrint = ASCIIBoard.formatBoardAsLines(storeSeeds, houseSeedsList);

        for (String line : linesToPrint) {
            io.println(line);
        }
    }

    @Override
    public String getUserInput(int playerNum) {
        String queryStr = "Player P"+playerNum+"'s turn - Specify house number or 'q' to quit: ";
        String userInput = io.readFromKeyboard(queryStr);

        return userInput;
    }

    @Override
    public boolean isInputToQuit(String userInput) {
        if (userInput == null) { return false; }
        return userInput.equals("q");
    }

    @Override
    public void printPlayerScore(int player, int score) {
        int playerNum = player + 1;
        io.println("\tplayer "+playerNum+":"+score);
    }

    @Override
    public void printPlayerWon(int winnerIndex) {
        int winnerNum = winnerIndex + 1;
        io.println("Player "+winnerNum+" wins!");
    }

    @Override
    public void printGameOver() {
        io.println("Game over");
    }

    @Override
    public void printGameTied() {
        io.println("A tie!");
    }
}
