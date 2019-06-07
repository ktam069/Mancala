package kalah.Model;

import kalah.Settings;

import java.util.List;

/* Handles the sowing of seed for a single move */
public class SeedSower {
    List<Player> players;
    private int houseI;
    private int pitOwnerI;
    private int currentPlayerI;
    private boolean atStore;

    public SeedSower(int houseIndex, int currentPlayerIndex, List<Player> players) {
        this.players = players;
        this.houseI = houseIndex;
        this.pitOwnerI = currentPlayerIndex;
        this.currentPlayerI = currentPlayerIndex;
        this.atStore = false;
    }

    public void goToNextPit() {
        if (atStore) {
            // Leave the store and go to the next player's houses
            atStore = false;
            houseI = 0;
            pitOwnerI = nextPlayerIndex(pitOwnerI);
            return;
        }

        houseI++;
        if (houseI >= Settings.NUM_HOUSES_PER_PLAYER) {
            if (pitOwnerI == currentPlayerI) {
                // Reached the player's own store
                atStore = true;
            } else {
                // Different player's store - go to the next player's houses
                houseI = 0;
                pitOwnerI = nextPlayerIndex(pitOwnerI);
            }
        }
    }

    private int nextPlayerIndex(int playerI) {
        return (playerI+1) % Settings.NUM_PLAYERS;
    }

    public Player getCurrentPitOwner() {
        return players.get(pitOwnerI);
    }

    public int getCurrentPitOwnerIndex() {
        return pitOwnerI;
    }

    public int getCurrentHouseIndex() {
        return houseI;
    }

    public void sowSeedToHouse(int seedValue) {
        Player player = getCurrentPitOwner();
        player.incrementHouseSeedCount(houseI, seedValue);
    }

    public void sowSeedToStore(int seedValue) {
        Player player = getCurrentPitOwner();
        player.incrementStoreSeedCount(seedValue);
    }

    public boolean atStore() {
        return atStore;
    }
}
