package kalah.Model;

import kalah.Settings;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Player> players = new ArrayList<Player>();

    // The player index whose turn it is
    private int currentPlayerI = Settings.STARTING_PLAYER_INDEX;

    public Board() {
        for (int playerI = 0; playerI < Settings.NUM_PLAYERS; playerI++) {
            players.add(new Player());
        }
    }

    public void processMove(int houseI) {
        Player currentPlayer = players.get(currentPlayerI);
        int numSeeds = currentPlayer.getHouseSeedCount(houseI);

        // Clear the seeds in the house to be processed
        currentPlayer.setHouseSeedCount(houseI, 0);

        SeedSower seedSower = new SeedSower(houseI, currentPlayerI, players);

        // Distribute the removed seeds over the board
        while (numSeeds > 0) {
            seedSower.goToNextPit();

            if (seedSower.atStore()) {
                // Reached the player's own store
                seedSower.sowSeedToStore(1);

                if (numSeeds == 1) {
                    // Extra turn
                    toggleCurrentPlayer();
                    break;
                }
            } else {
                seedSower.sowSeedToHouse(1);
                attemptCapture(seedSower, numSeeds);
            }

            numSeeds--;
        }

        toggleCurrentPlayer();
    }

    private void toggleCurrentPlayer() {
        currentPlayerI = nextPlayerIndex(currentPlayerI);
    }

    private int nextPlayerIndex(int playerI) {
        return (playerI+1) % Settings.NUM_PLAYERS;
    }

    /* Returns the array index for which player's turn it is (ranges from 0 to NUM_PLAYERS-1) */
    public int getPlayerI() {
        return currentPlayerI;
    }

    /* Assumes playerI is from 0 to NUM_PLAYERS-1, and houseI is in the range 0 to NUM_HOUSES_PER_PLAYER-1 */
    public int getHouseSeeds(int playerI, int houseI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1 || houseI < 0 || houseI > Settings.NUM_HOUSES_PER_PLAYER-1) {
            return -1;
        }
        Player player = players.get(playerI);
        return player.getHouseSeedCount(houseI);
    }

    /* Assumes playerI is from 0 to NUM_PLAYERS-1 */
    public int getStoreSeeds(int playerI) {
        if (playerI < 0 || playerI > Settings.NUM_PLAYERS-1) { return -1; }
        Player player = players.get(playerI);
        return player.getStoreSeedCount();
    }

    /* Checks if the current player has any seeds in houses */
    public boolean housesEmpty() {
        Player player = players.get(currentPlayerI);

        boolean allEmpty = true;
        for (int i = 0; i < Settings.NUM_HOUSES_PER_PLAYER; i++) {
            allEmpty = allEmpty && (player.getHouseSeedCount(i) == 0);
        }

        return allEmpty;
    }

    /* Move all remaining seeds into the respective players' stores */
    public void gameEndTallying() {
        Player player;
        int numSeeds;

        for (int j = 0; j < Settings.NUM_PLAYERS; j++) {
            player = players.get(j);
            for (int i = 0; i < Settings.NUM_HOUSES_PER_PLAYER; i++) {
                numSeeds = player.getHouseSeedCount(i);
                player.setHouseSeedCount(i,0);
                player.incrementStoreSeedCount(numSeeds);
            }
        }
    }

    private void attemptCapture(SeedSower seedSower, int numSeedsToSow) {
        if (numSeedsToSow != 1) { return; }

        int currentHouseI = seedSower.getCurrentHouseIndex();
        int oppositeHouseI = findOppositeHouseIndex(currentHouseI);
        int houseOwnerI = seedSower.getCurrentPitOwnerIndex();
        int nextPlayerI = nextPlayerIndex(houseOwnerI);

        Player houseOwner = players.get(houseOwnerI);
        Player nextPlayer = players.get(nextPlayerI);

        int houseSeedCount = houseOwner.getHouseSeedCount(currentHouseI);
        int oppositeSeedCount = nextPlayer.getHouseSeedCount(oppositeHouseI);

        // Nothing to capture
        if (oppositeSeedCount == 0) { return; }

        // Perform capture if the last seed is sown on the player's own houses
        if (houseSeedCount == 1 && (currentPlayerI == houseOwnerI)) {
            // Capture the seeds from the opposite house
            int numSeeds = nextPlayer.getHouseSeedCount(oppositeHouseI);
            nextPlayer.setHouseSeedCount(oppositeHouseI, 0);
            houseOwner.incrementStoreSeedCount(numSeeds);

            // Move the seed in the player's house into their store as well
            houseOwner.setHouseSeedCount(currentHouseI, 0);
            houseOwner.incrementStoreSeedCount(houseSeedCount);
        }
    }

    /* Calculates the index of the house that is opposite to this one */
    private int findOppositeHouseIndex(int i) {
        int maxI = Settings.NUM_HOUSES_PER_PLAYER - 1;
        return (maxI - i);
    }
}
