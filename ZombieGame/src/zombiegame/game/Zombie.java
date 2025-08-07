package zombiegame.game;

import zombiegame.GameConstants;
import zombiegame.game.resources.ResourceFactory;
import zombiegame.game.resources.ResourceManager;
import zombiegame.game.walls.Wall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class Zombie extends GameObject {
    public enum Direction {UP, DOWN, LEFT, RIGHT, SHOOT}
    private final EnumSet<Direction> keysPressed = EnumSet.noneOf(Direction.class);

    private float vx; // useful for simple approach to collisions, velocity x distance traveled over time
    private float vy; // useful for simple approach to collisions, velocity y
    private float R = 5; // speed
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage bulletImg;
    private List<Bullet> bullets;
    private float facingOffset = 0f;

    private boolean isHit = false;
    private long hitTime = 0;
    private int health = GameConstants.MAX_HEALTH; // 100
    private int lives = GameConstants.LIVES;

    private boolean shielded = false;
    private double speedMultiplier = 1.0;
    private long boostTimer;
    private boolean laserEnabled = false;

    private boolean exploding = false;
    private int explosionFrame = 0;
    private static final int MAX_EXPLOSION_FRAMES = ResourceFactory.explosionFrames.length;
    private static final int EXPLOSION_FRAME_INTERVAL = 200;
    private static final int RESPAWN_DELAY = 2500;
    private long explosionStartTime = 0;
    private long laserStartTime = 0;

    private long lastMovementTime = System.currentTimeMillis();
    private boolean breathing = false;
    private float breathingScale = 1.0f;
    private boolean breathingIn = true;

    private final LinkedList<Integer> konamiBuffer = new LinkedList<>();
    private final List<Integer> konamiCode;
    private boolean konamiGlowingOn = false;
    private long glowStartTime = 0;
    private int glowDuration;
    private boolean showKonamiMessage = false;
    private long konamiMessageStartTime = 0;

    Zombie(float x, float y, float vx, float vy, float angle, BufferedImage img, List<Integer> konamiCode) {
        super(x, y, vx, vy, angle, img);
        this.boostTimer = System.currentTimeMillis();
        this.bullets = new ArrayList<>();
        this.konamiCode = konamiCode;
    }

   public void pressed(Direction dir) {
        keysPressed.add(dir);
    }

    public void released(Direction dir) {
        keysPressed.remove(dir);
    }

    public void registerKeyPress(int keyCode) {
        konamiBuffer.add(keyCode);
        if (konamiBuffer.size() > konamiCode.size()) {
            konamiBuffer.removeFirst();
        }
        if (konamiBuffer.equals(konamiCode)) {
            this.showKonamiMessage = true;
            this.konamiMessageStartTime = System.currentTimeMillis();
            this.heal(100);
            this.shielded = true;
            this.speedMultiplier = GameConstants.SPEED_BOOST;
            this.boostTimer = System.currentTimeMillis();
            this.glowStartTime = System.currentTimeMillis();
            this.konamiGlowingOn = true;
            this.glowDuration = GameConstants.POWERUP_DURATION;
            this.laserEnabled = true;
            this.laserStartTime = System.currentTimeMillis();
            ResourceManager.getInstance().playSound("pick_up.wav");
            System.out.println("KONAMI UNLOCK");
            konamiBuffer.clear();
        }
    }

    public void update(List<Wall> walls, Zombie otherZombie) {
        if (exploding) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;
            if (explosionFrame < MAX_EXPLOSION_FRAMES && elapsed >= explosionFrame * EXPLOSION_FRAME_INTERVAL) {
                explosionFrame++;
            }
            // Wait before respawn
            if (explosionFrame >= MAX_EXPLOSION_FRAMES && elapsed >= MAX_EXPLOSION_FRAMES * EXPLOSION_FRAME_INTERVAL + RESPAWN_DELAY) {
                if (lives > 0) {
                    boolean validLocation = false;
                    int zombieWidth = img.getWidth();
                    int zombieHeight = img.getHeight();
                    while (!validLocation) {
                        float newX = 50 + (float) (Math.random() * (GameConstants.GAME_SCREEN_WIDTH - 100));
                        float newY = 50 + (float) (Math.random() * (GameConstants.GAME_SCREEN_HEIGHT - 100));
                        Rectangle newBounds = new Rectangle((int)newX, (int)newY, zombieWidth, zombieHeight);
                        validLocation = true;
                        for  (Wall wall : walls) {
                            if (newBounds.intersects(wall.getBounds())) {
                                validLocation = false;
                                break;
                            }
                        }
                        if (validLocation) {
                            this.x = newX;
                            this.y = newY;
                        }
                    }
                    health = GameConstants.MAX_HEALTH;
                    exploding = false;
                    explosionFrame = 0;
                    lastMovementTime = System.currentTimeMillis();
                    breathing = false;
                    breathingScale = 1.0f;
                    breathingIn = true;
                }
            }
            return;
        }
        boolean moved = false;
        float originalX = x;
        float originalY = y;
        if (this.keysPressed.contains(Direction.UP)) {
            this.moveForwards();
            moved = true;
        }
        if (this.keysPressed.contains(Direction.DOWN)) {
            this.moveBackwards();
            moved = true;
        }
        if (this.keysPressed.contains(Direction.LEFT)) {
            this.rotateLeft();
            moved = true;
        }
        if (this.keysPressed.contains(Direction.RIGHT)) {
            this.rotateRight();
            moved = true;
        }
        if (moved || x != originalX || y != originalY) {
            lastMovementTime = System.currentTimeMillis();
            breathing = false; // reset breathing state
        } else {
            long idleTime = System.currentTimeMillis() - lastMovementTime;
            if (idleTime > GameConstants.IDLE_TIME_BEFORE_BREATHE) {
                if (!breathing) {
                    ResourceManager.getInstance().playSound("zombie_idle.wav");
                    breathing = true;
                }
                updateBreathingAnimation();
            }
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
            Rectangle otherBounds = new Rectangle((int)otherZombie.getX(), (int)otherZombie.getY(), otherZombie.img.getWidth(), otherZombie.img.getHeight());
            if (nextBounds.intersects(otherBounds)) {
                x = originalX;
                y = originalY;
            }
        }
        resetPowerUps();
    }

    private void updateBreathingAnimation() {
        float delta = 0.0005f;
        if (breathingIn) {
            breathingScale += delta;
            if (breathingScale >= 1.03f) breathingIn = false;
        } else {
            breathingScale -= delta;
            if (breathingScale <= 0.97f) breathingIn = true;
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round((float)(R * speedMultiplier * Math.cos(Math.toRadians(angle))));
        vy =  Math.round((float)(R * speedMultiplier * Math.sin(Math.toRadians(angle))));
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

    @Override
    public void draw(Graphics2D g) {
        if (exploding) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;
            if (explosionFrame < ResourceFactory.explosionFrames.length && elapsed >= explosionFrame * EXPLOSION_FRAME_INTERVAL) {
                explosionFrame++;
            }
            if (explosionFrame < ResourceFactory.explosionFrames.length) {
                g.drawImage(ResourceFactory.explosionFrames[explosionFrame], (int)x, (int)y, null);
            }
            return;
        }
        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);
        at.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (isHit && (System.currentTimeMillis() - hitTime < GameConstants.HIT_FLASH_DURATION_MS)) {
            // Glow red if hit
            BufferedImage tinted = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tinted.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.setColor(new Color(255, 0, 0, 128));
            g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
            g2d.dispose();
            g.drawImage(tinted, at, null);
        } else {
            AffineTransform scaleTransform = new AffineTransform();
            scaleTransform.translate(this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
            scaleTransform.scale(breathing ? breathingScale : 1.0, breathing ? breathingScale : 1.0);
            scaleTransform.translate(-this.img.getWidth() / 2.0, -this.img.getHeight() / 2.0);
            at.concatenate(scaleTransform);
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
        g.drawString("Lives: " + lives, x, y - 15);
        if (this.speedMultiplier > 1.0) {
            g.drawString(GameConstants.SPEED_ON, x, y - 50);
        }
        // Show shield while active
        if (isShieldActive()) {
            g.setColor(new Color(0, 255, 255, 255));
            g.drawOval((int)x - 8, (int)y - 8, img.getWidth() + 16, img.getHeight() + 16);
            g.drawString(GameConstants.SHIELD_ON, x, y - 35);
        }
        if (konamiGlowingOn) {
            float alpha = (float)(Math.sin((System.currentTimeMillis() - glowStartTime) / 200.0) * 0.2 + 0.7);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.setColor(Color.MAGENTA);
            g.setStroke(new BasicStroke(4));
            g.drawOval((int)x-30/2, (int)y-30/2, img.getWidth()+30, img.getHeight()+30);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset
            g.setStroke(new BasicStroke(1));
        }
        if (showKonamiMessage) {
            long elapsed = System.currentTimeMillis() - konamiMessageStartTime;
            if (elapsed < GameConstants.KONAMI_MESSAGE_DURATION) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Comic Sans", Font.BOLD, 22));
                FontMetrics fm = g.getFontMetrics();
                int messageWidth = fm.stringWidth(GameConstants.SECRET_MESSAGE);
                int messageX = (int) x - (messageWidth / 2);
                int messageY = (int) y - 40;
                g.drawString(GameConstants.SECRET_MESSAGE, messageX, messageY);
            } else {
                showKonamiMessage = false;
            }
        }
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
        Bullet newBullet = new Bullet(bulletX, bulletY, bulletAngle, bulletImg, laserEnabled);
        if (laserEnabled) {
            ResourceManager.getInstance().playSound("laser_shot.wav");
        } else {
            ResourceManager.getInstance().playSound("bullet-shot.wav");
        }
        synchronized (this.getBullets()) {
            bullets.add(newBullet);
        }
    }

    public void setFacingOffset(float offset) {
        this.facingOffset = offset;
    }

    public BufferedImage getImage() {
        return this.img;
    }

    // trigger red flash and play hit sound, explode if life lost
    public void onHit(int damage) {
        if (this.lives > 0) {
            ResourceManager.getInstance().playSound("zombie_hit.wav");
        }
        this.health -= damage;
        this.isHit = true;
        this.hitTime = System.currentTimeMillis();
    }

    public void heal(int healAmount) {
        try {
            Thread.sleep(100); // wait 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ResourceManager.getInstance().playSound("healing_pickup.wav");
        this.health = Math.min(100, health + healAmount);
    }

    public void setSpeedBoost(double multiplier) {
        ResourceManager.getInstance().playSound("speed_powering_up.wav");
        System.out.println("Zombie speed at " + multiplier + "x");
        this.speedMultiplier = multiplier;
        this.boostTimer = System.currentTimeMillis(); // Set timer at same time
    }

    public void setBoostTimer() {
        this.boostTimer = System.currentTimeMillis();
    }

    public void setShield(boolean isShielded) {
        ResourceManager.getInstance().playSound("shield_item_pick_up_ding.wav");
        System.out.println("Zombie shielded " + isShielded);
        this.shielded = isShielded;
        this.boostTimer = System.currentTimeMillis(); // Reset boost/shield timer together
    }

    private void resetPowerUps() {
        long timeElapsed = System.currentTimeMillis() - boostTimer;
        if (boostTimer == 0) {
            this.speedMultiplier = 1.0;
            this.shielded = false;
            this.konamiGlowingOn = false;
            return;
        }
        if (timeElapsed > GameConstants.POWERUP_DURATION) {
            this.speedMultiplier = 1.0;
            this.shielded = false;
            this.konamiGlowingOn = false;
        }
        if (laserEnabled && System.currentTimeMillis() - laserStartTime > GameConstants.POWERUP_DURATION) {
            laserEnabled = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getLives() {
        return lives;
    }

    public void deductALife() {
        if (!exploding) {
            lives--;
            // trigger explosion
            exploding = true;
            explosionFrame = 0;
            explosionStartTime = System.currentTimeMillis(); // ← starts explosion timer
        }
    }

    public boolean isShieldActive() {
        return this.shielded && (System.currentTimeMillis() - boostTimer <= GameConstants.POWERUP_DURATION);
    }

    public boolean isExploding() {
        return this.exploding;
    }

    public void enableLaser() {
        System.out.println("Zombie laser enabled");
        ResourceManager.getInstance().playSound("laser_pickup.wav");
        this.laserEnabled = true;
        this.laserStartTime = System.currentTimeMillis();
    }
}
