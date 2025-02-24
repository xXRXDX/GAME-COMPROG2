package main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    // --- Logical game resolution (game logic remains based on these values) ---
    final int originalTileSize = 16;               // Base tile size in pixels
    final int scale = 3;                           // Logical scale factor
    final int tileSize = originalTileSize * scale;   // Effective tile size in logical resolution
    final int maxScreenCol = 16;                   // Number of columns
    final int maxScreenRow = 12;                   // Number of rows
    final int gameWidth = tileSize * maxScreenCol;   // Logical game width
    final int gameHeight = tileSize * maxScreenRow;  // Logical game height

    // --- FPS setting ---
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
        // We no longer fix the panel size so the container can scale it (e.g., in full-screen mode).
        // If needed, you can still set a preferred size, but it won't restrict resizing:
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

    public void update() {
        // Update the player's position based on keyboard input (logical resolution).
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

        // Create an offscreen image at the logical resolution.
        BufferedImage gameImage = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dGame = gameImage.createGraphics();

        // --- Render the game scene at the logical resolution ---
        // Fill a dark gray background.
        g2dGame.setColor(Color.darkGray);
        g2dGame.fillRect(0, 0, gameWidth, gameHeight);

        // Optionally, draw grid lines to visualize tiles.
        g2dGame.setColor(Color.gray);
        for (int col = 0; col <= maxScreenCol; col++) {
            int x = col * tileSize;
            g2dGame.drawLine(x, 0, x, gameHeight);
        }
        for (int row = 0; row <= maxScreenRow; row++) {
            int y = row * tileSize;
            g2dGame.drawLine(0, y, gameWidth, y);
        }

        // Draw the player (using logical coordinates).
        g2dGame.setColor(Color.white);
        g2dGame.fillRect(playerX, playerY, playerSize, playerSize);

        // Optionally, display mouse coordinates.
        g2dGame.setColor(Color.red);
        g2dGame.drawString("Mouse: (" + inputHandler.mouseX + ", " + inputHandler.mouseY + ")", 10, 20);

        g2dGame.dispose();

        // --- Scale the offscreen image to fit the current panel size while preserving aspect ratio ---
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        double scaleFactor = Math.min((double) panelWidth / gameWidth, (double) panelHeight / gameHeight);
        int drawWidth = (int) (gameWidth * scaleFactor);
        int drawHeight = (int) (gameHeight * scaleFactor);
        int offsetX = (panelWidth - drawWidth) / 2;
        int offsetY = (panelHeight - drawHeight) / 2;

        Graphics2D g2dPanel = (Graphics2D) g;
        g2dPanel.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dPanel.drawImage(gameImage, offsetX, offsetY, drawWidth, drawHeight, null);
    }
}
