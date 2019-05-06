package kalah;

import com.qualitascorpus.testsupport.IO;

public class IOHandler {
    private static final int NUM_HOUSES = 12;
    private static final int NUM_STORES = 2;    // This might be redundant as it would always be two

    private IO io;
    private Board board;

    public IOHandler(IO io) {
        this.io = io;

        // Create a game board, instantiating the board
        this.board = new Board(NUM_HOUSES, NUM_STORES);
    }

    public void run() {
        boolean moveMade;
        boolean gameOver = false;

        // Runs the game forever until the game is quit with 'q'
        while(true) {
            moveMade = false;

            while (!moveMade) {
                // Print the current state of the board
                printGameState();

                // Wait for input for proceeding to the next turn
                String userInput = getInput();

                // Terminate the program upon the input of 'q'
                if (userInput.equals("q")) {
                    printGameOver();
                    gameOver = true;
                    break;
                }

                // Process the input and update the game's state
                int houseNum = inputToInt(userInput);
                if (houseNum < 0 || houseNum > NUM_HOUSES/2) {
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

            if (gameOver) { break; }
        }
    }

    private void printGameState() {
        // TODO: Could proabably improve the handling of P1 vs P2 to avoid duplicate code

        String lineToPrint;
        int numSeeds;
        int playerNum;

        io.println("+----+-------+-------+-------+-------+-------+-------+----+");

        // Format P2 Houses
        playerNum = 1;
        lineToPrint = "| P2 | ";
        for (int i = (NUM_HOUSES/2)-1; i >= 0 ; i--) {
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
        for (int i = 0; i < NUM_HOUSES/2; i++) {
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
}
