/*
 * Copyright Notice for JacobSuperProfCrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:41 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "AnimationTime" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

package me.carinasophie.crosswise.util.constants;

/**
 * Enum for the time of the animation of an AI turn
 *
 * @author Carina Sophie Schoppe
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

