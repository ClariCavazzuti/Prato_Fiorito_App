package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

class Cell extends JPanel {
    private boolean clicked = false;
    private boolean mine = false;
    private boolean flagged = false; // Aggiunto per le bandiere
    private int neighborMines = 0;
    private final int row, col;
    private JLabel label;


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        label = new JLabel(""); // Crea il JLabel una volta
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Aumentato la dimensione del font
        label.setHorizontalAlignment(SwingConstants.CENTER); // Centra il testo
        label.setVerticalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER); // Aggiungi il label al centro
        label.setVisible(false); // Inizialmente non visibile

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ottieni un riferimento al GamePanel genitore
                GamePanel gamePanel = (GamePanel) getParent();

                if (gamePanel.isGameOver()) { // Se il gioco è finito, non fare nulla
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!clicked && !flagged) { // Non cliccare se già cliccata o flaggata
                        if (!gamePanel.isFirstClickDone()) {
                            // Questo è il primo clic dell'utente, inizializza il gioco
                            gamePanel.handleFirstClick(row, col);
                        } else {
                            // Clic normale
                            gamePanel.revealCell(row, col); // Lascia che il GamePanel gestisca la rivelazione
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (!clicked) { // Puoi mettere/togliere la bandiera solo su celle non cliccate
                        flagged = !flagged;
                        if (flagged) {
                            setBackground(Color.YELLOW); // Colore per la bandiera
                            // Potresti anche usare un'icona qui
                        } else {
                            setBackground(Color.DARK_GRAY); // Torna al colore originale
                        }
                    }
                }
            }
        });
    }
    // Metodo per aggiornare il testo del label
    public void updateLabel(String text) {
        label.setText(text);
        label.setVisible(!text.isEmpty()); // Rendi visibile solo se c'è testo
        revalidate();
        repaint();
    }

    // Metodo per rivelare la cella (chiamato dal GamePanel)
    public void reveal() {
        if (clicked) return; // Già rivelata
        clicked = true;
        setBackground(Color.LIGHT_GRAY);

        if (mine) {
            setBackground(Color.RED); // Mina cliccata
            updateLabel("X"); // Simbolo per la mina
        } else {
            if (neighborMines > 0) {
                updateLabel(String.valueOf(neighborMines));
            } else {
                updateLabel("");
            }
        }
        revalidate();
        repaint();
    }

    // Metodo per mostrare la mina (usato a fine gioco)
    public void showMine() {
        if (mine) {
            setBackground(Color.GRAY); // Colore diverso per le mine non cliccate
            updateLabel("M"); // Simbolo per la mina
            revalidate();
            repaint();
        }
    }

    // --- Getter e Setter ---
    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setNeighborMines(int count) {
        this.neighborMines = count;
    }

    public int getNeighborMines() {
        return neighborMines;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isFlagged() {
        return flagged;
    }
}