package zombiegame.game;

import zombiegame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private float speed = GameConstants.BULLET_SPEED;
    private boolean active = true;
    private boolean isLaser;
    private int damage;

    public Bullet(float x, float y, float angle, BufferedImage img, Boolean laserEnabled) {
        super(x, y, 0, 0, angle, img);
        this.isLaser = laserEnabled;
        this.damage = isLaser ? 10 : 5;
        this.speed = isLaser ? 13 : GameConstants.BULLET_SPEED;
    }

    public void update() {
        x = (float) (x + GameConstants.BULLET_SPEED * Math.cos(Math.toRadians(angle)));
        y = (float) (y + GameConstants.BULLET_SPEED * Math.sin(Math.toRadians(angle)));
    }

    public void draw(Graphics2D g) {
        if (!active) return;
        if (this.isLaser) {
            g.setColor(Color.CYAN);
            g.setStroke(new BasicStroke(4));
            int startX = (int) x;
            int startY = (int) y;
            int endX = (int)(x + Math.cos(Math.toRadians(angle)) * 40);
            int endY = (int)(y + Math.sin(Math.toRadians(angle)) * 40);
            g.drawLine(startX, startY, endX, endY);
        } else {
            AffineTransform at = new AffineTransform();
            at.translate(x, y);
            at.rotate(Math.toRadians(angle), img.getWidth() / 2.0, img.getHeight() / 2.0);
            g.drawImage(img, at, null);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDamage() {
        return damage;
    }
}
