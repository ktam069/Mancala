package kalah;

import com.qualitascorpus.testsupport.IO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHandler {
    private static final int NUM_PLAYERS = Settings.NUM_PLAYERS;
    private static final int NUM_HOUSES = Settings.NUM_HOUSES;

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
                if (houseNum < 0 || houseNum > NUM_HOUSES /2) {
                    ioHandler.printInvalidInput();
                    continue;
                }
                if (this.board.getHouseSeeds(this.board.getPlayerNum(), houseNum) < 1) {
                    ioHandler.printInvalidMove();
                    continue;
                }
                board.processMove(houseNum);

                moveMade = true;
            }

            if (gameEnded) { break; }
        }
    }

    // TODO: Could probably improve the handling of P1 vs P2 to avoid duplicate code
    private void printGameState() {
        int numSeeds;
        int playerIndex;

        List<Integer> seedsL1 = new ArrayList<Integer>();
        List<Integer> seedsL2 = new ArrayList<Integer>();
        List<Integer> playerNums = new ArrayList<Integer>();
        List<Integer> storeSeeds = new ArrayList<Integer>();

        // Format P2 Houses
        playerIndex = 1;
        playerNums.add(playerIndex+1);
        for (int i = 0; i < NUM_HOUSES /2; i++) {
            numSeeds = board.getHouseSeeds(playerIndex, i);
            seedsL1.add(numSeeds);
        }

        // Format P1 Store
        playerIndex = 0;
        numSeeds = board.getStoreSeeds(playerIndex);
        storeSeeds.add(numSeeds);

        // Format P2 Store
        playerIndex = 1;
        numSeeds = board.getStoreSeeds(playerIndex);
        storeSeeds.add(numSeeds);

        // Format P1 Houses
        playerIndex = 0;
        for (int i = 0; i < NUM_HOUSES /2; i++) {
            numSeeds = board.getHouseSeeds(playerIndex, i);
            seedsL2.add(numSeeds);
        }
        playerNums.add(playerIndex+1);

        // Print the game state using the IOHandler
        ioHandler.printGameState(seedsL1, seedsL2, playerNums, storeSeeds);
    }

    private String getInput() {
        int playerNum = board.getPlayerNum() + 1;

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

        for (int i = 0; i < NUM_PLAYERS; i++) {
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
