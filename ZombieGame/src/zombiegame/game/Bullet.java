package zombiegame.game;

import zombiegame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float speed = GameConstants.BULLET_SPEED;
    private boolean active = true;

    public Bullet(float x, float y, float angle, BufferedImage img) {
        super(x, y, 0, 0, angle, img);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
