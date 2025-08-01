package zombiegame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {

    private BufferedImage img;

    public Wall(int x, int y, int width, int height, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.drawImage(img, x, y, width, height, null);
    }

    protected BufferedImage getImage() {
        return this.img;
    }
}
