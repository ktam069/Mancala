package kalah;

public abstract class Pit {
    private int seeds;

    public Pit(int numSeeds) {
        this.seeds = numSeeds;
    }

    public int getNumSeeds() {
        return this.seeds;
    }

    public void setNumSeeds(int seeds) {
        this.seeds = seeds;
    }
}
