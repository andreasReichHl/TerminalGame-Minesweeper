package de.example;

import java.util.Scanner;

public class Main {

    public static PlayingField game;

    public static void main(String[] args) {
        displayStart();
        boolean isUserInput = false;
        Scanner scanner = new Scanner(System.in);

        while (!isUserInput) {
            System.out.println("Anfänger = 1, Fortgeschritten = 2, Profi = 3 | Anleitung = 9");
            System.out.println("Deine Auswahl:");
            int userChoice = convertingStartInput(scanner.next());
            if (userChoice == -1) {
                return;
            } else if (userChoice == 9) {
                System.out.println("Anleitung");
            } else if (userChoice > 0 && userChoice <= 3) {
                game = setPlayingField(userChoice);
                isUserInput = true;
            } else {
                System.out.println("Falsche Eingabe!");
            }
        }

        Scanner scanner1 = new Scanner(System.in);
        int gameRun = 0;
        while (true) {
            clearScreen();
            game.printArray();
            System.out.println("Mine:" + game.getMines());
            System.out.println("Feld aufdecken = (r Reihe Spalte) und Markieren = (f Reihe Spalte)");
            System.out.println("Gib deinen Zug ein:");
            String userInput = scanner1.nextLine();
            if (userInput.equals("EXIT")) return;
            gameRun = convertingWhileRun(userInput);
            if (gameRun == -1) {
                game.clearThePlayingField();
                game.printArray();
                System.out.println("Leider verloren!");
                return;
            }

        }
//scanner1.close();
//        scanner.close();
    }

    public static int convertingStartInput(String userInput) {
        if (userInput.equals("EXIT")) return -1;
        if (userInput.length() == 1 && Character.isDigit(userInput.charAt(0)))
            return Integer.parseInt(String.valueOf(userInput.charAt(0)));
        return 0;
    }

    public static int convertingWhileRun(String userInput) {
        if (userInput.length() >= 5 && userInput.charAt(1) == ' ' &&
                (userInput.startsWith("f ") || userInput.startsWith("r "))) {

            String[] input = userInput.split(" ");
            String letter = input[0];
            int row = Integer.parseInt(input[1]);
            int column = Integer.parseInt(input[2]);

            if (row < game.getRow() && column < game.getColumn()) {
                return setUncoverField(letter, row, column);
            }
        } else {
            System.out.println("Falsche Eingabe!");
        }
        return 0;
    }

    public static int setUncoverField(String letter, int row, int column) {
        char[][] gameFieldUser = game.getFieldUser();
        char[][] gameField = game.getField();

        if (letter.equals("f")) {
            gameFieldUser[row][column] = 'F';
            game.setMines(game.getMines() - 1);
            return 0;
        }

        if (gameFieldUser[row][column] != '.') return 0;
        if (gameField[row][column] == '*') return -1; // Game End

        game.clearEmptyFields(row, column); //

        if (letter.equals("r")) {
            gameFieldUser[row][column] = gameField[row][column];
        }
        return 0;
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
                break;
        }
        return new PlayingField(rows, column, mine);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void displayStart() {
        String[] minesweeperArt = {
                "███    ███ ██ ███    ██ ███████ ███████ ██     ██ ███████ ███████ ██████  ███████ ██████  ",
                "████  ████ ██ ████   ██ ██      ██      ██     ██ ██      ██      ██   ██ ██      ██   ██ ",
                "██ ████ ██ ██ ██ ██  ██ █████   ███████ ██  █  ██ █████   █████   ██████  █████   ██████  ",
                "██  ██  ██ ██ ██  ██ ██ ██           ██ ██ ███ ██ ██      ██      ██      ██      ██   ██ ",
                "██      ██ ██ ██   ████ ███████ ███████  ███ ███  ███████ ███████ ██      ███████ ██   ██ "

        };
        clearScreen();
        for (String line : minesweeperArt) {
            System.out.println(line);
        }
        System.out.println();
        System.out.println("Wähle dein Schwierigkeitsgrad:");
        System.out.println("Mit der Eingabe von EXIT kannst du jederzeit das Spiel verlassen.");
    }
}