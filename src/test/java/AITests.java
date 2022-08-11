/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:29 PM by Carina The Latest changes made by Carina on 8/9/22, 12:51 PM All contents of "AITests" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

import me.carinasophie.crosswise.game.AI;
import me.carinasophie.crosswise.game.Game;
import me.carinasophie.crosswise.util.Position;
import me.carinasophie.crosswise.util.TokenMove;
import me.carinasophie.crosswise.util.constants.TokenType;
import me.carinasophie.crosswise.util.filehandle.FileInputReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AITests {


    final FakeGUI gui = new FakeGUI();

    @Test
    void normalTurn1() {
        File file = new File("src/test/resources/configs/good/crosswise.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(4, 5), 3,
                TokenType.getTokenType(2), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurn2() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(2, 4), new Position(5,2),9,
                TokenType.getTokenType(9), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurn3() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 2), new Position(1,3), 8, TokenType.getTokenType(9), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurnEmptyField() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGameFinished.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 0), 0, TokenType.getTokenType(1),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void doWinningMoveHorizontalTeam() {
        File file = new File("src/test/resources/configs/good/horizontalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(3, 5), 1001, TokenType.getTokenType(2),
                true, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void doWinningMoveVerticalTeam() {
        File file = new File("src/test/resources/configs/good/verticalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(5, 0), -1002, TokenType.getTokenType(5),
                true, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preventLossHorizontalTeam() {
        File file = new File("src/test/resources/configs/good/verticalAlmostSixRowHMove.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(5, 0), 5, TokenType.getTokenType(4),
                false, true);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        TokenMove tokenMove = ai.calculateAIMove();

        assertEquals(move, tokenMove);
    }

    @Test
    void preventLossVerticalTeam() {
        File file = new File("src/test/resources/configs/good/horizontalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(3, 5), 4, TokenType.getTokenType(1),
                false, true);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preferTokenOnHandWithBiggerOccurrence() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGameFinished.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 0), 0, TokenType.getTokenType(6),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(2);
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preferLesserOccurrenceOnBoard() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinished2Tokens.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(1, 0), -6, TokenType.getTokenType(2),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(3);
        ai.getHandTokens();
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void chooseRemoverTurn() {
        File file = new File("src/test/resources/configs/good/removerTurn.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(1, 1), -5, TokenType.getTokenType(7),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        ai.getHandTokens();
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void chooseMoverTurn() {
        File file = new File("src/test/resources/configs/good/moverTurn.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(3, 2), new Position(5,1), -7, TokenType.getTokenType(8),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        ai.getHandTokens();
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void chooseSwapperTurn() {
        File file = new File("src/test/resources/configs/good/swapperTurn.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(2, 1), new Position(5,5), -12, TokenType.getTokenType(9),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        ai.getHandTokens();
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void chooseReplacerTurn() {
        File file = new File("src/test/resources/configs/good/replacerTurn.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(5, 1), new Position(1), -5, TokenType.getTokenType(10),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        ai.getHandTokens();
        TokenMove tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }


    @Test
    void checkColumnFor1Point() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-4), 1);
    }
    @Test
    void checkColumnFor3Point1() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-2), 3);
    }
    @Test
    void checkColumnFor3Point2() {
        File file = new File("src/test/resources/configs/good/pointsPerLineColumns.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-3), 3);
    }
    @Test
    void checkColumnFor5Point() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-3), 5);
    }
    @Test
    void checkColumnFor7Point() {
        File file = new File("src/test/resources/configs/good/pointsPerLineColumns.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-1), 7);
    }
    @Test
    void checkColumnForWin() {
        File file = new File("src/test/resources/configs/good/pointsPerLineColumns.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-5), -1000);
    }
    @Test
    void checkColumnFor2Point() {
        File file = new File("src/test/resources/configs/good/crosswise.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-1), 2);
    }
    @Test
    void checkColumnFor6Point1() {
        File file = new File("src/test/resources/configs/good/pointsPerLineColumns.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-2), 6);
    }
    @Test
    void checkColumnFor6Point2() {
        File file = new File("src/test/resources/configs/good/pointsPerLineColumns.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(-4), 6);
    }


    @Test
    void checkRowFor1Point() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(3), 1);
    }
    @Test
    void checkRowFor3Point1() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(1), 3);
    }
    @Test
    void checkRowFor3Point2() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(3), 3);
    }
    @Test
    void checkRowFor5Point() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(6), 5);
    }
    @Test
    void checkRowFor7Point() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(1), 7);
    }
    @Test
    void checkRowForWin() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(5), -1000);
    }
    @Test
    void checkRowFor2Point() {
        File file = new File("src/test/resources/configs/good/crosswise.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(5), 2);
    }
    @Test
    void checkRowFor6Point1() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(2), 6);
    }
    @Test
    void checkRowFor6Point2() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        assertEquals(map.get(4), 6);
    }

    @Test
    void checkTeamPoints1() {
        File file = new File("src/test/resources/configs/good/pointsPerLineRow.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, -978);
        assertEquals(rowCounter, 6);
    }

    @Test
    void checkTeamPoints2() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, 11);
        assertEquals(rowCounter, 9);
    }

    @Test
    void checkTeamPoints3() {
        File file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, 15);
        assertEquals(rowCounter, 13);
    }

    @Test
    void checkTeamPoints4() {
        File file = new File("src/test/resources/configs/good/verticalMorePoints.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, 25);
        assertEquals(rowCounter, 19);
    }

    @Test
    void checkTeamPoints5() {
        File file = new File("src/test/resources/configs/good/horizontalMorePoints.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, 19);
        assertEquals(rowCounter, 22);
    }

    @Test
    void checkTeamPoints6() {
        File file = new File("src/test/resources/configs/good/swapperTurn.json");
        FileInputReader.readFile(file, gui);
        Map<Integer, Integer> map = AI.calculateCurrentOverallPoints();
        int columnCounter = 0;
        int rowCounter = 0;
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            if (entry.getKey() > 0) {
                columnCounter = columnCounter + entry.getValue();
            } else {
                rowCounter = rowCounter + entry.getValue();
            }
        }
        assertEquals(columnCounter, 14);
        assertEquals(rowCounter, 13);
    }

}
