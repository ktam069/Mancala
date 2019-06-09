package kalah.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Pit {
    protected List<Seed> seeds;
    protected int playerIndex = -1;

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
