package de.example;

import java.util.ArrayList;
import java.util.List;

public class PlayingField {

    private final int column;
    private final int row;
    private final char[][] field;
    private final char[][] fieldUser;


    private int mines;

    public PlayingField(int rows, int column, int mine) {
        this.column = column;
        this.row = rows;
        this.mines = mine;
        this.field = charsArray(Symbols.EMPTY_FIELD.asChar());
        this.fieldUser = charsArray(Symbols.HIDDENFIELD.asChar());
        mineInField(mines);
    }

    public int getMines() {
        return mines;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public char[][] getField() {
        return field;
    }

    public char[][] getFieldUser() {
        return fieldUser;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
    //

    private int numberOfMines(int length, int width) {
        return length * width / 5;
    }

    private char[][] charsArray(char c) {
        char[][] arr = new char[this.row][this.column];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < column; j++) {
                arr[i][j] = c;
            }
        }
        return arr;
    }

    private int mineInField(int mines) {
        while (mines > 0) {
            int randomColumn = (int) (Math.random() * this.column);
            int randomRow = (int) (Math.random() * this.row);
            if (this.field[randomRow][randomColumn] == Symbols.MINE.asChar()) return mineInField(mines);
            this.field[randomRow][randomColumn] = Symbols.MINE.asChar();
            determineTheNumbers(randomRow, randomColumn);
            --mines;
        }
        return 0;
    }

    public void printArray() {
        System.out.print("   ");
        for (int j = 0; j < column; j++) {
            if (j >= 10) {
                System.out.print(j / 10 + " ");
            } else {
                System.out.print("  ");
            }
        }
        System.out.println();

        System.out.print("   ");
        for (int j = 0; j < column; j++) {
            System.out.print(j % 10 + " ");
        }
        System.out.println();


        for (int i = 0; i < row; i++) {
            System.out.print((i < 10 ? " " : "") + i + " ");

            for (int j = 0; j < column; j++) {
                char currentField = fieldUser[i][j];
                switch (currentField) {
                    case '0':
                        System.out.print(Colors.DARK_GRAY.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case '1':
                        System.out.print(Colors.BLUE.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case '2':
                        System.out.print(Colors.GREEN.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case '3':
                        System.out.print(Colors.RED.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case '4':
                        System.out.print(Colors.DARK_BLUE.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case '5':
                        System.out.print(Colors.DARK_RED.asColor() + currentField + Colors.RESET.asColor() + " ");
                        break;
                    case 'F':
                        System.out.print("🚩");
                        break;
                    case '*':
                        System.out.print("💣");  // Mine
                        break;
                    case '#':
                        System.out.print(Colors.RED_BACKGROUND.asColor() + Colors.BLACK.asColor() + "✘" + Colors.RESET.asColor());  // Mine
                        break;
                    case '.':
                    default:
                        System.out.print(currentField + " ");
                        break;
                }
            }
            System.out.println();
        }
    }


    private void determineTheNumbers(int mineRow, int mineColumn) {
        int startRow = Math.max(0, mineRow - 1);
        int startColumn = Math.max(0, mineColumn - 1);
        int endRow = Math.min(field.length, mineRow + 2);
        int endColumn = Math.min(field[0].length, mineColumn + 2);

        while (startRow < endRow) {

            for (int j = startColumn; j < endColumn; j++) {
                char temp = field[startRow][j];
                if (temp != Symbols.MINE.asChar()) {
                    int number = temp - '0' + 1;
                    field[startRow][j] = (char) ('0' + number);
                }

            }
            ++startRow;
        }
    }

    public void clearEmptyFields(int row, int column) {
        if (field[row][column] != Symbols.EMPTY_FIELD.asChar()) return;
        List<int[][]> emptyFields = new ArrayList<>();

        int[][] startField = {{row, column}};
        emptyFields.add(startField);

        while (!emptyFields.isEmpty()) {
            int[][] tempArr = emptyFields.getFirst();
            row = tempArr[0][0];
            column = tempArr[0][1];
            int startRow = Math.max(0, row - 1);
            int startColumn = Math.max(0, column - 1);
            int endRow = Math.min(field.length, row + 2);
            int endColumn = Math.min(field[0].length, column + 2);

            while (startRow < endRow) {

                for (int j = startColumn; j < endColumn; j++) {
                    char temp = field[startRow][j];
                    char tempUser = fieldUser[startRow][j];
                    if (temp == Symbols.EMPTY_FIELD.asChar() && tempUser != Symbols.EMPTY_FIELD.asChar()) {
                        fieldUser[startRow][j] = Symbols.EMPTY_FIELD.asChar();
                        int[][] emptyField = {{startRow, j}};
                        emptyFields.add(emptyField);
                    } else {
                        fieldUser[startRow][j] = field[startRow][j];
                    }
                }
                ++startRow;
            }
            emptyFields.removeFirst();
        }
    }

    public void clearThePlayingField(boolean won) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (fieldUser[i][j] == Symbols.HIDDENFIELD.asChar()) {
                    if(won && field[i][j] == Symbols.MINE.asChar()){
                        fieldUser[i][j] = Symbols.FLAG.asChar();
                    }else {
                    fieldUser[i][j] = field[i][j];
                    }
                }
                if (fieldUser[i][j] == Symbols.FLAG.asChar() && field[i][j] != Symbols.MINE.asChar())
                    fieldUser[i][j] = Symbols.WRONG_MARK.asChar();
            }
        }
    }

    public int evaluateGameWinStatus() {
        for (int i = 0; i < fieldUser.length; i++) {
            for (int j = 0; j < fieldUser[0].length; j++) {
                if (fieldUser[i][j] == Symbols.HIDDENFIELD.asChar() && field[i][j] != Symbols.MINE.asChar()) {
                    return 0;
                }
            }
        }
        return -1;
    }

}
