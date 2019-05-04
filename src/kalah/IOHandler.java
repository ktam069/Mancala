package kalah;

import com.qualitascorpus.testsupport.IO;

public class IOHandler {
    private IO io;
    private Board board;

    public IOHandler(IO io) {
        this.io = io;

        // Create a game board, instantiating the board
        this.board = new Board();
    }

    public void run() {
        // Runs the game forever until the game is quit with 'q'
        while(true) {
            // Print the current state of the board

            // Wait for input for the next turn

            // Process the input and update the game's state

        }
    }

    private void printGameState() {
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
        io.println("| P2 | 6[ 4] | 5[ 4] | 4[ 4] | 3[ 4] | 2[ 4] | 1[ 4] |  0 |");
        io.println("|    |-------+-------+-------+-------+-------+-------|    |");
        io.println("|  0 | 1[ 4] | 2[ 4] | 3[ 4] | 4[ 4] | 5[ 4] | 6[ 4] | P1 |");
        io.println("+----+-------+-------+-------+-------+-------+-------+----+");
    }

    private void getInput() {
        int playerNum = this.board.getPlayerNum();
        String queryStr = "Player "+playerNum+"'s turn - Specify house number or 'q' to quit: ";
		String userInput = io.readFromKeyboard(queryStr);

//		System.out.println("printing input: " + userInput);
    }
}
