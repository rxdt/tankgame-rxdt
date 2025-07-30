package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall {
    private int x, y, width, height;
    private BufferedImage texture;

    public Wall(int x, int y, int width, int height, BufferedImage texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.drawImage(texture, x, y, width, height, null);
    }
}
