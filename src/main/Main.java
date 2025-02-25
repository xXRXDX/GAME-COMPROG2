package main;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Star Finding Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setSize(new Dimension(1280, 720));

        GamePanel gamePanel = new GamePanel();
        // To test with a different base tile size, uncomment the next line:
        // gamePanel.setOriginalTileSize(64);

        window.add(gamePanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}

