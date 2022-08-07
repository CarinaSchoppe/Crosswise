package logic;

public enum Action {
    PLACE("placed"), REMOVE("removed"), SWAPPED("swapped"), MOVED("moved"), REPLACED("replaced");

    private final String text;

    Action(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
