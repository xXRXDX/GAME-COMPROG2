package main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    // Change these to any size you want
    private final int columns = 20;
    private final int rows = 15;

    private final TileManager tileManager = new TileManager(columns, rows);

    // Basic game loop at 60 FPS
    private final int FPS = 60;
    private Thread gameThread;

    public GamePanel() {
        // Default size; can be anything
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Add Images
        try {
            Image A1 = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/A1.png"));
            tileManager.setTileImage(0, 0, A1); // Sets custom image for tile "A-1"

            Image J8 = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/J8.png"));
            tileManager.setTileImage(9, 7, J8);  // Sets custom image for tile "J-8"

            Image T15 = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/T15.png"));
            tileManager.setTileImage(19, 14, T15); // Sets custom image for tile "T-15"
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // 1) Update logic (if needed)
            // 2) Render
            repaint();

            // Basic timing
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) Determine tile size to keep them square
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int tileSize = Math.min(panelWidth / columns, panelHeight / rows);

        // 2) Compute the total rendered grid size
        int gridWidth = tileSize * columns;
        int gridHeight = tileSize * rows;

        // 3) Center the grid in the panel
        int xOffset = (panelWidth - gridWidth) / 2;
        int yOffset = (panelHeight - gridHeight) / 2;

        // 4) Draw a background for the entire panel
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.darkGray);
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        // 5) Draw the tiles
        tileManager.drawTiles(g2d, xOffset, yOffset, tileSize);

        // 6) Optionally, show tile IDs (like "A-1") on each tile
        g2d.setColor(Color.black);
        FontMetrics fm = g2d.getFontMetrics();
        for (int row = 0; row < tileManager.getRows(); row++) {
            for (int col = 0; col < tileManager.getColumns(); col++) {
                String id = tileManager.getTile(col, row).getId();
                int textWidth = fm.stringWidth(id);
                int textHeight = fm.getAscent();

                // Center the text inside the tile
                int x = xOffset + col * tileSize + (tileSize - textWidth) / 2;
                int y = yOffset + row * tileSize + (tileSize + textHeight) / 2;
                g2d.drawString(id, x, y);
            }
        }
    }
}
