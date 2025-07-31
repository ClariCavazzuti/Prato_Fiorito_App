package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public  class GamePanel extends JPanel {
    private int rows, cols;
    private Cell[][] cells;
    private boolean firstClickDone = false;
    private boolean gameOver = false;
    private int numMines;

    public GamePanel(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        setLayout(new GridLayout(rows, cols));
        setPreferredSize(new Dimension(400, 400));

        cells = new Cell[rows][cols];

        // Inizializza celle
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
                add(cells[i][j]);
            }
        }
        // le miene e i numeri li mettiamo dopo il primo click
    }

    // Nuovo metodo per gestire il primo clic dell'utente
    public void handleFirstClick(int clickedRow, int clickedCol) {
        if (firstClickDone) return; // Già inizializzato

        placeMinesExcluding(clickedRow, clickedCol); // Posiziona mine escludendo l'area del primo clic
        calculateNeighborMines();
        firstClickDone = true;
        revealCell(clickedRow, clickedCol); // Rivelare la cella iniziale
    }

    // Modifica di placeMines per escludere la prima area cliccata
    private void placeMinesExcluding(int startR, int startC) {
        Random rand = new Random();
        int placed = 0;

        // Crea una lista di tutte le possibili posizioni per le mine
        java.util.List<Point> availablePositions = new java.util.ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                availablePositions.add(new Point(i, j));
            }
        }

        // Rimuovi la cella cliccata e le sue vicinanze dalla lista delle posizioni disponibili per le mine
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int ni = startR + dx;
                int nj = startC + dy;
                if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
                    availablePositions.remove(new Point(ni, nj));
                }
            }
        }

        // Posiziona le mine dalle posizioni rimanenti
        while (placed < numMines && !availablePositions.isEmpty()) {
            int index = rand.nextInt(availablePositions.size());
            Point p = availablePositions.remove(index); // Rimuovi per evitare duplicati
            cells[p.x][p.y].setMine(true);
            placed++;
        }
    }

    private void calculateNeighborMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int count = 0;
                if (cells[i][j].hasMine()) {
                    cells[i][j].setNeighborMines(-1); // solo per chiarezza
                    continue;
                }

                // Controlla 8 celle attorno
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int ni = i + dx;
                        int nj = j + dy;
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
                            if (cells[ni][nj].hasMine()) {
                                count++;
                            }
                        }
                    }
                }

                cells[i][j].setNeighborMines(count);
            }
        }
    }

    // Metodo principale per rivelare una cella
    public void revealCell(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || cells[r][c].isClicked() || cells[r][c].isFlagged()) {
            return; // Esci se fuori dai bordi, già cliccata o flaggata
        }

        cells[r][c].reveal(); // Chiama il metodo reveal della cella

        if (cells[r][c].hasMine()) {
            // L'utente ha cliccato su una mina: Game Over!
            endGame(false); // false indica sconfitta
        } else if (cells[r][c].getNeighborMines() == 0) {
            // Se la cella è vuota (0 mine vicine), rivela ricorsivamente le vicine
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue; // Salta la cella corrente
                    revealCell(r + dx, c + dy); // Chiamata ricorsiva
                }
            }
        }
        checkWinCondition(); // Controlla la vittoria dopo ogni rivelazione
    }

    // Metodo per terminare il gioco (vittoria o sconfitta)
    private void endGame(boolean won) {
        gameOver = true; // Imposta lo stato di gioco terminato

        // Rivelare tutte le mine
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].showMine(); // Mostra le mine
            }
        }

        String message;
        if (won) {
            message = "Complimenti! Hai Vinto!";
        } else {
            message = "Hai cliccato su una mina! Hai Perso!";
        }

        // Mostra un messaggio all'utente
        JOptionPane.showMessageDialog(this, message, "Fine del Gioco", JOptionPane.INFORMATION_MESSAGE);

        // Aggiungi qui la logica per un nuovo gioco, se vuoi
        // Es: chiedere all'utente se vuole giocare di nuovo
    }

    // Controlla la condizione di vittoria
    private void checkWinCondition() {
        int revealedNonMines = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j].isClicked() && !cells[i][j].hasMine()) {
                    revealedNonMines++;
                }
            }
        }

        if (revealedNonMines == (rows * cols) - numMines) {
            endGame(true); // true indica vittoria
        }
    }

    // Getter per lo stato del primo clic
    public boolean isFirstClickDone() {
        return firstClickDone;
    }

    // Getter per lo stato del gioco terminato
    public boolean isGameOver() {
        return gameOver;
    }

}


