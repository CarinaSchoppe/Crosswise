import gui.fileHandle.FileInputReader;
import logic.AI;
import logic.Game;
import logic.util.Position;
import logic.util.TokenMove;
import logic.util.TokenType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AITests {


    final FakeGUI gui = new FakeGUI();

    @Test
    void normalTurn1() {
        var file = new File("src/test/resources/configs/good/crosswise.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(4, 5), 3,
                TokenType.getTokenType(2), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurn2() {
        var file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(2, 4), new Position(5,2),9,
                TokenType.getTokenType(9), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurn3() {
        var file = new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 2), new Position(1,3), 8, TokenType.getTokenType(9), false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void normalTurnEmptyField() {
        var file = new File("src/test/resources/configs/good/crosswiseNotFinishedGameFinished.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 0), 0, TokenType.getTokenType(1),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void doWinningMoveHorizontalTeam() {
        var file = new File("src/test/resources/configs/good/horizontalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(3, 5), 1001, TokenType.getTokenType(2),
                true, false);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void doWinningMoveVerticalTeam() {
        var file = new File("src/test/resources/configs/good/verticalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(5, 0), -1002, TokenType.getTokenType(5),
                true, false);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preventLossHorizontalTeam() {
        var file = new File("src/test/resources/configs/good/verticalAlmostSixRowHMove.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(5, 0), 5, TokenType.getTokenType(4),
                false, true);
        AI ai = (AI) Game.getGame().getPlayers().get(1);
        var tokenMove = ai.calculateAIMove();

        assertEquals(move, tokenMove);
    }

    @Test
    void preventLossVerticalTeam() {
        var file = new File("src/test/resources/configs/good/horizontalAlmostSixRow.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(3, 5), 4, TokenType.getTokenType(1),
                false, true);
        AI ai = (AI) Game.getGame().getPlayers().get(0);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preferTokenOnHandWithBiggerOccurrence() {
        var file = new File("src/test/resources/configs/good/crosswiseNotFinishedGameFinished.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(0, 0), 0, TokenType.getTokenType(6),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(2);
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }

    @Test
    void preferLesserOccurrenceOnBoard() {
        var file = new File("src/test/resources/configs/good/crosswiseNotFinished2Tokens.json");
        FileInputReader.readFile(file, gui);
        TokenMove move = new TokenMove(new Position(1, 0), -6, TokenType.getTokenType(2),
                false, false);
        AI ai = (AI) Game.getGame().getPlayers().get(3);
        ai.getHandTokens();
        var tokenMove = ai.calculateAIMove();
        assertEquals(move, tokenMove);
    }
}
