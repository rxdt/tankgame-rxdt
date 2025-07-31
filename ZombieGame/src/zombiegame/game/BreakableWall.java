package zombiegame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
    private boolean destroyed = false;

    public BreakableWall(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!destroyed) {
            g.drawImage(getImage(), getX(), getY(), null);
        }
    }

    public void destroy() {
        this.destroyed = true;
    }

    @Override
    public Rectangle getBounds() {
        return destroyed ? new Rectangle(0, 0, 0, 0) : super.getBounds();
    }
}
