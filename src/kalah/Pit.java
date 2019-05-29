package kalah;

import java.util.ArrayList;
import java.util.List;

public abstract class Pit {
    private List<Seed> seeds;
    private int playerIndex = -1;

    public Pit(int numSeeds) {
        this.seeds = new ArrayList<Seed>();
        for (int i = 0; i < numSeeds; i++) {
            this.seeds.add(new Seed());
        }
    }

    public Pit(int numSeeds, int playerIndex) {
        this(numSeeds);
        this.playerIndex = playerIndex;
    }

    public int getNumSeeds() {
        return this.seeds.size();
    }

    public void addSeed() {
        this.seeds.add(new Seed());
    }

    public void addSeeds(int numSeeds) {
        for (int i = 0; i < numSeeds; i++) {
            this.addSeed();
        }
    }

    public void removeSeed() {
        this.seeds.remove(seeds.size()-1);
    }

    public void removeSeeds(int numToRemove) {
        for (int i = 0; i < numToRemove; i++) {
            this.removeSeed();
        }
    }

    // TODO: Potentially refactor the use of this so that only houses has this method
    // ... i.e. use add or remove seed methods instead
    public void setNumSeeds(int seeds) {
        if (seeds > getNumSeeds()) {
            this.addSeeds(seeds-getNumSeeds());
        } else {
            this.removeSeeds(getNumSeeds()-seeds);
        }
    }

    public int getOwnerIndex() {
        return playerIndex;
    }
}
