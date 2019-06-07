package kalah.Model;

import kalah.Settings;

public class House extends Pit {
    public House() {
        super(Settings.INIT_NUM_HOUSE_SEEDS);
    }

    public House(int playerIndex) {
        super(Settings.INIT_NUM_HOUSE_SEEDS, playerIndex);
    }
}
