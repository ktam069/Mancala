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
		// Start Input/Output Handler, which will also starts the game
		IOHandler ioHandler = new IOHandler(io);

		// Start the game: this will run forever until the game is quit
		ioHandler.run();
	}
}
