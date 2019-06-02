package kalah;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected int playerID;
    protected static int nextID = 0;

    protected List<Pit> pits = new ArrayList<Pit>();
    protected int storeIndex;

    public Player() {
        this.playerID = nextID++;

        for (int j = 0; j < Settings.NUM_HOUSES_PER_PLAYER; j++) {
            pits.add(new House());
        }
        this.storeIndex = pits.size();
        pits.add(new Store());
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getHouseSeedCount(int houseIndex) {
        if (houseIndex < 0 || houseIndex > Settings.NUM_HOUSES_PER_PLAYER-1) {
            return -1;
        }
        return pits.get(houseIndex).getNumSeeds();
    }

    public int getStoreSeedCount() {
        return pits.get(this.storeIndex).getNumSeeds();
    }
}
