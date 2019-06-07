package kalah.Model;

import kalah.Settings;

public class Store extends Pit {
    public Store() {
        super(Settings.INIT_NUM_STORE_SEEDS);
    }

    public Store(int playerIndex) {
        super(Settings.INIT_NUM_STORE_SEEDS, playerIndex);
    }
}
