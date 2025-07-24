package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public  class GamePanel extends JPanel {
    private int rows, cols;
    private Cell[][] cells;

    public GamePanel(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
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

        // Posiziona mine
        placeMines(numMines);

        // Calcola numeri vicini
        calculateNeighborMines();
    }

    private void placeMines(int numMines) {
        Random rand = new Random();
        int placed = 0;

        while (placed < numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            if (!cells[r][c].hasMine()) {
                cells[r][c].setMine(true);
                placed++;
            }
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
}


