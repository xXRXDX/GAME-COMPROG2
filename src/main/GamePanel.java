package main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    // --- Fixed logical grid settings ---
    final int originalTileSize = 16;             // Base tile size in pixels
    final int scale = 3;                         // Scale factor for logical resolution
    final int tileSize = originalTileSize * scale; // Effective tile size (e.g., 48 pixels)
    final int maxScreenCol = 16;                 // Fixed number of columns
    final int maxScreenRow = 12;                 // Fixed number of rows
    final int gameWidth = tileSize * maxScreenCol; // Logical game width (e.g., 768)
    final int gameHeight = tileSize * maxScreenRow; // Logical game height (e.g., 576)

    // --- FPS ---
    final int FPS = 60;

    // --- Input handler ---
    InputHandler inputHandler = new InputHandler();
    Thread gameThread;

    // --- Player properties (logical coordinates) ---
    int playerX = 100;
    int playerY = 100;
    final int playerSpeed = 5;
    final int playerSize = 48;

    public GamePanel() {
        // Set preferred size for initial layout based on logical resolution.
        this.setPreferredSize(new Dimension(gameWidth, gameHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // Register input handlers.
        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler);
        this.addMouseMotionListener(inputHandler);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; // Nanoseconds per frame
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

    // Update game logic using fixed logical resolution boundaries.
    public void update() {
        // Keep the player within the fixed grid boundaries.
        if (inputHandler.upPressed && playerY > 0) {
            playerY -= playerSpeed;
        }
        if (inputHandler.downPressed && playerY + playerSize < gameHeight) {
            playerY += playerSpeed;
        }
        if (inputHandler.leftPressed && playerX > 0) {
            playerX -= playerSpeed;
        }
        if (inputHandler.rightPressed && playerX + playerSize < gameWidth) {
            playerX += playerSpeed;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create an offscreen image at the fixed logical resolution.
        BufferedImage gameImage = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dGame = gameImage.createGraphics();

        // --- Render the game scene at logical resolution ---
        // Fill the background.
        g2dGame.setColor(Color.darkGray);
        g2dGame.fillRect(0, 0, gameWidth, gameHeight);

        // Draw grid lines.
        g2dGame.setColor(Color.gray);
        for (int col = 0; col <= maxScreenCol; col++) {
            int x = col * tileSize;
            g2dGame.drawLine(x, 0, x, gameHeight);
        }
        for (int row = 0; row <= maxScreenRow; row++) {
            int y = row * tileSize;
            g2dGame.drawLine(0, y, gameWidth, y);
        }

        // Draw the player.
        g2dGame.setColor(Color.white);
        g2dGame.fillRect(playerX, playerY, playerSize, playerSize);

        // Optionally, display mouse coordinates in logical space.
        g2dGame.setColor(Color.red);
        g2dGame.drawString("Mouse: (" + inputHandler.mouseX + ", " + inputHandler.mouseY + ")", 10, 20);

        g2dGame.dispose();

        // --- Scale the offscreen image to completely cover the current panel ---
        // This scales the image to the panel dimensions regardless of aspect ratio,
        // ensuring that the fixed grid of 16×12 always fills the window.
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        Graphics2D g2dPanel = (Graphics2D) g;
        // For smooth scaling.
        g2dPanel.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dPanel.drawImage(gameImage, 0, 0, panelWidth, panelHeight, null);
    }
}
