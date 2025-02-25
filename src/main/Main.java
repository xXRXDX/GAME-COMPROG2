package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Excel-Like Grid Example");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Size the window around the panel's preferred size
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game/render loop
        gamePanel.startGameThread();
    }
}
