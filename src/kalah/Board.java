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
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                }
            } else if (houseI == NUM_HOUSES/2) {
                // Reached the player 1's store
                if (currentPlayerNum == 1) {
                    addHouseSeeds(houseI);
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                }
            } else {
                addHouseSeeds(houseI);
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
        writeHouseSeeds(houseNum, readHouseSeeds(houseNum)+1);
    }

    // NOTE: Wrapped in a method to reduce syntactic dependencies when changing data structure used
    private void addStoreSeeds(int playerNum) {
        writeStoreSeeds(playerNum, readStoreSeeds(playerNum)+1);
    }

    private void toggleCurrentPlayer() {
        currentPlayerNum = (currentPlayerNum+1) % NUM_STORES;
    }

    /* Assumes playerNum is either 0 or 1, and houseNum is in the range 0 to 5 (inclusive) */
    private int houseNumToArrayIndex(int playerNum, int houseNum) {
        if (playerNum < 0 || playerNum > 1 || houseNum < 0 || houseNum > 5) { return -1; }
        return houseNum + playerNum*(NUM_HOUSES /2);
    }

    /* Calculates the index of the house that is opposite to this one */
    private int findOppositeArrayIndex(int i) {
        int min = 0;
        int max = NUM_HOUSES-1;
        int diff;
        int result;
        if (i <= NUM_HOUSES/2) {
            diff = i - min;
            result = max - diff;
        } else {
            diff = max - i;
            result = min + diff;
        }
        return result;
    }
}