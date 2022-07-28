/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/28/22, 4:49 PM by Carina The Latest changes made by Carina on 7/28/22, 4:49 PM All contents of "AnimationTIme" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.util.game;

public enum AnimationTime {
    SLOW(2.0f), MIDDLE(1.0f), FAST(0.5f);

    private final float time;

    AnimationTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return time;
    }
}

