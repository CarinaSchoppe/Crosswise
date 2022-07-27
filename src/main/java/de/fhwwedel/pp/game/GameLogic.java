/*
 * Copyright Notice for Crosswise-PP
 * Copyright (c) at Crosswise-Jacob 2022
 * File created on 7/27/22, 11:22 AM by Carina The Latest changes made by Carina on 7/27/22, 11:21 AM All contents of "GameLogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at Crosswise-Jacob. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Crosswise-Jacob.
 */

package de.fhwwedel.pp.game;

import de.fhwwedel.pp.util.game.Team;
import de.fhwwedel.pp.util.game.TokenType;

import java.util.HashMap;

public record GameLogic(Game game) {


    public HashMap<Boolean, Team> isGameOver(PlayingField field) {
        var map = new HashMap<Boolean, Team>();
        if (checkRows(field)) {
            map.put(true, Team.VERTICAL);
            return map;
        }

        if (checkColumns(field)) {
            map.put(true, Team.HORIZONTAL);
            return map;
        }

        //all filled
        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (field.getFieldMap()[i][j].getToken() == null) {
                    map.put(false, null);
                    return map;
                }
            }
        }
        map.put(true, null);
        return map;
    }


    @SuppressWarnings("DuplicatedCode")
    private boolean checkRows(final PlayingField field) {
        TokenType current = null;
        boolean equal = true;
        for (int i = 0; i < field.getSize(); i++) {
            equal = true;
            for (int j = 0; j < field.getSize(); j++) {
                if (current == null) {
                    if (field.getFieldMap()[i][j].getToken() != null) {
                        current = field.getFieldMap()[i][j].getToken().getTokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (field.getFieldMap()[i][j].getToken() == null || field.getFieldMap()[i][j].getToken().getTokenType() != current) {
                        equal = false;
                        current = null;
                        break;
                    }
                }
            }
        }
        return equal;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean checkColumns(final PlayingField field) {
        TokenType current = null;
        boolean equal = true;
        for (int i = 0; i < field.getSize(); i++) {
            equal = true;
            for (int j = 0; j < field.getSize(); j++) {
                if (current == null) {
                    if (field.getFieldMap()[j][i].getToken() != null) {
                        current = field.getFieldMap()[j][i].getToken().getTokenType();
                    } else {
                        equal = false;
                        break;
                    }
                } else {
                    if (field.getFieldMap()[j][i].getToken() == null || field.getFieldMap()[j][i].getToken().getTokenType() != current) {
                        equal = false;
                        current = null;
                        break;
                    }
                }
            }
        }
        return equal;
    }


}
