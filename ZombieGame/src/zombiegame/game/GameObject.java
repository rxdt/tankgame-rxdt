package zombiegame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected int width, height;
    protected float x, y, angle;
    protected BufferedImage img;

    public GameObject(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    public abstract void draw(Graphics2D g);
}
