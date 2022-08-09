package logic;

import logic.util.AnimationTime;
import logic.util.TeamType;
import logic.util.TokenType;

/**
 * Interface for the gui Connector, implemented by the FXGUI class and the FakeGUI class
 *
 * @author Jacob Klövekorn
 */
public interface GUIConnector {
    void resetSpecialTokenImages();

    void setupDragAndDropEvent();

    void startGamePopUp();

    void changeCurrentAnimationTime(AnimationTime time);

    void updatePlayerHandIcons(int playerID, TokenType[] tokens);

    void faultyAlert(Integer caseID);

    void showHand(boolean isAI, int playerID, boolean hideAll);

    void resetText();

    void notifyTurn(String playerName, int playerID);

    void handVisibleSwitch(int playerID);

    void gameWonNotifier(TeamType wonType, int points, boolean rowComplete);

    void performMoveUIUpdate(int[] players, TokenType[][] tokens, TokenType[][] gameField, Integer[] pointsMap);

    void addTokenImagesForPlayer4(TokenType[] tokens);

    void addTokenImagesForPlayer3(TokenType[] tokens);

    void addTokenImagesForPlayer2(TokenType[] tokens);

    void addTokenImagesForPlayer1(TokenType[] tokens);

    void changeCurrentPlayerText(String playerName);

    void removerAmountText();

    void moverAmountText();

    void swapperAmountText();

    void replacerAmountText();

    void generateGrid();

    void updateSpecialTokenIcons(TokenType type);

    void placerAnimationFrame(int x, int y, TokenType type);

    void removerAnimationFrame(int x, int y);

    void showError(String message);
}
