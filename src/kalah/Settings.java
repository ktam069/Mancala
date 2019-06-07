package kalah;

public class Settings {
    public static final int NUM_PLAYERS = 2;
    public static final int NUM_HOUSES_PER_PLAYER = 6;
    public static final int NUM_HOUSES = NUM_PLAYERS * NUM_HOUSES_PER_PLAYER;

    public static final int NUM_PITS_PER_PLAYER = NUM_HOUSES_PER_PLAYER + 1;
    public static final int NUM_PITS = NUM_PLAYERS * NUM_PITS_PER_PLAYER;

    public static final int INIT_NUM_HOUSE_SEEDS = 4;
    public static final int INIT_NUM_STORE_SEEDS = 0;

    public static final int DEFAULT_SEED_VALUE = 1;

    public static final int STARTING_PLAYER_INDEX =  0;
}
