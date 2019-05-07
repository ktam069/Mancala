package kalah;

public class Board {
    // Note that houses are indexed anticlockwise starting from house one of player one
    private Pit[] houses;
    private Pit[] stores;

    private int NUM_HOUSES = Settings.NUM_HOUSES;
    private int NUM_PLAYERS = Settings.NUM_PLAYERS;

    // TODO: Create players; change array into arrayLists (?)

    // The player number whose turn it is (should be either 0 or 1)
    private int currentPlayerNum = 0;

    public Board() {
        houses = new House[NUM_HOUSES];
        stores = new Store[NUM_PLAYERS];

        for (int i = 0; i < NUM_HOUSES; i++) {
            houses[i] = new House();
        }
        for (int i = 0; i < NUM_PLAYERS; i++) {
            stores[i] = new Store();
        }
    }

    public void processMove(int houseNum) {
        int houseI = houseNumToArrayIndex(currentPlayerNum, houseNum);
        int numSeeds = readHouseSeeds(houseI);

        // Clear the seeds in the house to be processed
        writeHouseSeeds(houseI, 0);

        // Distribute the removed seeds over the board
        while (numSeeds > 0) {
            houseI = (houseI + 1) % NUM_HOUSES;

            if (houseI == 0) {
                // Reached the player 2's store
                if (currentPlayerNum == 0) {
                    addHouseSeeds(houseI);
                    if (numSeeds==1) { attemptCapture(houseI); }
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                    if (numSeeds==1) { attemptCapture(houseI); }
                }
            } else if (houseI == NUM_HOUSES/ NUM_PLAYERS) {
                // Reached the player 1's store
                if (currentPlayerNum == 1) {
                    addHouseSeeds(houseI);
                    if (numSeeds==1) { attemptCapture(houseI); }
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                    if (numSeeds==1) { attemptCapture(houseI); }
                }
            } else {
                addHouseSeeds(houseI);
                if (numSeeds==1) { attemptCapture(houseI); }
            }

            numSeeds--;
        }

        toggleCurrentPlayer();
    }

    /* Returns whether it's player 1's turn (returns 0) or player 2's turn (returns 1) */
    public int getPlayerNum() {
        return currentPlayerNum;
    }

    /* Assumes playerNum is either 0 or 1, and houseNum is in the range 0 to 5 (inclusive) */
    public int getHouseSeeds(int playerNum, int houseNum) {
        if (playerNum < 0 || playerNum > 1 || houseNum < 0 || houseNum > 5) { return -1; }
        int i = houseNumToArrayIndex(playerNum, houseNum);
        return readHouseSeeds(i);
    }

    /* Assumes playerNum is either 0 or 1 */
    public int getStoreSeeds(int playerNum) {
        if (playerNum < 0 || playerNum > 1) { return -1; }
        return readStoreSeeds(playerNum);
    }

    /* Checks if the current player has any seeds in houses */
    public boolean housesEmpty() {
        int numHousesPerPlayer = NUM_HOUSES/ NUM_PLAYERS;
        int startI = currentPlayerNum   * numHousesPerPlayer;
        int endI = (currentPlayerNum+1) * numHousesPerPlayer;

        boolean allEmpty = true;
        for (int i = startI; i < endI; i++) {
            allEmpty = allEmpty && (readHouseSeeds(i) == 0);
        }

        return allEmpty;
    }

    /* Move all remaining seeds into the respective players' stores */
    public void gameEndTallying() {
        int numHousesPerPlayer = NUM_HOUSES/ NUM_PLAYERS;
        int startI, endI;
        int numSeeds;

        for (int j = 0; j < NUM_PLAYERS; j++) {
            startI = j   * numHousesPerPlayer;
            endI = (j+1) * numHousesPerPlayer;

            for (int i = startI; i < endI; i++) {
                numSeeds = readHouseSeeds(i);
                writeHouseSeeds(i, 0);
                addStoreSeeds(j, numSeeds);
            }
        }
    }

    // NOTE: Wrapped in methods to reduce syntactic dependencies if changing data structure

    private int readHouseSeeds(int houseI) {
        return houses[houseI].getNumSeeds();
    }

    private int readStoreSeeds(int playerI) {
        return stores[playerI].getNumSeeds();
    }

    private void writeHouseSeeds(int houseI, int seedNum) {
        houses[houseI].setNumSeeds(seedNum);
    }

    private void writeStoreSeeds(int playerI, int seedNum) {
        stores[playerI].setNumSeeds(seedNum);
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

    private void toggleCurrentPlayer() {
        currentPlayerNum = (currentPlayerNum+1) % NUM_PLAYERS;
    }

    /* Assumes playerNum is either 0 or 1, and houseNum is in the range 0 to 5 (inclusive) */
    private int houseNumToArrayIndex(int playerNum, int houseNum) {
        if (playerNum < 0 || playerNum > 1 || houseNum < 0 || houseNum > 5) { return -1; }
        return houseNum + playerNum*(NUM_HOUSES/ NUM_PLAYERS);
    }

    private void attemptCapture(int currentHouseI) {
        int oppositeHouseI = findOppositeArrayIndex(currentHouseI);

        if (readHouseSeeds(currentHouseI) == 1 && indexBelongsToPlayer(currentHouseI)) {
            if (readHouseSeeds(oppositeHouseI) == 0) { return; }

            captureOpposite(oppositeHouseI);

            // Move the seed in the player's house into their store as well
            writeHouseSeeds(currentHouseI, 0);
            addStoreSeeds(currentPlayerNum, 1);
        }
    }

    /* Calculates the index of the house that is opposite to this one */
    private int findOppositeArrayIndex(int i) {
        int max = NUM_HOUSES-1;
        return (max - i);
    }

    private boolean indexBelongsToPlayer(int arrayIndex) {
        return (currentPlayerNum==0)&&(arrayIndex < NUM_HOUSES/ NUM_PLAYERS)
                || (currentPlayerNum==1)&&(arrayIndex >= NUM_HOUSES/ NUM_PLAYERS);
    }

    private void captureOpposite(int houseIToCapture) {
        int numSeeds = readHouseSeeds(houseIToCapture);

        // Capture the opposite house
        writeHouseSeeds(houseIToCapture, 0);
        addStoreSeeds(currentPlayerNum, numSeeds);
    }
}
