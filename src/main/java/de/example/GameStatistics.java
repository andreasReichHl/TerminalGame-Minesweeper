package de.example;

public class GameStatistics {
    private long startTime;
    private long endTime;
    private int moves = 0;

    public GameStatistics() {
//        this.startTime = System.nanoTime();
    }

    public void setStartTime() {
        this.startTime = System.nanoTime();
    }
    public void endGame() {
        this.endTime = System.nanoTime();
    }

    public void setMoves() {
        this.moves++;
    }

    public int getMoves() {
        return moves;
    }

    public void printSummary(boolean won) {
        long duration = (endTime - startTime) / 1000000000; //
        long minutes = (duration % 3600) / 60;
        long seconds = duration % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        System.out.println("\nSpiel beendet!");
        System.out.println("Ergebnis: " + (won ? "Gewonnen" : "Verloren"));
        System.out.println("Dauer des Spiels: " + timeString);
        System.out.println("benötigte Züge: " + moves);
//        System.out.println("Markierte Minen: " + minesMarked);
//        System.out.println("Falsch markierte Minen: " + falseMinesMarked);
//        System.out.println("Gesamtanzahl der Minen: " + totalMines);
//        System.out.println("Verbleibende Minen: " + (totalMines - minesMarked));
    }
}