package zombiegame.game;

import zombiegame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Zombie {
    public enum Direction {UP, DOWN, LEFT, RIGHT, SHOOT}
    private final EnumSet<Direction> keysPressed = EnumSet.noneOf(Direction.class);

    private float x;
    private float y;
    private float vx; // useful for simple approach to collisions, velocity x distance traveled over time
    private float vy; // useful for simple approach to collisions, velocity y

    private float angle; // direction zombie is facing - make sure all assets facing same way
    private float R = 5; // speed
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img; // don't want it static atm because need two zombies that look different
    private BufferedImage bulletImg;
    private List<Bullet> bullets = new ArrayList<>();
    private float facingOffset = 0f;

    private boolean isHit = false;
    private long hitTime = 0;
    private static final int HIT_FLASH_DURATION_MS = 200;

    private int health = GameConstants.MAX_HEALTH; // 100
    private int lives = 3;

    private static final long BOOST_DURATION = 5000; // 5 seconds
    private boolean shielded = false;
    private double speedMultiplier = 1.0;
    private long boostTimer = 0;

    Zombie(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

   public void pressed(Direction dir) {
        keysPressed.add(dir);
    }

    public void released(Direction dir) {
        keysPressed.remove(dir);
    }

    public void update(List<Wall> walls, Zombie otherZombie) {
        resetPowerUps();
        float originalX = x;
        float originalY = y;
        if (this.keysPressed.contains(Direction.UP)) {
            this.moveForwards();
        }
        if (this.keysPressed.contains(Direction.DOWN)) {
            this.moveBackwards();
        }
        if (this.keysPressed.contains(Direction.LEFT)) {
            this.rotateLeft();
        }
        if (this.keysPressed.contains(Direction.RIGHT)) {
            this.rotateRight();
        }
        Rectangle nextBounds = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
        for (Wall wall : walls) {
            if (nextBounds.intersects(wall.getBounds())) {
                // Undo movement
                x = originalX;
                y = originalY;
                return;
            }
        }
        // Check for zombie-zombie collision
        if (otherZombie != null) {
            Rectangle otherBounds = new Rectangle(otherZombie.getX(), otherZombie.getY(), otherZombie.img.getWidth(), otherZombie.img.getHeight());
            if (nextBounds.intersects(otherBounds)) {
                x = originalX;
                y = originalY;
            }
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
        vx = Math.round((float)(R * speedMultiplier * Math.cos(Math.toRadians(angle))));
        vy = Math.round((float)(R * speedMultiplier * Math.sin(Math.toRadians(angle))));
        x += vx;
        y += vy;
        checkBorder();
    }

    // should be GAME_WORLD_HEIGHT, don't go off the world especially during split screen
    private void checkBorder() {
        int zombieWidth = this.img.getWidth();
        int zombieHeight = this.img.getHeight();
        if (x < 0) {
            x = 0;
        }
        if (x > GameConstants.GAME_SCREEN_WIDTH - zombieWidth) {
            x = GameConstants.GAME_SCREEN_WIDTH - zombieWidth;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > GameConstants.GAME_SCREEN_HEIGHT - zombieHeight) {
            y = GameConstants.GAME_SCREEN_HEIGHT - zombieHeight;
        }
    }

    public void drawImage(Graphics2D g) {
        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);
        at.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (isHit && (System.currentTimeMillis() - hitTime < HIT_FLASH_DURATION_MS)) {
            // Glow red if hit
            System.out.println("Render red-tinted zombie");
            BufferedImage tinted = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tinted.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.setColor(new Color(255, 0, 0, 128));
            g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
            g2d.dispose();
            g.drawImage(tinted, at, null);
        } else {
            g.drawImage(this.img, at, null);
            isHit = false; // reset if time passed
        }

        // Health bar above zombie head
        int barWidth = 40;
        int barHeight = 5;
        int barX = (int)x + (img.getWidth() - barWidth) / 2;
        int barY = (int)y - 10;
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int)(barWidth * (health / 100.0)), barHeight);
        // Lives count above zombie head
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Lives: " + lives, x, y - 25);

        // Show shield while active
        if (this.shielded && System.currentTimeMillis() - boostTimer <= GameConstants.FIVE_SECONDS) {
            g.setColor(new Color(0, 255, 255, 255));
            g.drawOval((int)x - 8, (int)y - 8, img.getWidth() + 16, img.getHeight() + 16);
        }
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }

    public void setBulletImage(BufferedImage bulletImg) {
        this.bulletImg = bulletImg;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void fire() {
        float bulletAngle = (angle + facingOffset + 360) % 360;
        float spawnDistance = img.getHeight() / 2f;
        float bulletX = x + img.getWidth() / 2f - bulletImg.getWidth() / 2f +
                (float)(spawnDistance * Math.cos(Math.toRadians(bulletAngle)));
        float bulletY = y + img.getHeight() / 2f - bulletImg.getHeight() / 2f +
                (float)(spawnDistance * Math.sin(Math.toRadians(bulletAngle)));
        bullets.add(new Bullet(bulletX, bulletY, bulletAngle, bulletImg));
    }

    public void setFacingOffset(float offset) {
        this.facingOffset = offset;
    }

    public BufferedImage getImage() {
        return this.img;
    }

    // trigger red flash and play hit sound
    public void onHit() {
        ResourceManager.getInstance().playSound("zombie_hit.wav");
        if (this.shielded && System.currentTimeMillis() - boostTimer <= GameConstants.FIVE_SECONDS) {
            return; // shield still active
        }
        this.health -= 5; // else damage the zombie
    }

    public void heal(int healAmount) {
        this.health = Math.min(100, health + healAmount);
    }

    public void setSpeedBoost(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public void setBoostTimer(long boostTimer) {
        this.boostTimer = boostTimer;
    }

    public void setShield(boolean isShielded) {
        this.shielded = isShielded;
    }

    private void resetPowerUps() {
        if (System.currentTimeMillis() - boostTimer > BOOST_DURATION) {
            speedMultiplier = 1.0;
            shielded = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getLives() {
        return lives;
    }

    public void deductALife() {
        lives--;
        this.health = GameConstants.MAX_HEALTH; // reset health
    }
}
