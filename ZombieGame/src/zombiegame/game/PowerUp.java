package zombiegame.game;

import zombiegame.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObject {
    private long spawnTime;
    private double scale = 0.0; // current scale, 0 to 1
    private boolean growing = true;
    private boolean disappearing = false;
    private boolean fullyDisappeared = false;
    private long animationStartTime;

    public PowerUp(int x, int y, BufferedImage img) {
        super(x, y, 0, 0, 0, img);
    }

    @Override
    public Rectangle getBounds() {
        int width = (int)(img.getWidth() * scale);
        int height = (int)(img.getHeight() * scale);
        float drawX = x + (img.getWidth() - width) / 2;
        float drawY = y + (img.getHeight() - height) / 2;
        return new Rectangle((int)drawX, (int)drawY, width, height);
    }

    @Override
    public void draw(Graphics2D g) {
        int width = (int)(img.getWidth() * scale);
        int height = (int)(img.getHeight() * scale);
        float drawX = x + (img.getWidth() - width) / 2;
        float drawY = y + (img.getHeight() - height) / 2;
        if (scale > 0.01) {
            g.drawImage(img, (int)drawX, (int)drawY, width, height, null);
        }
    }

    public abstract void applyTo(Zombie zombie);

    public void setSpawnTime(long time) {
        this.spawnTime = time;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public void startAppearingAnimation() {
        this.animationStartTime = System.currentTimeMillis();
        this.scale = 0.0;
        this.growing = true;
        this.disappearing = false;
    }

    public void startDisappearingAnimation() {
        this.animationStartTime = System.currentTimeMillis();
        this.growing = false;
        this.disappearing = true;
    }

    public boolean isDisappearing() {
        return disappearing;
    }

    public boolean isFullyDisappeared() {
        return fullyDisappeared;
    }

    public void update() {
        long elapsed = System.currentTimeMillis() - this.animationStartTime;
        double progress = Math.min(1.0, (double) elapsed / GameConstants.ANIMATION_DURATION);
        if (growing) {
            scale = progress;
            if (progress >= 1.0) {
                growing = false;
            }
        } else if (disappearing) {
            scale = 1.0 - progress;
            if (progress >= 1.0) {
                scale = 0.0; // ensure it's fully gone
                disappearing = false;
                fullyDisappeared = true;
            }
        }
    }
}
