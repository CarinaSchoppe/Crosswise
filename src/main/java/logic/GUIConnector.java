package logic;

import java.util.List;

public interface GUIConnector {
    void resetSpecialTokenImages();

    void setupDragAndDropEvent();

    void startGamePopUp();

    void changeCurrentAnimationTime(AnimationTime time);

    void updatePlayerHandIcons(int playerID, List<Token> tokens);

    void faultyAlert(Integer caseID);

    void showHand(boolean isAI, int playerID, boolean hideAll);

    void resetText();


    void notifyTurn(String playerName, int playerID);

    void handVisibleSwitch(int playerID);

    void gameWonNotifier(TeamType wonType, int points, boolean rowComplete);

    void performMoveUIUpdate(List<Player> players, TokenType[][] gameField, Integer[] pointsMap);

    void addTokenImagesForPlayer4(List<Token> tokens);

    void addTokenImagesForPlayer3(List<Token> tokens);

    void addTokenImagesForPlayer2(List<Token> tokens);

    void addTokenImagesForPlayer1(List<Token> tokens);

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

    void showGUIElements();
}
