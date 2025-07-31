package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp {
    protected int x, y;
    protected BufferedImage img;
    protected Rectangle bounds;
    private long spawnTime;

    public PowerUp(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.bounds = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Graphics2D g) {
        g.drawImage(img, x, y, null);
    }

    public abstract void applyTo(Tank tank);

    public void setSpawnTime(long time) {
        this.spawnTime = time;
    }

    public long getSpawnTime() {
        return spawnTime;
    }
}
