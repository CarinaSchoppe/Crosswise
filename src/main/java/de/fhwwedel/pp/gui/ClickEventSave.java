package de.fhwwedel.pp.gui;

public class ClickEventSave {
    private Integer posX;

    private Integer posY;

    private boolean isGrid;

    private Integer playerID;

    private Integer handPosition;


    public ClickEventSave(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public ClickEventSave(boolean isGrid, Integer playerID, Integer handPosition) {
        this.isGrid = isGrid;
        this.playerID = playerID;
        this.handPosition = handPosition;
    }

    public Integer getPosX() {
        return posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public boolean isGrid() {
        return isGrid;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getHandPosition() {
        return handPosition;
    }
}
