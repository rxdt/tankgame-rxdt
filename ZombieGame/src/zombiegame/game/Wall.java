package zombiegame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {

    public Wall(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, 0, 0, 0, img);
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g) {
        g.drawImage(img, (int)x, (int)y, width, height, null);
    }

    protected BufferedImage getImage() {
        return this.img;
    }
}
