package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.Controller.ControllerInterface;
import kalah.Controller.MancalaGame;
import kalah.Model.Board;
import kalah.Model.ModelInterface;
import kalah.View.ASCIIBoard;
import kalah.View.HorizontalASCIIBoard;
import kalah.View.IOConsole;
import kalah.View.ViewInterface;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}

	public void play(IO io) {
		// Start input/output console (for i/o handling)
		ViewInterface ioConsole = new IOConsole(io);

		// Set the board layout used
		ASCIIBoard asciiBoard = new HorizontalASCIIBoard();
		ioConsole.setASCIIBoard(asciiBoard);

		// Create and instantiate the game board
		ModelInterface board = new Board();

		// Start Game Handler, which accepts an IOConsole for handling of the view
		ControllerInterface mancalaGame = new MancalaGame(ioConsole, board);

		// Start the game: this will run forever until the game is quit
		mancalaGame.run();
	}
}
