package kalah.Model;

public interface ModelInterface {
    void processMove(int houseNum);

    /* Returns the array index for which player's turn it is (ranges from 0 to NUM_PLAYERS-1) */
    int getPlayerI();

    /* Assumes playerI is from 0 to NUM_PLAYERS-1, and houseI is in the range 0 to NUM_HOUSES_PER_PLAYER-1 */
    int getHouseSeeds(int playerI, int houseI);

    /* Assumes playerI is from 0 to NUM_PLAYERS-1 */
    int getStoreSeeds(int playerI);

    /* Checks if the current player has any seeds in houses */
    boolean housesEmpty();

    /* Move all remaining seeds into the respective players' stores */
    void gameEndTallying();
}
