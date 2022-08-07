import logic.*;

import java.util.List;

public class FakeGUI implements GUIConnector {


    @Override
    public void resetText() {

    }

    @Override
    public void placerAnimationFrame(int x, int y, TokenType type) {

    }

    @Override
    public void removerAnimationFrame(int x, int y) {

    }

    @Override
    public void showError(String string) {

    }

    @Override
    public void setupDragAndDropEvent() {

    }

    @Override
    public void startGamePopUp() {

    }

    public void faultyAlert(Integer caseID) {

    }

    @Override
    public void changeCurrentAnimationTime(AnimationTime time) {

    }

    @Override
    public void updatePlayerHandIcons(int playerID, List<Token> tokens) {

    }

    @Override
    public void showHand(boolean isAI, int playerID, boolean hideAll) {

    }

    @Override
    public void notifyTurn(String playerName, int playerID) {

    }

    @Override
    public void handVisibleSwitch(int playerID) {

    }

    @Override
    public void gameWonNotifier(TeamType wonType, int points, boolean rowComplete) {

    }

    @Override
    public void performMoveUIUpdate(List<Player> players, TokenType[][] gameField, Integer[] pointsMap) {

    }

    @Override
    public void addTokenImagesForPlayer4(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer3(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer2(List<Token> tokens) {

    }

    @Override
    public void addTokenImagesForPlayer1(List<Token> tokens) {

    }

    @Override
    public void changeCurrentPlayerText(String playerName) {

    }

    @Override
    public void removerAmountText() {

    }

    @Override
    public void moverAmountText() {

    }

    @Override
    public void swapperAmountText() {

    }

    @Override
    public void replacerAmountText() {

    }

    @Override
    public void generateGrid() {

    }

    @Override
    public void updateSpecialTokenIcons(TokenType type) {

    }


    public void showGUIElements() {

    }
}
