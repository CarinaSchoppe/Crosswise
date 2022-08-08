package logic.util;

public enum TokenType {
    NONE(0, false, "pictures/0none.png", "pictures/0noneBorder.png"),
    SUN(1, false, "pictures/1sun.png", "pictures/1sunBorder.png"),
    CROSS(2, false, "pictures/2cross.png", "pictures/2crossBorder.png"),
    TRIANGLE(3, false, "pictures/3triangle.png", "pictures/3triangleBorder.png"),
    SQUARE(4, false, "pictures/4square.png", "pictures/4squareBorder.png"),
    PENTAGON(5, false, "pictures/5pentagon.png", "pictures/5pentagonBorder.png"),
    STAR(6, false, "pictures/6star.png", "pictures/6starBorder.png"),
    REMOVER(7, true, "pictures/7remove.png", "pictures/7removeBorder.png"),
    MOVER(8, true, "pictures/8move.png", "pictures/8moveBorder.png"),
    SWAPPER(9, true, "pictures/9swaponboard.png", "pictures/9swaponboardBorder.png"),
    REPLACER(10, true, "pictures/10swapwithhand.png", "pictures/10swapwithhandBorder.png");


    private final int value;

    private final boolean special;
    private final String imagePathNormal;
    private final String imagePathGolden;

    TokenType(int value, boolean special, String imagePathNormal, String imagePathGolden) {
        this.value = value;
        this.special = special;
        this.imagePathNormal = imagePathNormal;
        this.imagePathGolden = imagePathGolden;
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
