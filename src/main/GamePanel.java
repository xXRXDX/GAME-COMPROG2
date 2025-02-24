package main;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    // FPS
    final int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // Player Position & Speed
    int playerX = 100;
    int playerY = 100;
    final int playerSpeed = 5;
    final int playerSize = 48;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = (nextDrawTime - System.nanoTime()) / 1_000_000;

                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime);
                }

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Movement Logic
        if (keyH.upPressed && playerY > 0) {
            playerY -= playerSpeed;
        }
        if (keyH.downPressed && playerY + playerSize < screenHeight) {
            playerY += playerSpeed;
        }
        if (keyH.leftPressed && playerX > 0) {
            playerX -= playerSpeed;
        }
        if (keyH.rightPressed && playerX + playerSize < screenWidth) {
            playerX += playerSpeed;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.fillRect(playerX, playerY, playerSize, playerSize);

        g2d.dispose();
    }
}
