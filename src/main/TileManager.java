package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileManager {
    private final int columns;
    private final int rows;
    private final Tile[][] tiles;
    private final Image defaultTileImage;

    public TileManager(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        tiles = new Tile[rows][columns];
        initializeTiles();
        defaultTileImage = createDefaultTileImage(32, 32, Color.lightGray);
    }

    /**
     * Create a Tile for each grid position and assign it an Excel-like ID.
     */
    private void initializeTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                String tileId = GridReference.getTileID(col, row);
                tiles[row][col] = new Tile(tileId);
            }
        }
    }

    /**
     * Creates a simple default tile image of the given width, height, and color.
     */
    private Image createDefaultTileImage(int width, int height, Color color) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return img;
    }

    /**
     * Get a Tile at the specified grid coordinate.
     */
    public Tile getTile(int col, int row) {
        if (col < 0 || col >= columns || row < 0 || row >= rows) {
            return null;
        }
        return tiles[row][col];
    }

    /**
     * Assign an image to a specific tile (e.g., place a sprite or texture).
     */
    public void setTileImage(int col, int row, Image image) {
        Tile tile = getTile(col, row);
        if (tile != null) {
            tile.setImage(image);
        }
    }

    /**
     * Draw the tiles onto a Graphics2D context, using the given offset and tile size.
     */
    public void drawTiles(Graphics2D g2d, int xOffset, int yOffset, int tileSize) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Calculate the top-left pixel of this tile
                int x = xOffset + col * tileSize;
                int y = yOffset + row * tileSize;

                // Draw a basic background for each tile
                g2d.setColor(Color.lightGray);
                g2d.fillRect(x, y, tileSize, tileSize);

                // Draw a border around each tile
                g2d.setColor(Color.gray);
                g2d.drawRect(x, y, tileSize, tileSize);

                // If the tile has an image, draw it. Otherwise, draw the default image.
                Image img = tiles[row][col].getImage();
                if (img == null) {
                    g2d.drawImage(defaultTileImage, x, y, tileSize, tileSize, null);
                } else {
                    g2d.drawImage(img, x, y, tileSize, tileSize, null);
                }
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
