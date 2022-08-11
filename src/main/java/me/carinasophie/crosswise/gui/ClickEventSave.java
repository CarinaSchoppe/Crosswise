/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "ClickEventSave" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.gui;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for a Clicked Position either on the grid or on the hand of the current player
 *
 * @author Jacob Klövekorn
 */
@Getter
@Setter
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
     * Boolean, whether the clicked location is on the grid, if false it's a hand position
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

}
