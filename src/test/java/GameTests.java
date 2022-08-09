import gui.fileHandle.FileInputReader;
import gui.CrossWise;
import logic.Game.Game;
import logic.Game.Team;
import logic.ConstantsEnums.TeamType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for tests checking the game class
 *
 * @author Jacob Klövekorn
 */
class GameTests {

    @BeforeEach
    void setup() {
        CrossWise.UI = false;
        if (Game.getGame() != null)
            Game.getGame().cancel();
    }

    @Test
    void correctCompile() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise.json"), new FakeGUI());
        Assertions.assertDoesNotThrow(() -> Game.getGame().testStart(false));
    }

    @Test
    void wrongConfig3Player() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(true, false, true, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void wrongConfig1Player() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, false, false, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void wrongConfig0Players() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, false, false, false), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }

    @Test
    void wrongConfig2PlayersSameTeam() {
        Game.createNewGame(List.of("test1", "test2", "test3", "test4"), List.of(true, true, true, true), List.of(false, true, false, true), new FakeGUI(), false, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Game.getGame().testStart(false));
    }


    @Test
    void testGameNotFinishedStandard() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswise.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        map.put(false, null);
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }

    @Test
    void testGameNotFinishedEmpty() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswiseNotFinishedGameFinished.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        map.put(false, null);
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }


    @Test
    void testGameNotFinished1() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswiseNotFinishedGame2.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        map.put(false, null);
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }

    @Test
    void testGameNotFinished2() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswiseFinished1.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        map.put(true, Team.getVerticalTeam());
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }

    @Test
    void testGameNotFinished3() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswiseNotFinishedGame1.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        map.put(false, null);
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }


    @Test
    void testGameFinished1() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/crosswiseFinished2.json"), new FakeGUI());
        Game.getGame().setup(true);
        Map<Boolean, Team> map = new HashMap<>();
        Team team = new Team(TeamType.HORIZONTAL);
        map.put(true, team);
        Assertions.assertEquals(map, Game.getGame().isGameOver());
    }



    @Test
    void testForVertical6ColumnWin() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/verticalSixRow.json"), new FakeGUI());
        Game.getGame().setup(true);
        Team team = new Team(TeamType.VERTICAL);
        team.getPlayers().addAll(Team.getVerticalTeam().getPlayers());
        team.setRowWin(true);
        Assertions.assertTrue(teamsEqual(team, Game.getGame().isGameOver().get(true)));
    }

    @Test
    void testForDraw() {
        FileInputReader.readFile(new File("src/test/resources/configs/good/drawGameFinished.json"), new FakeGUI());
        Game.getGame().setup(true);
        Team team = new Team(TeamType.VERTICAL);
        team.getPlayers().addAll(Team.getVerticalTeam().getPlayers());
        team.setRowWin(false);
        Assertions.assertNull(Game.getGame().isGameOver().get(true));
    }


    private boolean teamsEqual(Team t1, Team t2) {
        return t1.equals(t2);
    }
}
