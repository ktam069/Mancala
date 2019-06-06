package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.Controller.MancalaGame;
import kalah.Model.Board;
import kalah.View.IOConsole;

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
		IOConsole ioConsole = new IOConsole(io);

		// Create and instantiate the game board
		Board board = new Board();

		// Start Game Handler, which accepts an IOConsole for handling of the view
		MancalaGame mancalaGame = new MancalaGame(ioConsole, board);

		// Start the game: this will run forever until the game is quit
		mancalaGame.run();
	}
}
