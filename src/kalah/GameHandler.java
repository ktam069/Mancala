package kalah;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private IOHandler ioHandler;
    private Board board;

    public GameHandler(IOHandler ioHandler) {
        this.ioHandler = ioHandler;

        // Create a game board, instantiating the board
        this.board = new Board();
    }

    public void run() {
        boolean moveMade;
        boolean gameEnded = false;

        // Runs the game forever until the game is quit with 'q'
        while(true) {
            moveMade = false;

            while (!moveMade) {
                // Print the current state of the board
                printGameState();

                // Terminate the game if one side has no seeds left
                if (board.housesEmpty()) {
                    printGameEnded();
                    gameEnded = true;
                    break;
                }

                // Wait for input for proceeding to the next turn
                String userInput = getInput();

                // Terminate the program upon the input of 'q'
                if (ioHandler.isInputToQuit(userInput)) {
                    printGameOver();
                    gameEnded = true;
                    break;
                }

                // Process the input and update the game's state
                int houseNum = inputToInt(userInput);
                if (houseNum < 0 || houseNum > Settings.NUM_HOUSES/2-1) {
                    ioHandler.printInvalidInput();
                    continue;
                }
                if (board.getHouseSeeds(board.getPlayerI(), houseNum) < 1) {
                    ioHandler.printInvalidMove();
                    continue;
                }
                board.processMove(houseNum);

                moveMade = true;
            }

            if (gameEnded) { break; }
        }
    }

    private void printGameState() {
        int numSeeds;
        int playerIndex;

        List<Integer> seedsL1 = new ArrayList<Integer>();
        List<Integer> seedsL2 = new ArrayList<Integer>();
        List<Integer> playerNums = new ArrayList<Integer>();
        List<Integer> storeSeeds = new ArrayList<Integer>();

        List<List<Integer>> seedLines = new ArrayList<List<Integer>>();
        seedLines.add(seedsL1);
        seedLines.add(seedsL2);

        for (int i = 0; i < Settings.NUM_PLAYERS; i++) {
            getSeedsAsList(seedLines.get(i), playerNums, storeSeeds, i);
        }

        // Print the game state using the IOHandler
        ioHandler.printGameState(seedLines.get(0), seedLines.get(1), playerNums, storeSeeds);
    }

    private void getSeedsAsList(List<Integer> list, List<Integer> playerNums, List<Integer> storeSeeds, int playerI) {
        int numSeeds;

        // Format the opponent's houses first
        playerI = (playerI+1) % Settings.NUM_PLAYERS;
        for (int i = 0; i < Settings.NUM_HOUSES/2; i++) {
            numSeeds = board.getHouseSeeds(playerI, i);
            list.add(numSeeds);
        }
        playerNums.add(playerI+1);

        // Format the current player's store
        playerI = (playerI+1) % Settings.NUM_PLAYERS;
        numSeeds = board.getStoreSeeds(playerI);
        storeSeeds.add(numSeeds);
    }

    private String getInput() {
        int playerNum = board.getPlayerI() + 1;

        return ioHandler.getUserInput(playerNum);
    }

    private int inputToInt(String input) {
        try {
            return ( Integer.parseInt(input) - 1 );
        } catch(Exception e) {
            return -1;
        }
    }

    private void printGameOver() {
        ioHandler.printGameOver();
        printGameState();
    }

    private void printGameEnded() {
        printGameOver();
        board.gameEndTallying();

        int newScore;
        ArrayList<Integer> scores = new ArrayList<Integer>();

        for (int i = 0; i < Settings.NUM_PLAYERS; i++) {
            newScore = board.getStoreSeeds(i);
            ioHandler.printPlayerScore(i, newScore);
            scores.add(newScore);
        }

        int maxScore = Collections.max(scores);
        int winnerI = scores.indexOf(maxScore);

        // Determine result based on whether there are more than one player with max score
        if (winnerI == scores.lastIndexOf(maxScore)) {
            ioHandler.printPlayerWon(winnerI);
        } else {
            ioHandler.printGameTied();
        }

    }
}
