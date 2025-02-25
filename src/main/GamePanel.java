package main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    // --- Configurable grid settings ---
    private int originalTileSize = Constants.ORIGINAL_TILE_SIZE;
    private int scale = Constants.SCALE;
    private int tileSize = originalTileSize * scale;
    private int maxScreenCol = Constants.MAX_SCREEN_COL;
    private int maxScreenRow = Constants.MAX_SCREEN_ROW;
    private int gameWidth = maxScreenCol * tileSize;
    private int gameHeight = maxScreenRow * tileSize;

    // --- FPS ---
    private final int FPS = Constants.FPS;

    // --- Input handler and game thread ---
    private final InputHandler inputHandler = new InputHandler();
    private Thread gameThread;

    // --- Player properties ---
    private int playerX = 100, playerY = 100;
    private final int playerSpeed = 5;
    private final int playerSize = 48;

    public GamePanel() {
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);

        // Register input handlers.
        addKeyListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);
        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Change the base tile size (for example, 64).
     */
    public void setOriginalTileSize(int newTileSize) {
        this.originalTileSize = newTileSize;
        this.tileSize = originalTileSize * scale;
        this.gameWidth = maxScreenCol * tileSize;
        this.gameHeight = maxScreenRow * tileSize;
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        revalidate();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                long sleepTime = (long) ((nextDrawTime - System.nanoTime()) / 1_000_000);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create an offscreen image at the fixed logical resolution (gameWidth x gameHeight).
        BufferedImage gameImage = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dGame = gameImage.createGraphics();

        // 1) --- Render the game scene at the logical resolution ---
        // Fill background
        g2dGame.setColor(Color.darkGray);
        g2dGame.fillRect(0, 0, gameWidth, gameHeight);

        // Draw grid lines
        g2dGame.setColor(Color.gray);
        for (int col = 0; col <= maxScreenCol; col++) {
            int x = col * tileSize;
            g2dGame.drawLine(x, 0, x, gameHeight);
        }
        for (int row = 0; row <= maxScreenRow; row++) {
            int y = row * tileSize;
            g2dGame.drawLine(0, y, gameWidth, y);
        }

        // Draw the player
        g2dGame.setColor(Color.white);
        g2dGame.fillRect(playerX, playerY, playerSize, playerSize);

        // Show mouse coordinates
        g2dGame.setColor(Color.red);
        g2dGame.drawString("Mouse: (" + inputHandler.mouseX + ", " + inputHandler.mouseY + ")", 10, 20);

        g2dGame.dispose();

        // 2) --- Determine how to draw the image to keep tiles square ---
        // Get the panel's current size
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Calculate the scale factors for width and height
        double scaleX = (double) panelWidth / (double) gameWidth;
        double scaleY = (double) panelHeight / (double) gameHeight;

        // Use the smaller of the two to preserve aspect ratio (so squares stay squares)
        double finalScale = Math.min(scaleX, scaleY);

        // Compute the final scaled width and height
        int scaledWidth = (int) (gameWidth * finalScale);
        int scaledHeight = (int) (gameHeight * finalScale);

        // Center the image (letterbox/pillarbox)
        int xOffset = (panelWidth - scaledWidth) / 2;
        int yOffset = (panelHeight - scaledHeight) / 2;

        // 3) --- Draw the scaled image ---
        Graphics2D g2dPanel = (Graphics2D) g;
        g2dPanel.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dPanel.drawImage(gameImage, xOffset, yOffset, scaledWidth, scaledHeight, null);
    }

}

