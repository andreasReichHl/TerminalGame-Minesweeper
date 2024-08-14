package de.example;

import java.util.Scanner;

public class Main {

    public static PlayingField playingField;
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";


    public static void main(String[] args) {
        displayStart();

        Scanner scanner = new Scanner(System.in);
        if (selectDifficultLevel(scanner)) return;

        Scanner scanner1 = new Scanner(System.in);
        gameBoard(scanner1);

        scanner1.close();
        scanner.close();
    }

    private static boolean selectDifficultLevel(Scanner scanner) {
        while (true) {
            System.out.println("Anfänger = 1, Fortgeschritten = 2, Profi = 3 | Anleitung = 9");
            System.out.println("Deine Auswahl:");
            int userChoice = convertingStartInput(scanner.next());

            switch (userChoice) {
                case -1:
                    return true;
                case 9:
                    printInstructions();
                    break;
                case 1:
                case 2:
                case 3:
                    playingField = setPlayingField(userChoice);
                    return false;
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }

    private static void gameBoard(Scanner scanner1) {
        while (true) {
            GameStatistics stats = new GameStatistics();
            playingField.printArray();
            System.out.println("Mine: " + playingField.getMines());
            System.out.println(BOLD + "Feld aufdecken = r Reihe Spalte oder Flagge setzen/ entfernen = f Reihe Spalte" + RESET);
            System.out.println("Gib deinen Zug ein:");

            String userInput = scanner1.nextLine();
            if (userInput.equals("EXIT")) {
                System.out.println("Das Spiel wurde beendet!");
                return;
            }

            int gameRun = convertingInputWhileRun(userInput);
            if (gameRun == -1) {
                playingField.clearThePlayingField();
                playingField.printArray();
                stats.endGame();
                stats.printSummary(false);
                return;
            }

            int winStatus = playingField.evaluateGameWinStatus();
            if (winStatus == -1) {
                playingField.printArray();
                stats.endGame();
                stats.printSummary(true);
                return;
            }
        }
    }


    public static int convertingStartInput(String userInput) {
        if (userInput.equals("EXIT")) return -1;
        if (userInput.length() == 1 && Character.isDigit(userInput.charAt(0)))
            return Integer.parseInt(String.valueOf(userInput.charAt(0)));
        return 0;
    }

    public static int convertingInputWhileRun(String userInput) {
        if (userInput.length() >= 5 && userInput.charAt(1) == ' ' &&
                (userInput.startsWith("f ") || userInput.startsWith("r "))) {

            String[] input = userInput.split(" ");
            String letter = input[0];
            int row = Integer.parseInt(input[1]);
            int column = Integer.parseInt(input[2]);

            if (row < playingField.getRow() && column < playingField.getColumn()) {
                return setUncoverField(letter, row, column);
            }
        } else {
            System.out.println("Falsche Eingabe!");
        }
        return 0;
    }

    public static int setUncoverField(String letter, int row, int column) {
        char[][] gameFieldUser = playingField.getFieldUser();
        char[][] gameField = playingField.getField();

        if (row < 0 || row >= gameField.length || column < 0 || column >= gameField[0].length) {
            return 0;
        }

        if (letter.equals("r") && gameFieldUser[row][column] != Symbols.HIDDENFIELD.asChar()) {
            return 0;
        }

        switch (letter) {
            case "f":

                if (playingField.getMines() <= 0 && gameFieldUser[row][column] == Symbols.HIDDENFIELD.asChar()) {
                    return 0;
                }
                toggleFlag(gameFieldUser, row, column);
                return 0;

            case "r":
                if (gameField[row][column] == Symbols.MINE.asChar()) {
                    return -1;
                }
                playingField.clearEmptyFields(row, column);
                gameFieldUser[row][column] = gameField[row][column];
                return 0;

            default:
                return 0;
        }
    }

    public static void toggleFlag(char[][] gameFieldUser, int row, int column) {
        if (gameFieldUser[row][column] == Symbols.FLAG.asChar()) {
            gameFieldUser[row][column] = Symbols.HIDDENFIELD.asChar();
            playingField.setMines(playingField.getMines() + 1);
        } else if(gameFieldUser[row][column] == Symbols.HIDDENFIELD.asChar()){
            gameFieldUser[row][column] = Symbols.FLAG.asChar();
            playingField.setMines(playingField.getMines() - 1);
        }
    }


    public static PlayingField setPlayingField(int userChoice) {
        int rows = 0, column = 0, mine = 0;
        switch (userChoice) {
            case 1:
                rows = 10;
                column = 10;
                mine = 10;
                break;
            case 2:
                rows = 17;
                column = 17;
                mine = 40;
                break;
            case 3:
                rows = 17;
                column = 31;
                mine = 99;
                break;
            default:
                System.out.println("Falsche Eingabe");
                return null;
        }
        return new PlayingField(rows, column, mine);
    }

    public static void displayStart() {
        String[] minesweeperArt = {
                "███    ███ ██ ███    ██ ███████ ███████ ██     ██ ███████ ███████ ██████  ███████ ██████  ",
                "████  ████ ██ ████   ██ ██      ██      ██     ██ ██      ██      ██   ██ ██      ██   ██ ",
                "██ ████ ██ ██ ██ ██  ██ █████   ███████ ██  █  ██ █████   █████   ██████  █████   ██████  ",
                "██  ██  ██ ██ ██  ██ ██ ██           ██ ██ ███ ██ ██      ██      ██      ██      ██   ██ ",
                "██      ██ ██ ██   ████ ███████ ███████  ███ ███  ███████ ███████ ██      ███████ ██   ██ "

        };
        for (String line : minesweeperArt) {
            System.out.println(line);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Wähle dein Schwierigkeitsgrad:");
        sb.append("Mit der Eingabe von EXIT kannst du jederzeit das Spiel verlassen.");
        System.out.println(sb);
    }

    public static void printInstructions() {
        StringBuilder instructions = new StringBuilder();

        instructions.append(BOLD).append("Minesweeper Anleitung:").append(RESET).append("\n\n");

        instructions.append(BOLD).append("1. Spielfeld:").append(RESET).append("\n");
        instructions.append("   - Ein Gitter von Zellen, auf denen Minen zufällig verteilt sind.\n\n");

        instructions.append(BOLD).append("2. Ziel:").append(RESET).append("\n");
        instructions.append("   - Alle Zellen ohne Minen aufdecken. Vermeide Minen, um zu gewinnen.\n\n");

        instructions.append(BOLD).append("3. Feld aufdecken:").append(RESET).append("\n");
        instructions.append("   - Befehl: `r Reihe Spalte`\n");
        instructions.append("   - Beispiel: `r 3 4` deckt die Zelle in Reihe 3, Spalte 4 auf.\n\n");

        instructions.append(BOLD).append("4. Markieren von Minen:").append(RESET).append("\n");
        instructions.append("   - Befehl: `f Reihe Spalte`\n");
        instructions.append("   - Beispiel: `f 2 5` markiert die Zelle in Reihe 2, Spalte 5 mit einer Flagge.\n\n");

        instructions.append(BOLD).append("5. Gewinnen oder Verlieren:").append(RESET).append("\n");
        instructions.append("   - Gewinnen: Alle sicheren Zellen aufgedeckt, keine Minen aufgedeckt.\n");
        instructions.append("   - Verlieren: Auf eine Mine klicken.\n");

        System.out.println(instructions.toString());
    }
}