package de.fhwwedel.pp.gui;

public class ClickEventSave {
    private Integer posX;

    private Integer posY;

    private boolean isGrid;


    private Integer handPosition;


    public ClickEventSave(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
        this.isGrid = true;
    }

    public ClickEventSave(Integer handPosition) {
        this.isGrid = false;

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

    public Integer getHandPosition() {
        return handPosition;
    }
}
