package kalah;

public abstract class Pit {
    private int seeds;
    private int playerIndex = -1;

    public Pit(int numSeeds) {
        this.seeds = numSeeds;
    }

    public Pit(int numSeeds, int playerIndex) {
        this.seeds = numSeeds;
        this.playerIndex = playerIndex;
    }

    public int getNumSeeds() {
        return this.seeds;
    }

    public void setNumSeeds(int seeds) {
        this.seeds = seeds;
    }

    public int getOwnerIndex() {
        return playerIndex;
    }
}
