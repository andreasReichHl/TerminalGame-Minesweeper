package de.example;

import java.util.Scanner;

public class Main {

    public static PlayingField playingField;
    public static GameStatistics stats;
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
            System.out.println("\nWähle dein Schwierigkeitsgrad:");
            System.out.println("Anfänger = 1, Fortgeschritten = 2, Profi = 3,  Benutzerdefiniert = 4 | Anleitung = 9");
            System.out.println("Mit der Eingabe von EXIT kannst du jederzeit das Spiel verlassen.");
            ;
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
                case 4:
                    customGame(scanner);
                    return false;
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }

    private static void customGame(Scanner scanner) {
        System.out.println("Benutzerdefiniert - Anzahl der Reihen, Spalten und Minen eingeben.");
        int row = 0, column = 0, mine = 0;

        while (true) {
            System.out.println("Anzahl der Reihen (min: 5 | max: 25): ");
            row = scanner.nextInt();
            if (row >= 5 && row <= 25) break;
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen 5 und 25 ein.");
        }

        while (true) {
            System.out.println("Anzahl der Spalten (min: 5 | max: 40):");
            column = scanner.nextInt();
            if (column >= 5 && column <= 40) break;
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen 5 und 40 ein.");
        }

        while (true) {
            int maxMine = row * column / 6;
            if (maxMine > 200) maxMine = 200;
            System.out.println("Anzahl der Minen (min:1 | max:" + maxMine + "): ");
            mine = scanner.nextInt();
            if (mine > 0 && mine <= maxMine) break;
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen 1 und 200 ein.");
        }

        getPlayingField(row, column, mine);
    }


    private static void gameBoard(Scanner scanner1) {
        stats = new GameStatistics();
        stats.setStartTime();
        while (true) {
            playingField.printArray();
            System.out.println("Mine: " + playingField.getMines() + " | Züge: " + stats.getMoves());
            System.out.println(BOLD + "Feld aufdecken = r Reihe Spalte oder Flagge setzen/ entfernen = f Reihe Spalte" + RESET);
            System.out.println("Gib deinen Zug ein:");
            String userInput = scanner1.nextLine();
            if (userInput.equals("EXIT")) {
                System.out.println("Das Spiel wurde beendet!");
                return;
            }

            int gameRun = convertingInputWhileRun(userInput, stats);
            if (gameRun == -1) {
                playingField.clearThePlayingField(false);
                playingField.printArray();
                stats.endGame();
                stats.printSummary(false);
                return;
            }


            int winStatus = playingField.evaluateGameWinStatus();
            if (winStatus == -1) {
                playingField.clearThePlayingField(true);
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

    public static int convertingInputWhileRun(String userInput, GameStatistics stats) {
        userInput = userInput.strip();
        if (userInput.length() >= 5 && userInput.charAt(1) == ' ' &&
                (userInput.startsWith("f ") || userInput.startsWith("r "))) {

            String[] input = userInput.split(" ");
            String letter = input[0];
            int row = Integer.parseInt(input[1]);
            int column = Integer.parseInt(input[2]);

            if (row < playingField.getRow() && column < playingField.getColumn()) {
                return setUncoverField(letter, row, column, stats);
            }
        } else {
            System.out.println("Falsche Eingabe!");
        }
        return 0;
    }

    public static int setUncoverField(String letter, int row, int column, GameStatistics stats) {
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
                stats.setMoves();
                return 0;

            case "r":
                if (gameField[row][column] == Symbols.MINE.asChar()) {
                    return -1;
                }
                playingField.clearEmptyFields(row, column);
                gameFieldUser[row][column] = gameField[row][column];
                stats.setMoves();
                return 0;

            default:
                return 0;
        }
    }

    public static void toggleFlag(char[][] gameFieldUser, int row, int column) {
        if (gameFieldUser[row][column] == Symbols.FLAG.asChar()) {
            gameFieldUser[row][column] = Symbols.HIDDENFIELD.asChar();
            playingField.setMines(playingField.getMines() + 1);
        } else if (gameFieldUser[row][column] == Symbols.HIDDENFIELD.asChar()) {
            gameFieldUser[row][column] = Symbols.FLAG.asChar();
            playingField.setMines(playingField.getMines() - 1);
        }
    }


    public static PlayingField setPlayingField(int userChoice) {
        int row = 0, column = 0, mine = 0;
        switch (userChoice) {
            case 1:
                row = 10;
                column = 10;
                mine = 10;
                break;
            case 2:
                row = 17;
                column = 17;
                mine = 40;
                break;
            case 3:
                row = 17;
                column = 31;
                mine = 99;
                break;
            default:
                System.out.println("Falsche Eingabe");
                return null;
        }
        return getPlayingField(row, column, mine);
    }

    private static PlayingField getPlayingField(int rows, int column, int mine) {
        playingField = new PlayingField(rows, column, mine);
        return playingField;
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
    }

    public static void printInstructions() {

        String instructions = BOLD + "Minesweeper Anleitung:" + RESET + "\n\n" +
                BOLD + "1. Spielfeld:" + RESET + "\n" +
                "   - Ein Gitter von Zellen, auf denen Minen zufällig verteilt sind.\n\n" +
                BOLD + "2. Ziel:" + RESET + "\n" +
                "   - Alle Zellen ohne Minen aufdecken. Vermeide Minen, um zu gewinnen.\n\n" +
                BOLD + "3. Feld aufdecken:" + RESET + "\n" +
                "   - Befehl: `r Reihe Spalte`\n" +
                "   - Beispiel: `r 3 4` deckt die Zelle in Reihe 3, Spalte 4 auf.\n\n" +
                BOLD + "4. Markieren von Minen:" + RESET + "\n" +
                "   - Befehl: `f Reihe Spalte`\n" +
                "   - Beispiel: `f 2 5` markiert die Zelle in Reihe 2, Spalte 5 mit einer Flagge.\n\n" +
                BOLD + "5. Gewinnen oder Verlieren:" + RESET + "\n" +
                "   - Gewinnen: Alle sicheren Zellen aufgedeckt, keine Minen aufgedeckt.\n" +
                "   - Verlieren: Auf eine Mine klicken.\n";

        System.out.println(instructions);
    }
}