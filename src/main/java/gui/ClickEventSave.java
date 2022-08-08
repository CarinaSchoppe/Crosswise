package gui;

/**
 * Class for a Clicked Position either on the grid or on the hand of the current player
 *
 * @author Jacob Klövekorn
 */
public class ClickEventSave {
    /**
     * Position X for the clicked Location on the grid
     */
    private Integer posX;
    /**
     * Position Y for the clicked location on the grid
     */
    private Integer posY;
    /**
     * Boolean, whether the clicked location is on the grid, if false its a hand position
     */
    private final boolean isGrid;
    /**
     * Position for the clicked location on the hand
     */
    private Integer handPosition;

    /**
     * Constructor for a Field position clicked
     *
     * @param posX pos-X on the grid
     * @param posY pos-y on the grid
     */
    public ClickEventSave(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
        this.isGrid = true;
    }

    /**
     * Constructor for a Hand position clicked
     *
     * @param handPosition hand index position
     */
    public ClickEventSave(Integer handPosition) {
        this.isGrid = false;

        this.handPosition = handPosition;
    }

    //----------------------------------------------Getter--------------------------------------------------------------

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
