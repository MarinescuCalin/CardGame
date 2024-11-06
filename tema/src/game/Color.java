package game;

public enum Color {
    BLACK,
    BLUE,
    BROWN,
    GREY,
    ORANGE,
    RED,
    YELLOW,
    WHITE;

    public static Color fromString(final String color)
    {
        return Color.valueOf(color.toUpperCase());
    }

}
