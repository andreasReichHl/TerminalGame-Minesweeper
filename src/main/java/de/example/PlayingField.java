package de.example;

public class PlayingField {

    private final int column;
    private final int row;
    private final char[][] field;
    private final char[][] fieldUser;
    private final char MINE = '*';
    private final char HIDDENFIELD = '.';
    private final char BUOY = 'F';
    private final char EMPTY_FIELD = '0';


    public PlayingField(int column, int width) {
        this.column = column;
        this.row = width;
        int mines = numberOfMines(column, width);
        this.field = charsArray(EMPTY_FIELD);
        this.fieldUser = charsArray(HIDDENFIELD);
        setMines(mines);
    }

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

    private int setMines(int mines) {
        while (mines > 0) {
            int randomColumn = (int) (Math.random() * this.column);
            int randomRow = (int) (Math.random() * this.row);
            if (this.field[randomRow][randomColumn] == MINE) return setMines(mines);
            this.field[randomRow][randomColumn] = MINE;
            determineTheNumbers(randomRow,randomColumn);
            --mines;
        }
        return 0;
    }

    public void printArray() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void determineTheNumbers(int mineRow, int mineColumn) {
        int startRow = Math.max(0, mineRow - 1);
        int startColumn = Math.max(0, mineColumn- 1);
        int endRow = Math.min(field.length, mineRow+2);
        int endColumn = Math.min(field.length, mineColumn+2);

        while (startRow < endRow) {

            for( int j = startColumn;j < endColumn; j++) {
                char temp = field[startRow][j];
                if (temp != MINE) {
                    int number = temp - '0' + 1;
                    field[startRow][j] = (char) ('0' + number);
                }

            }
            ++startRow;
        }
    }
}
