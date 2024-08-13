package de.example;

import java.util.ArrayList;
import java.util.List;

public class PlayingField {

    private final int column;
    private final int row;
    private final char[][] field;
    private final char[][] fieldUser;
    private final char MINE = '*';
    private final char HIDDENFIELD = '.';
    private final char BUOY = 'F';
    private final char EMPTY_FIELD = '0';
    private int mines;

    public PlayingField(int rows, int column, int mine) {
        this.column = column;
        this.row = rows;
        this.mines = mine;
        this.field = charsArray(EMPTY_FIELD);
        this.fieldUser = charsArray(HIDDENFIELD);
        mineInField(mine);
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
            if (this.field[randomRow][randomColumn] == MINE) return mineInField(mines);
            this.field[randomRow][randomColumn] = MINE;
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
            System.out.print(i + " ");
            if (i < 10) System.out.print(" ");
            for (int j = 0; j < column; j++) {
                System.out.print(fieldUser[i][j] + " ");
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
                if (temp != MINE) {
                    int number = temp - '0' + 1;
                    field[startRow][j] = (char) ('0' + number);
                }

            }
            ++startRow;
        }
    }

    public void clearEmptyFields(int row, int column) {
        if (field[row][column] != EMPTY_FIELD) return;
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
                    if (temp == EMPTY_FIELD && tempUser != EMPTY_FIELD) {
                        fieldUser[startRow][j] = EMPTY_FIELD;
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

    public void clearThePlayingField() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (fieldUser[i][j] == HIDDENFIELD) {
                    fieldUser[i][j] = field[i][j];
                }
                if (fieldUser[i][j] == BUOY && field[i][j] != MINE)
                    fieldUser[i][j] = '#';
            }
        }
    }

    public int checkWin() {
        for (int i = 0; i < fieldUser.length; i++) {
            for (int j = 0; j < fieldUser[0].length; j++) {
                if (fieldUser[i][j] == HIDDENFIELD) ;
                return 0;
            }
        }
        return -1;
    }

}
