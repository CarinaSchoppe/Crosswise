package logic.util;

/**
 * Enum class for an action, used by the logger
 *
 * @author Jacob Klövekorn
 */
public enum Action {
    /**
     * All possible actions
     */
    PLACE("placed"), REMOVE("removed"), SWAPPED("swapped"), MOVED("moved"), REPLACED("replaced");
    /**
     * String text of the performed action
     */
    private final String text;

    /**
     * Constructor
     *
     * @param text given action
     */
    Action(String text) {
        this.text = text;
    }

    /**
     * Getter
     *
     * @return String output of the action
     */
    public String getText() {
        return text;
    }
}
