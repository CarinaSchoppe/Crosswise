package logic.ConstantsEnums;

/**
 * Enum for the time of the animation of an AI turn
 *
 * @author Jacob Klövekorn
 */
public enum AnimationTime {
    SLOW(4), MIDDLE(2), FAST(1);
    /**
     * actual time as int
     */
    private final int time;

    /**
     * Constructor
     *
     * @param time animation time
     */
    AnimationTime(int time) {
        this.time = time;
    }

    /**
     * Getter
     *
     * @return int animation time
     */
    public int getTime() {
        return time;
    }
}

