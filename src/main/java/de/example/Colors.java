package de.example;

public enum Colors {
    RESET("\u001B[0m"),
    DARK_GRAY("\u001B[90m"),
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    DARK_BLUE("\u001B[94m"),
    RED  ("\033[0;31m"),
    YELLOW ("\u001B[33m"),
    MAGENTA ("\u001B[35m"),
    DARK_RED("\u001B[31m"),
    BLACK("\u001B[30m"),

    // Background

    RED_BACKGROUND("\u001B[41m"),
    BRIGHT_RED_BACKGROUND("\u001B[101m");


    private final String color;

    public String asColor(){
        return color;
    }

    Colors(String color) {
        this.color = color;
    }

}
