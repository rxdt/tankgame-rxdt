package zombiegame.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float x, y;
    private float angle;
    private float speed = 10f;
    private BufferedImage img;
    private boolean active = true;

    public Bullet(float x, float y, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
    }

    public void update() {
        x = (float) (x + speed * Math.cos(Math.toRadians(angle)));
        y = (float) (y + speed * Math.sin(Math.toRadians(angle)));
    }

    public void draw(Graphics2D g) {
        if (!active) return;
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), img.getWidth() / 2.0, img.getHeight() / 2.0);
        g.drawImage(img, at, null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
