package logic.ConstantsEnums;

/**
 * Abstract class for constants used in the game Crosswise
 *
 * @author Jacob Klövekorn
 */
public abstract class Constants {

    /**
     * Constructor
     */
    private Constants() {
    }

    /**
     * Minimum size for a game grid
     */
    public static final int MIN_GAMEGRID_SIZE = 2;
    /**
     * Time for the AI to wait during their turn
     */
    public static final int AI_TURN_TIME = 1000;
    /**
     * Size of the gamegrid
     */
    public static final int GAMEGRID_SIZE = 6;
    /**
     * Amount of possible players
     */
    public static final int PLAYER_COUNT = 4;
    /**
     * Size of the hand
     */
    public static final int HAND_SIZE = 4;
    /**
     * Amount of different symbol token types
     */
    public static final int UNIQUE_SYMBOL_TOKENS = 6;
    /**
     * Amount of different action tokens
     */
    public static final int UNIQUE_ACTION_TOKENS = 4;
    /**
     * Amount of action tokens per token
     */
    public static final int AMOUNT_ACTION_TOKENS = 3;
    /**
     * Amount of symbol tokens per token
     */
    public static final int AMOUNT_NORMAL_TOKENS = 7;
    /**
     * Name of the Logfile
     */
    public static final String LOG_FILE_NAME = "Logfile.txt";
    /**
     * Duration of animation
     */
    public static final long ANIMATION_TIME = 1;
    /**
     * Minimum count of players required for a game
     */
    public static final int MIN_PLAYER_SIZE = 2;
}
