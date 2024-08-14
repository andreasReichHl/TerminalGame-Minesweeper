package de.example;

public class GameStatistics {
    private final long startTime;
    private long endTime;

    public GameStatistics() {
        this.startTime = System.currentTimeMillis();
    }

    public void endGame() {
        this.endTime = System.currentTimeMillis();
    }

    public void printSummary(boolean won) {
        long duration = (endTime - startTime) / 1000; // Dauer in Sekunden
        System.out.println("Spiel beendet!");
        System.out.println("Ergebnis: " + (won ? "Gewonnen" : "Verloren"));
        System.out.println("Dauer des Spiels: " + duration + " Sekunden");
//        System.out.println("Aufgedeckte Felder: " + fieldsRevealed);
//        System.out.println("Markierte Minen: " + minesMarked);
//        System.out.println("Falsch markierte Minen: " + falseMinesMarked);
//        System.out.println("Gesamtanzahl der Minen: " + totalMines);
//        System.out.println("Verbleibende Minen: " + (totalMines - minesMarked));
    }
}