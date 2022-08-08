package logic;

/**
 * Record for a Calculation
 *
 * @param pointsChange Change in points for the move
 * @param gameWinning  will the move win the game
 * @author Jacob Klövekorn
 */
public record Calculation(Integer pointsChange, boolean isCreatingLoss, boolean gameWinning) {
}
