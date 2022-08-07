package logic;

public enum TeamType {

    VERTICAL("Vertical"), HORIZONTAL("Horizontal"), DEACTIVE("Deactive");


    private final String team;

    TeamType(String team) {
        this.team = team;
    }


    public String getTeamName() {
        return team;
    }
}
