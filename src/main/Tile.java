package main;

import java.awt.Image;

public class Tile {
    private final String id;
    private Image image; // optional image to display

    public Tile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
