package kalah.Model;

import kalah.Settings;

import java.util.ArrayList;
import java.util.List;

public class Board implements ModelInterface {
    private List<Pit> pits = new ArrayList<Pit>();

    // The player index whose turn it is
    private int currentPlayerI = Settings.STARTING_PLAYER_INDEX;

    public Board() {
        for (int playerI = 0; playerI < Settings.NUM_PLAYERS; playerI++) {
            // Create houses and stores for each player
            for (int j = 0; j < Settings.NUM_HOUSES_PER_PLAYER; j++) {
                pits.add(new House(playerI));
            }
            pits.add(new Store(playerI));
        }
    }

    @Override
    public void processMove(int houseNum) {
        int pitI = findHouseArrayIndex(currentPlayerI, houseNum);
        int numSeeds = readHouseSeeds(pitI);

        // Clear the seeds in the house to be processed
        writeHouseSeeds(pitI, 0);

        // Distribute the removed seeds over the board
        while (numSeeds > 0) {
            pitI = (pitI + 1) % Settings.NUM_PITS;

            if (pitIsAStore(pitI)) {
                if (currentPlayerI != getPitOwner(pitI)) {
                    // Reached the/an opposition player's store - ignore the store
                    continue;
                } else {
                    // Reached the player's own store
                    addStoreSeeds(currentPlayerI);

                    if (numSeeds == 1) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }
                }
            } else {
                addHouseSeeds(pitI);
                attemptCapture(pitI, numSeeds);
            }

            numSeeds--;
        }

        toggleCurrentPlayer();
    }

    /* Returns the array index for which player's turn it is (ranges from 0 to NUM_PLAYERS-1) */
    @Override
    public int getPlayerI() {
        return currentPlayerI;
    }

    /* Assumes playerI is from 0 to NUM_PLAYERS-1, and houseI is in the range 0 to NUM_HOUSES_PER_PLAYER-1 */
    @Override
    public int getHouseSeeds(int playerI, int houseI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1 || houseI < 0 || houseI > Settings.NUM_HOUSES_PER_PLAYER-1) {
            return -1;
        }
        int i = findHouseArrayIndex(playerI, houseI);
        return readHouseSeeds(i);
    }

    /* Assumes playerI is from 0 to NUM_PLAYERS-1 */
    @Override
    public int getStoreSeeds(int playerI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1) { return -1; }
        return readStoreSeeds(playerI);
    }

    /* Checks if the current player has any seeds in houses */
    @Override
    public boolean housesEmpty() {
        int startI = currentPlayerI * Settings.NUM_PITS_PER_SIDE;
        int endI = (currentPlayerI+1) * Settings.NUM_PITS_PER_SIDE - 1;

        boolean allEmpty = true;
        for (int i = startI; i < endI; i++) {
            allEmpty = allEmpty && (readHouseSeeds(i) == 0);
        }

        return allEmpty;
    }

    /* Move all remaining seeds into the respective players' stores */
    @Override
    public void gameEndTallying() {
        int startI, endI;
        int numSeeds;

        for (int j = 0; j < Settings.NUM_PLAYERS; j++) {
            startI = j   * Settings.NUM_PITS_PER_SIDE;
            endI = (j+1) * Settings.NUM_PITS_PER_SIDE - 1;

            for (int i = startI; i < endI; i++) {
                numSeeds = readHouseSeeds(i);
                writeHouseSeeds(i, 0);
                addStoreSeeds(j, numSeeds);
            }
        }
    }

    /* Returns the corresponding index in the pits List.
     * Assumes playerI is from 0 to NUM_PLAYERS-1, and houseI is in the range 0 to NUM_HOUSES_PER_PLAYER-1.
     * */
    private int findHouseArrayIndex(int playerI, int houseI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1 || houseI < 0 || houseI > Settings.NUM_HOUSES_PER_PLAYER-1) {
            return -1;
        }
        return houseI + playerI*Settings.NUM_PITS_PER_SIDE;
    }

    /* Assumes playerI is from 0 to NUM_PLAYERS-1 */
    private int findPlayerStoreIndex(int playerI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1) { return -1; }
        return (playerI+1)*Settings.NUM_PITS_PER_SIDE - 1;
    }

    private int readHouseSeeds(int houseI) {
        return pits.get(houseI).getNumSeeds();
    }

    private int readStoreSeeds(int playerI) {
        int storeI = findPlayerStoreIndex(playerI);
        return pits.get(storeI).getNumSeeds();
    }

    private void writeHouseSeeds(int houseI, int seedNum) {
        pits.get(houseI).setNumSeeds(seedNum);
    }

    private void writeStoreSeeds(int playerI, int seedNum) {
        int storeI = findPlayerStoreIndex(playerI);
        pits.get(storeI).setNumSeeds(seedNum);
    }

    private void addHouseSeeds(int houseI) {
        addHouseSeeds(houseI, 1);
    }

    private void addHouseSeeds(int houseI, int numToAdd) {
        writeHouseSeeds(houseI, readHouseSeeds(houseI)+numToAdd);
    }

    private void addStoreSeeds(int playerI) {
        addStoreSeeds(playerI, 1);
    }

    private void addStoreSeeds(int playerI, int numToAdd) {
        writeStoreSeeds(playerI, readStoreSeeds(playerI)+numToAdd);
    }

    private boolean pitIsAHouse(int pitIndex) {
        Pit pit = pits.get(pitIndex);
        return (pit instanceof House);
    }

    private boolean pitIsAStore(int pitIndex) {
        Pit pit = pits.get(pitIndex);
        return (pit instanceof Store);
    }

    private int getPitOwner(int pitIndex) {
        Pit pit = pits.get(pitIndex);
        return pit.getOwnerIndex();
    }

    private void toggleCurrentPlayer() {
        currentPlayerI = (currentPlayerI +1) % Settings.NUM_PLAYERS;
    }

    private void attemptCapture(int currentHouseI, int numSeedsToSow) {
        if (numSeedsToSow != 1) { return; }
        
        int oppositeHouseI = findOppositeArrayIndex(currentHouseI);

        if (readHouseSeeds(oppositeHouseI) == 0) { return; }

        if (readHouseSeeds(currentHouseI) == 1 && (currentPlayerI == getPitOwner(currentHouseI))) {
            captureOpposite(oppositeHouseI);

            // Move the seed in the player's house into their store as well
            writeHouseSeeds(currentHouseI, 0);
            addStoreSeeds(currentPlayerI, 1);
        }
    }

    /* Calculates the index of the house that is opposite to this one */
    private int findOppositeArrayIndex(int i) {
        int max = Settings.NUM_PITS - 2;
        return (max - i);
    }

    private void captureOpposite(int houseIToCapture) {
        int numSeeds = readHouseSeeds(houseIToCapture);

        // Capture the opposite house
        writeHouseSeeds(houseIToCapture, 0);
        addStoreSeeds(currentPlayerI, numSeeds);
    }
}
