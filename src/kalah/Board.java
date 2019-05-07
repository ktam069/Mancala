package kalah;

import java.util.ArrayList;
import java.util.List;

public class Board {
    // Note that houses are indexed anticlockwise starting from house one of player one
    // TODO
//    private List<Pit> pits = new ArrayList<Pit>();

    // TODO: manage pits by using their owners

    private List<Pit> houses = new ArrayList<Pit>();
    private List<Pit> stores = new ArrayList<Pit>();

    // The player number whose turn it is (should be either 0 or 1 if there are two players)
    private int currentPlayerNum = 0;

    public Board() {
        for (int playerI = 0; playerI < Settings.NUM_PLAYERS; playerI++) {
            // Create houses and stores for each player
            for (int j = 0; j < Settings.NUM_HOUSES_PER_PLAYER; j++) {
                houses.add(new House(playerI));
            }
            stores.add(new Store(playerI));
        }
    }

    public void processMove(int houseNum) {
        int houseI = houseNumToArrayIndex(currentPlayerNum, houseNum);
        int numSeeds = readHouseSeeds(houseI);

        // Clear the seeds in the house to be processed
        writeHouseSeeds(houseI, 0);

        // Distribute the removed seeds over the board
        while (numSeeds > 0) {
            houseI = (houseI + 1) % Settings.NUM_HOUSES;

            if (houseI == 0) {
                // Reached the player 2's store
                if (currentPlayerNum == 0) {
                    addHouseSeeds(houseI);
                    attemptCapture(houseI, numSeeds);
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                    attemptCapture(houseI, numSeeds);
                }
            } else if (houseI == Settings.NUM_HOUSES / Settings.NUM_PLAYERS) {
                // Reached the player 1's store
                if (currentPlayerNum == 1) {
                    addHouseSeeds(houseI);
                    attemptCapture(houseI, numSeeds);
                } else {
                    addStoreSeeds(currentPlayerNum);
                    numSeeds--;

                    if (numSeeds == 0) {
                        // Extra turn
                        toggleCurrentPlayer();
                        break;
                    }

                    addHouseSeeds(houseI);
                    attemptCapture(houseI, numSeeds);
                }
            } else {
                addHouseSeeds(houseI);
                attemptCapture(houseI, numSeeds);
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
        int numHousesPerPlayer = Settings.NUM_HOUSES/ Settings.NUM_PLAYERS;
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
        int numHousesPerPlayer = Settings.NUM_HOUSES / Settings.NUM_PLAYERS;
        int startI, endI;
        int numSeeds;

        for (int j = 0; j < Settings.NUM_PLAYERS; j++) {
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
        return houses.get(houseI).getNumSeeds();
    }

    private int readStoreSeeds(int playerI) {
        return stores.get(playerI).getNumSeeds();
    }

    private void writeHouseSeeds(int houseI, int seedNum) {
        houses.get(houseI).setNumSeeds(seedNum);
    }

    private void writeStoreSeeds(int playerI, int seedNum) {
        stores.get(playerI).setNumSeeds(seedNum);
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
        currentPlayerNum = (currentPlayerNum+1) % Settings.NUM_PLAYERS;
    }

    /* Assumes playerNum is either 0 or 1, and houseNum is in the range 0 to 5 (inclusive) */
    private int houseNumToArrayIndex(int playerNum, int houseNum) {
        if (playerNum < 0 || playerNum > 1 || houseNum < 0 || houseNum > 5) { return -1; }
        return houseNum + playerNum*(Settings.NUM_HOUSES/ Settings.NUM_PLAYERS);
    }

    private void attemptCapture(int currentHouseI, int numSeedsToSow) {
        if (numSeedsToSow != 1) { return; }
        
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
        int max = Settings.NUM_HOUSES - 1;
        return (max - i);
    }

    private boolean indexBelongsToPlayer(int arrayIndex) {
        return (currentPlayerNum==0)&&(arrayIndex < Settings.NUM_HOUSES/ Settings.NUM_PLAYERS)
                || (currentPlayerNum==1)&&(arrayIndex >= Settings.NUM_HOUSES/ Settings.NUM_PLAYERS);
    }

    private void captureOpposite(int houseIToCapture) {
        int numSeeds = readHouseSeeds(houseIToCapture);

        // Capture the opposite house
        writeHouseSeeds(houseIToCapture, 0);
        addStoreSeeds(currentPlayerNum, numSeeds);
    }
}
