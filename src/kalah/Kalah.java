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
		// Start Input/Output Handler
		IOHandler ioHandler = new IOHandler(io);

		// Start Game Handler, which accepts an IOHandler for 'view' handling
		GameHandler gameHandler = new GameHandler(ioHandler);

		// Start the game: this will run forever until the game is quit
		gameHandler.run();
	}
}
