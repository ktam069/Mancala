package kalah;

public class Seed {
    private int value;

    public Seed() {
        this.value = Settings.DEFAULT_SEED_VALUE;
    }

    public Seed(int seedValue) {
        this.value = seedValue;
    }

    public int getSeedValue() {
        return this.value;
    }
}
