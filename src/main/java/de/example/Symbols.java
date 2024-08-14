package de.example;

public enum Symbols {
    MINE('*'),
    HIDDENFIELD('.'),
    FLAG('F'),
    EMPTY_FIELD('0'),
    WRONG_MARK('#');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    Symbols(char asChar) {
        this.asChar = asChar;
    }
}

