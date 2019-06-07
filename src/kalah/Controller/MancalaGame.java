package kalah.Controller;

import kalah.Model.Board;
import kalah.Settings;
import kalah.View.IOConsole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MancalaGame {
    private IOConsole ioConsole;
    private Board board;

    public MancalaGame(IOConsole ioConsole) {
        this.ioConsole = ioConsole;

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
                if (ioConsole.isInputToQuit(userInput)) {
                    printGameOver();
                    gameEnded = true;
                    break;
                }

                // Process the input and update the game's state
                int houseNum = inputToInt(userInput);
                if (houseNum < 0 || houseNum > Settings.NUM_HOUSES/2-1) {
                    ioConsole.printInvalidInput();
                    continue;
                }
                if (board.getHouseSeeds(board.getPlayerI(), houseNum) < 1) {
                    ioConsole.printInvalidMove();
                    continue;
                }
                board.processMove(houseNum);

                moveMade = true;
            }

            if (gameEnded) { break; }
        }
    }

    private void printGameState() {
        List<Integer> storeSeeds = new ArrayList<Integer>();
        List<List<Integer>> houseSeedsList = new ArrayList<List<Integer>>();

        for (int i = 0; i < Settings.NUM_PLAYERS; i++) {
            List<Integer> houseSeeds = new ArrayList<Integer>();
            houseSeedsList.add(houseSeeds);

            // Get the seed numbers for the houses and stores of each player
            getSeedsAsList(storeSeeds, houseSeedsList.get(i), i);
        }

        // Print the game state using the IOConsole
        ioConsole.printGameState(storeSeeds, houseSeedsList);
    }

    private void getSeedsAsList(List<Integer> storeSeeds, List<Integer> houseSeedsList, int playerI) {
        int numSeeds;

        // Format the player's store
        numSeeds = board.getStoreSeeds(playerI);
        storeSeeds.add(numSeeds);

        // Format the player's houses
        for (int i = 0; i < Settings.NUM_HOUSES_PER_PLAYER; i++) {
            numSeeds = board.getHouseSeeds(playerI, i);
            houseSeedsList.add(numSeeds);
        }
    }

    private String getInput() {
        int playerNum = board.getPlayerI() + 1;

        return ioConsole.getUserInput(playerNum);
    }

    private int inputToInt(String input) {
        try {
            return ( Integer.parseInt(input) - 1 );
        } catch(Exception e) {
            return -1;
        }
    }

    private void printGameOver() {
        ioConsole.printGameOver();
        printGameState();
    }

    private void printGameEnded() {
        printGameOver();
        board.gameEndTallying();

        int newScore;
        ArrayList<Integer> scores = new ArrayList<Integer>();

        // Get an ArrayList of the players' final scores
        for (int i = 0; i < Settings.NUM_PLAYERS; i++) {
            newScore = board.getStoreSeeds(i);
            ioConsole.printPlayerScore(i, newScore);
            scores.add(newScore);
        }

        int maxScore = Collections.max(scores);
        int winnerI = scores.indexOf(maxScore);

        // Determine result based on whether there are more than one player with max score
        if (winnerI == scores.lastIndexOf(maxScore)) {
            ioConsole.printPlayerWon(winnerI);
        } else {
            ioConsole.printGameTied();
        }

    }
}
