package kalah.View;

import java.util.List;

public interface ViewInterface {
    void setASCIIBoard(ASCIIBoard asciiBoard);

    void printInvalidInput();

    void printInvalidMove();

    void printGameState(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList);

    String getUserInput(int playerNum);

    boolean isInputToQuit(String userInput);

    void printPlayerScore(int player, int score);

    void printPlayerWon(int winnerIndex);

    void printGameOver();

    void printGameTied();
}
