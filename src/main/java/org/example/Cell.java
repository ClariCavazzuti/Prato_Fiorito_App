package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Cell extends JPanel {
    private boolean clicked = false;
    private boolean mine = false;
    private int neighborMines = 0;
    private final int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!clicked) {
                    clicked = true;
                    if (mine) {
                        setBackground(Color.RED);
                    } else {
                        setBackground(Color.LIGHT_GRAY);
                        if (neighborMines > 0) {
                            addLabel(String.valueOf(neighborMines));
                        } else {
                            addLabel(""); // oppure "0"
                        }
                    }
                    repaint();
                }
            }
        });
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        add(label);
        revalidate();
        repaint();
    }

    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setNeighborMines(int count) {
        this.neighborMines = count;
    }
}

