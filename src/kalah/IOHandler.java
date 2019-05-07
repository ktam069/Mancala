package kalah;

import com.qualitascorpus.testsupport.IO;

import java.util.ArrayList;
import java.util.Collections;

public class IOHandler {
    private static final int NUM_PLAYERS = Settings.NUM_PLAYERS;
    private static final int NUM_HOUSES = Settings.NUM_HOUSES;

    private IO io;
    private Board board;

    // TODO: Move printing into its own class

    public IOHandler(IO io) {
        this.io = io;

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
                if (userInput.equals("q")) {
                    printGameOver();
                    gameEnded = true;
                    break;
                }

                // Process the input and update the game's state
                int houseNum = inputToInt(userInput);
                if (houseNum < 0 || houseNum > NUM_HOUSES /2) {
                    io.println("Invalid input. Move again.");
                    continue;
                }
                if (this.board.getHouseSeeds(this.board.getPlayerNum(), houseNum) < 1) {
                    io.println("House is empty. Move again.");
                    continue;
                }
                board.processMove(houseNum);

                moveMade = true;
            }

            if (gameEnded) { break; }
        }
    }

    private void printGameState() {
        // TODO: Could probably improve the handling of P1 vs P2 to avoid duplicate code

        String lineToPrint;
        int numSeeds;
        int playerNum;

        io.println("+----+-------+-------+-------+-------+-------+-------+----+");

        // Format P2 Houses
        playerNum = 1;
        lineToPrint = "| P2 | ";
        for (int i = (NUM_HOUSES /2)-1; i >= 0 ; i--) {
            numSeeds = this.board.getHouseSeeds(playerNum, i);
            lineToPrint += (i+1)+"[";
            if (numSeeds < 10) { lineToPrint += " "; }
            lineToPrint += numSeeds+"] | ";
        }

        // Format P1 Store
        playerNum = 0;
        numSeeds = this.board.getStoreSeeds(playerNum);
        if (numSeeds < 10) { lineToPrint += " "; }
        lineToPrint += numSeeds+" |";

        io.println(lineToPrint);

        io.println("|    |-------+-------+-------+-------+-------+-------|    |");

        // Format P2 Store
        playerNum = 1;
        lineToPrint = "|";
        numSeeds = this.board.getStoreSeeds(playerNum);
        if (numSeeds < 10) { lineToPrint += " "; }
        lineToPrint += " "+numSeeds+" | ";

        // Format P1 Houses
        playerNum = 0;
        for (int i = 0; i < NUM_HOUSES /2; i++) {
            numSeeds = this.board.getHouseSeeds(playerNum, i);
            lineToPrint += (i+1)+"[";
            if (numSeeds < 10) { lineToPrint += " "; }
            lineToPrint += numSeeds+"] | ";
        }
        lineToPrint += "P1 |";

        io.println(lineToPrint);

        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
    }

    private String getInput() {
        int playerNum = this.board.getPlayerNum() + 1;
        String queryStr = "Player P"+playerNum+"'s turn - Specify house number or 'q' to quit: ";
		String userInput = io.readFromKeyboard(queryStr);

        return userInput;
    }

    private int inputToInt(String input) {
        try {
            return ( Integer.parseInt(input) - 1 );
        } catch(Exception e) {
            return -1;
        }
    }

    private void printGameOver() {
        io.println("Game over");
        printGameState();
    }

    private void printGameEnded() {
        printGameOver();
        board.gameEndTallying();

        int newScore;
        ArrayList<Integer> scores = new ArrayList<Integer>();

        for (int i = 0; i < NUM_PLAYERS; i++) {
            newScore = board.getStoreSeeds(i);
            io.println("\tplayer "+(i+1)+":"+newScore);
            scores.add(newScore);
        }

        int maxScore = Collections.max(scores);
        int winner = scores.indexOf(maxScore);

        // Determine result based on whether there are more than one player with max score
        if (winner == scores.lastIndexOf(maxScore)) {
            io.println("Player "+(winner+1)+" wins!");
        } else {
            io.println("A tie!");
        }

    }
}
