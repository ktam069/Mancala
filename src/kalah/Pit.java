package kalah;

public abstract class Pit {
    private int seeds;

    public Pit(int numSeeds) {
        this.seeds = numSeeds;
    }

    public void addASeed() {
        this.seeds++;
    }

    public int getNumSeeds() {
        return this.seeds;
    }

    public void setNoSeeds(int seeds) {
        this.seeds = seeds;
    }
}
