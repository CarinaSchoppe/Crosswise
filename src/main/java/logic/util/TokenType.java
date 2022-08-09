package logic.util;

/**
 * Enum for all different Types of tokens including a value, boolean, if they are special, an image path and a border
 * image path
 *
 * @author Jacob Klövekorn
 */
public enum TokenType {
    NONE(0, false, "pictures/0none.png", "pictures/0noneBorder.png", "pictures/0noneHighlight.png"),
    SUN(1, false, "pictures/1sun.png", "pictures/1sunBorder.png", "pictures/1sunHighlight.png"),
    CROSS(2, false, "pictures/2cross.png", "pictures/2crossBorder.png", "pictures/2crossHighlight.png"),
    TRIANGLE(3, false, "pictures/3triangle.png", "pictures/3triangleBorder.png", "pictures/3triangleHighlight.png"),
    SQUARE(4, false, "pictures/4square.png", "pictures/4squareBorder.png", "pictures/4squareHighlight.png"),
    PENTAGON(5, false, "pictures/5pentagon.png", "pictures/5pentagonBorder.png", "pictures/5pentagonHighlight.png"),
    STAR(6, false, "pictures/6star.png", "pictures/6starBorder.png", "pictures/6starHighlight.png"),
    REMOVER(7, true, "pictures/7remove.png", "pictures/7removeBorder.png", "pictures/1sunHighlight.png"),
    MOVER(8, true, "pictures/8move.png", "pictures/8moveBorder.png", "pictures/1sunHighlight.png"),
    SWAPPER(9, true, "pictures/9swaponboard.png", "pictures/9swaponboardBorder.png", "pictures/1sunHighlight.png"),
    REPLACER(10, true, "pictures/10swapwithhand.png", "pictures/10swapwithhandBorder.png", "pictures/1sunHighlight.png");


    private final int value;
    private final boolean special;
    private final String imagePathNormal;
    private final String imagePathGolden;
    private final String imagePathHighlight;

    TokenType(int value, boolean special, String imagePathNormal, String imagePathGolden, String imagePathHighlight) {
        this.value = value;
        this.special = special;
        this.imagePathNormal = imagePathNormal;
        this.imagePathGolden = imagePathGolden;
        this.imagePathHighlight = imagePathHighlight;
    }

    public static TokenType getTokenType(int token) {
        for (TokenType type : TokenType.values()) {
            if (type.getValue() == token) {
                return type;
            }
        }
        return NONE;
    }

    public static TokenType getTokenTypeFromString(String input) {
        return switch (input) {
            case "REMOVER" -> REMOVER;
            case "MOVER" -> MOVER;
            case "SWAPPER" -> SWAPPER;
            case "REPLACER" -> REPLACER;
            case "SUN" -> SUN;
            case "CROSS" -> CROSS;
            case "TRIANGLE" -> TRIANGLE;
            case "SQUARE" -> SQUARE;
            case "PENTAGON" -> PENTAGON;
            case "STAR" -> STAR;
            case "NONE" -> NONE;
            default -> throw new IllegalArgumentException("Invalid token type");
        };
    }

    public String getImagePathNormal() {
        return imagePathNormal;
    }

    public String getImagePathHighlight() {
        return imagePathHighlight;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getValue() {
        return this.value;
    }

    public String getImagePathGolden() {
        return imagePathGolden;
    }
}
