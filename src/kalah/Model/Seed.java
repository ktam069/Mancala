package kalah.Model;

import kalah.Settings;

public class Seed {
    protected int seedValue;

    public Seed() {
        this.seedValue = Settings.DEFAULT_SEED_VALUE;
    }

    public Seed(int seedValue) {
        this.seedValue = seedValue;
    }

    public int getSeedValue() {
        return this.seedValue;
    }
}
