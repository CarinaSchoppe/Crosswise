package logic.ConstantsEnums;

/**
 * Enum for a TeamType
 *
 * @author Jacob Klövekorn
 */
public enum TeamType {

    VERTICAL("Vertical"), HORIZONTAL("Horizontal"), DEACTIVE("Deactive");
    /**
     * Name of the team
     */
    private final String team;

    /**
     * Constructor
     *
     * @param team team name
     */
    TeamType(String team) {
        this.team = team;
    }

    /**
     * Getter
     *
     * @return team name
     */
    public String getTeamName() {
        return team;
    }
}
