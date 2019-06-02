package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

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

		// Start Game Handler, which accepts an IOConsole for handling of the view
		MancalaGame mancalaGame = new MancalaGame(ioConsole);

		// Start the game: this will run forever until the game is quit
		mancalaGame.run();
	}
}
