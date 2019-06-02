package kalah.Model;

import kalah.Settings;

public class House extends Pit {
    public House() {
        super(Settings.INIT_NUM_HOUSE_SEEDS);
    }

    // TODO: may want to remove this constructor in the future
    public House(int playerIndex) {
        super(Settings.INIT_NUM_HOUSE_SEEDS, playerIndex);
    }
}
