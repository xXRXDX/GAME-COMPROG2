package main;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Star Finding Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        // Set a default window size, for example, 1280x720.
        window.setSize(new Dimension(1280, 720));

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
