package game;

public enum Color {
    BLACK,
    BLUE,
    BROWN,
    GREEN,
    GREY,
    ORANGE,
    RED,
    PINK,
    PURPLE,
    YELLOW,
    WHITE;

    public static Color fromString(final String color) {
        return Color.valueOf(color.toUpperCase());
    }

}
