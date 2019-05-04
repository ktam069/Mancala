package kalah;

public class Board {
    private final int NUM_HOUSES = 12;
    private final int NUM_STORES = 2;

    // Note that houses are indexed anticlockwise starting from house one of player one
    private House[] houses;
    private Store[] stores;

    // The player number whose turn it is (should be either 0 or 1)
    private int currentPlayerNum = 0;

    public Board() {
        houses = new House[NUM_HOUSES];
        stores = new Store[NUM_STORES];

        for(int i = 0; i < NUM_HOUSES; i++) {
            houses[i] = new House();
        }
        for(int i = 0; i < NUM_STORES; i++) {
            stores[i] = new Store();
        }
    }

    public void processMove(int houseNum) {
        //

        currentPlayerNum = (currentPlayerNum+1) % 2;
    }

    private void moveSeeds(int houseNum){

    }

    /* Returns whether it's player 1's turn or player 2's turn */
    public int getPlayerNum() {
        return (currentPlayerNum+1);
    }

    public int getPlayerOneHouseSeeds(int houseNum) {
        int i = houseNum;
        return houses[i].getNumSeeds();
    }

    public int getPlayerTwoHouseSeeds(int houseNum) {
        int i = houseNum + (NUM_HOUSES/2);
        return houses[i].getNumSeeds();
    }

    public int getPlayerOneStoreSeeds() {
        return stores[0].getNumSeeds();
    }

    public int getPlayerTwoStoreSeeds() {
        return stores[1].getNumSeeds();
    }
}
