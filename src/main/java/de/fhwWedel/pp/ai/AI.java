/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/26/22, 4:39 PM All contents of "AI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwWedel.pp.ai;

import de.fhwWedel.pp.player.Player;

public class AI extends Player {
    public AI(int playerID, boolean active, String name) {
        super(playerID, active, name);
    }


    public void makeMove() {

        /*
        TODO: Best turn!
        Restrictions:
        - Immer zug machen wenn zug gewinn machbar
        - Zug machen wenn gegner zug gewinn möglich
        - Zug machen der am meisten Punkte bringt

        - lieber normaler zug als special
        - wenn 2 züge gleich dann nimm den mit mehr tokens auf hand
            - gleich? nimm den wo weniger auf dem feld liegt
             - wenn gleich? nimm stein mit der geringsten zahl
               - wenn gleich? nimm feld ganz oben links dann nach rechts dann unten
         */


    }

}
