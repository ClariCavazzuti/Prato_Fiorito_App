package org.example;

import javax.swing.*;
import java.awt.*;

public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prato Fiorito - Numeri");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            int rows = 30;
            int cols = 30;
            int numMines = 150;

            GamePanel gamePanel = new GamePanel(rows, cols, numMines);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }
}
