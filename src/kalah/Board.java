package kalah;

public class Board {
    // Note that houses are indexed anticlockwise starting from house one of player one
    private House[] houses;
    private Store[] stores;

    private int NUM_HOUSES;
    private int NUM_STORES;

    // The player number whose turn it is (should be either 0 or 1)
    private int currentPlayerNum = 0;

    public Board(int numHouses, int numStores) {
        houses = new House[numHouses];
        stores = new Store[numStores];

        for (int i = 0; i < numHouses; i++) {
            houses[i] = new House();
        }
        for (int i = 0; i < numStores; i++) {
            stores[i] = new Store();
        }

        this.NUM_HOUSES = numHouses;
        this.NUM_STORES = numStores;
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
            } else if (houseI == NUM_HOUSES/NUM_STORES) {
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

        // TODO: Handle game ending when there are no seeds left on the current player's side

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

    public boolean housesEmpty() {
        int numHousesPerPlayer = NUM_HOUSES/NUM_STORES;
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
        int numHousesPerPlayer = NUM_HOUSES/NUM_STORES;
        int startI, endI;
        int numSeeds;

        for (int j = 0; j < NUM_STORES; j++) {
            startI = j   * numHousesPerPlayer;
            endI = (j+1) * numHousesPerPlayer;

            for (int i = startI; i < endI; i++) {
                numSeeds = readHouseSeeds(i);
                writeHouseSeeds(i, 0);
                addStoreSeeds(j, numSeeds);
            }
        }
    }

    // TODO: Fix naming convention / inconsistencies between houseNum vs houseI, etc.

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private int readHouseSeeds(int houseNum) {
        return houses[houseNum].getNumSeeds();
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private int readStoreSeeds(int playerNum) {
        return stores[playerNum].getNumSeeds();
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void writeHouseSeeds(int houseNum, int seedNum) {
        houses[houseNum].setNumSeeds(seedNum);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void writeStoreSeeds(int playerNum, int seedNum) {
        stores[playerNum].setNumSeeds(seedNum);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void addHouseSeeds(int houseNum) {
        addHouseSeeds(houseNum, 1);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void addHouseSeeds(int houseNum, int numToAdd) {
        writeHouseSeeds(houseNum, readHouseSeeds(houseNum)+numToAdd);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void addStoreSeeds(int playerNum) {
        addStoreSeeds(playerNum, 1);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void addStoreSeeds(int playerNum, int numToAdd) {
        writeStoreSeeds(playerNum, readStoreSeeds(playerNum)+numToAdd);
    }

    private void toggleCurrentPlayer() {
        currentPlayerNum = (currentPlayerNum+1) % NUM_STORES;
    }

    /* Assumes playerNum is either 0 or 1, and houseNum is in the range 0 to 5 (inclusive) */
    private int houseNumToArrayIndex(int playerNum, int houseNum) {
        if (playerNum < 0 || playerNum > 1 || houseNum < 0 || houseNum > 5) { return -1; }
        return houseNum + playerNum*(NUM_HOUSES/NUM_STORES);
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
        int min = 0;
        int max = NUM_HOUSES-1;
        int diff;
        int result;
        if (i < NUM_HOUSES/NUM_STORES) {
            diff = i - min;
            result = max - diff;
        } else {
            diff = max - i;
            result = min + diff;
        }
        // TODO: Check the above is correct and check if it can be simplified (by running just the first part)
        return result;
    }

    private boolean indexBelongsToPlayer(int arrayIndex) {
        return (currentPlayerNum==0)&&(arrayIndex < NUM_HOUSES/NUM_STORES)
                || (currentPlayerNum==1)&&(arrayIndex >= NUM_HOUSES/NUM_STORES);
    }

    private void captureOpposite(int houseNumToCapture) {
        int numSeeds = readHouseSeeds(houseNumToCapture);

        // Capture the opposite house
        writeHouseSeeds(houseNumToCapture, 0);
        addStoreSeeds(currentPlayerNum, numSeeds);
    }
}
